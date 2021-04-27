package com.joveo.eqrtestsdk.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.typesafe.config.Config;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedService {
  private static Logger logger = LoggerFactory.getLogger(FeedService.class);
  private XmlMapper xmlMapper;

  @Inject
  FeedService(XmlMapper xmlMapper) {
    this.xmlMapper = xmlMapper;
  }

  /**
   * get feed url hosted in aws s3.
   *
   * @param awsService aws service instance
   * @param feed       feedDto instance
   * @return xml feed url
   * @throws InvalidInputException throws invalid input exception
   */
  public String getFeedUrl(Config config, AwsService awsService, FeedDto feed)
      throws InvalidInputException {

    try {
      String xml = xmlMapper.writeValueAsString(feed);
      String path = "inbound-feeds/" + LocalDate.now() + "/" + UUID.randomUUID() + ".xml";
      return awsService.uploadXmlFeed(config.getString("BucketName"), path, xml);
    } catch (JsonProcessingException e) {
      logger.error("Unable to serialize feed to xml: " + e.getMessage());
      throw new InvalidInputException("Unable to serialize feed to xml: " + e.getMessage());
    }
  }


  public void deleteFeedUrl(Config config, AwsService awsService, String feedUrl)
      throws InvalidInputException {
    awsService.deleteS3Object(config.getString("BucketName"), feedUrl);
  }
}