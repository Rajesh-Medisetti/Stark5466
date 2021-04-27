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
import com.google.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwsService {
  private static Logger logger = LoggerFactory.getLogger(ClientService.class);

  AmazonEC2 ec2;
  AmazonS3 s3;

  /** constructor to get aws ec2 and s3 instance. */
  @Inject
  public AwsService() {
    this.ec2 = AmazonEC2ClientBuilder.standard().build();
    this.s3 = AmazonS3ClientBuilder.standard().build();
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
    Filter filter = new Filter("tag:" + tag, Arrays.asList(value));
    DescribeInstancesResult result = ec2.describeInstances(request.withFilters(filter));
    List<Reservation> reservations = result.getReservations();
    ArrayList<String> ipList = new ArrayList<>();
    for (Reservation reservation : reservations) {
      List<Instance> instances = reservation.getInstances();
      for (Instance instance : instances) {
        ipList.add(instance.getPrivateIpAddress());
      }
    }
    return ipList;
  }

  /**
   * upload xml feed in s3 bucket.
   *
   * @param xml xml feed in string format
   * @return url of xml feed
   */
  public String uploadXmlFeed(String bucketName, String awsFilePath, String xml)
      throws AmazonServiceException, SdkClientException {

    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("text/xml");
      InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
      PutObjectRequest request =
          new PutObjectRequest(bucketName, awsFilePath, inputStream, metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead);
      s3.putObject(request);
      String objectUrl = s3.getUrl(bucketName, awsFilePath).toString();

      logger.info("feed url: " + objectUrl);
      return objectUrl;

    } catch (AmazonServiceException e) {
      logger.error("S3 couldn't process the request: " + e.getMessage());
      throw new AmazonServiceException("S3 couldn't process the request: " + e.getMessage());
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
      logger.error("S3 couldn't process the request: " + e.getMessage());
      throw new AmazonServiceException("S3 couldn't process the request: " + e.getMessage());
    } catch (SdkClientException e) {
      logger.error(
          "S3 couldn't be contacted for response or couldn't parse the response: "
              + e.getMessage());
      throw new SdkClientException(
          "S3 couldn't be contacted for response or couldn't parse the response: "
              + e.getMessage());
    }
  }
}
