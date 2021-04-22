package com.joveo.eqrtestsdk.core.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AwsService {
  AmazonEC2 ec2;

  AwsService(String awsId, String secretKey) {
    this.ec2 =
        AmazonEC2ClientBuilder.standard()
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsId, secretKey)))
            .withRegion(Regions.US_EAST_1)
            .build();
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
}
