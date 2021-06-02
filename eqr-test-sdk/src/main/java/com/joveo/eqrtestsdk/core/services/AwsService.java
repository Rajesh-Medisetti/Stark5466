package com.joveo.eqrtestsdk.core.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.google.common.collect.Lists;
import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.exception.S3IoException;
import com.joveo.eqrtestsdk.exception.SqsEventFailedException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwsService {
  private static Logger logger = LoggerFactory.getLogger(AwsService.class);

  AmazonEC2 ec2;
  AmazonS3 s3;
  AmazonSQS sqs;

  /** constructor to get aws ec2 and s3 instance. */
  @Inject
  public AwsService() {
    this.ec2 = AmazonEC2ClientBuilder.standard().build();
    this.s3 = AmazonS3ClientBuilder.standard().build();
    this.sqs = AmazonSQSClientBuilder.standard().build();
  }

  /**
   * get tags.
   *
   * @param tag AWS Tag
   * @param value AWS tag value
   * @return List of IP addresses
   */
  public List<String> getIpByTags(String tag, String value) {
    DescribeInstancesRequest request = new DescribeInstancesRequest();
    Filter filter = new Filter("tag:" + tag, Collections.singletonList(value));
    Filter activeIps = new Filter("instance-state-name", Collections.singletonList("running"));
    DescribeInstancesResult result = ec2.describeInstances(request.withFilters(filter, activeIps));
    List<Reservation> reservations = result.getReservations();
    ArrayList<String> ipList = new ArrayList<>();
    for (Reservation reservation : reservations) {
      List<Instance> instances = reservation.getInstances();
      for (Instance instance : instances) {
        ipList.add(instance.getPublicIpAddress());
      }
    }
    return ipList;
  }

  /**
   * Upload file in s3.
   *
   * @param bucketName s3 bucket name
   * @param awsFilePath file path in s3
   * @param file file content as string
   * @param contentType content type for metadata
   * @return File url
   * @throws S3IoException IoException in S3
   */
  public String uploadFile(
      String bucketName, String awsFilePath, String file, MediaType contentType)
      throws S3IoException {

    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(contentType.toString());
      InputStream inputStream = new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8));
      PutObjectRequest request =
          new PutObjectRequest(bucketName, awsFilePath, inputStream, metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead);
      s3.putObject(request);
      String objectUrl = s3.getUrl(bucketName, awsFilePath).toString();

      logger.info("File Url: " + objectUrl);
      return objectUrl;

    } catch (AmazonServiceException e) {
      String errorMessage =
          "S3 couldn't process the request: "
              + e.getMessage()
              + " Status code- "
              + e.getErrorCode();
      logger.error(errorMessage);
      throw new S3IoException(errorMessage);
    } catch (SdkClientException e) {
      logger.error(
          "S3 couldn't be contacted for response or couldn't parse the response: "
              + e.getMessage());
      throw new S3IoException(
          "S3 couldn't be contacted for response or couldn't parse the response: "
              + e.getMessage());
    }
  }

  /**
   * delete object in s3 bucket.
   *
   * @param url file path in s3 bucket
   */
  public void deleteS3Object(String bucketName, String url)
      throws AmazonServiceException, SdkClientException {
    String awsFilePath = new AmazonS3URI(url).getKey();

    try {
      s3.deleteObject(new DeleteObjectRequest(bucketName, awsFilePath));
    } catch (AmazonServiceException e) {
      String errorMessage =
          "S3 couldn't process the request: "
              + e.getMessage()
              + " Status code- "
              + e.getErrorCode();
      logger.error(errorMessage);
      throw new AmazonServiceException(errorMessage);
    } catch (SdkClientException e) {
      logger.error(
          "S3 couldn't be contacted for response or couldn't parse the response: "
              + e.getMessage());
      throw new SdkClientException(
          "S3 couldn't be contacted for response or couldn't parse the response: "
              + e.getMessage());
    }
  }

  /**
   * This method checks whether file exists in given s3 bucket.
   *
   * @param bucketName Name of S3 Bucket
   * @param objectName File path
   * @return returns presence of file path.
   */
  public boolean doesFileExistsInS3(String bucketName, String objectName) {
    return s3.doesObjectExist(bucketName, objectName);
  }

  /**
   * This method takes a list of messages and pushes them to the given queue in a batch of 10.
   *
   * @param messages List of messages
   * @param url Queue URL
   * @throws SqsEventFailedException throws when event gets failed
   */
  public void sendSqsMessages(List<String> messages, String url) throws SqsEventFailedException {
    logger.info("Events are getting pushed into SQS..." + url);
    List<List<String>> batches = Lists.partition(messages, 10);
    for (List<String> batch : batches) {
      List<SendMessageBatchRequestEntry> messageBatch = new ArrayList<>();
      for (int msgNo = 0; msgNo < batch.size(); msgNo++) {
        messageBatch.add(
            new SendMessageBatchRequestEntry()
                .withId(String.valueOf(msgNo))
                .withMessageBody(batch.get(msgNo)));
      }
      SendMessageBatchRequest sendMessageBatchRequest =
          new SendMessageBatchRequest(url, messageBatch);
      SendMessageBatchResult result = sqs.sendMessageBatch(sendMessageBatchRequest);
      logger.info("Successful messages in queue : " + result.getSuccessful().size());
      if (result.getFailed().size() > 0) {
        String errorMessage =
            "Total failed messages while pushing to SQS: " + result.getFailed().size();
        logger.error(errorMessage);
        throw new SqsEventFailedException(errorMessage);
      }
    }
    logger.info("All events are pushed to SQS...");
  }
}
