package com.joveo.eqrtestsdk.core.mojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.services.SchedulerService;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.OutboundFeedDto;
import com.joveo.eqrtestsdk.models.OutboundJob;
import com.typesafe.config.Config;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutboundFeed {
  private static Logger logger = LoggerFactory.getLogger(OutboundFeed.class);
  private static ObjectMapper xmlMapper = new XmlMapper();

  private SchedulerService schedulerService;
  private Session session;
  private Config config;
  private String clientId;
  private String feedUrl;
  private OutboundFeedDto feed;
  private LocalDateTime feedRefreshTime;

  static {
    xmlMapper.registerModule(new JavaTimeModule());
  }

  /**
   * .
   *
   * @param feedUrl feedUrl
   * @param clientId clientId
   * @param schedulerService schedulerService
   * @param session session
   * @param config config
   * @throws InvalidInputException On invalid credentials
   * @throws UnexpectedResponseException On unexpected Response
   */
  public OutboundFeed(
      String feedUrl,
      String clientId,
      SchedulerService schedulerService,
      Session session,
      Config config)
      throws InvalidInputException, UnexpectedResponseException {
    this.feedUrl = feedUrl;
    this.schedulerService = schedulerService;
    this.session = session;
    this.config = config;
    this.clientId = clientId;
    refreshFeed();
  }

  public OutboundFeedDto getFeed() {
    return feed;
  }

  /**
   * . This method is used to refresh the feed
   *
   * @throws InvalidInputException On invalid URL
   * @throws UnexpectedResponseException The API response was not as expected On unexpected Response
   */
  public void refreshFeed() throws InvalidInputException, UnexpectedResponseException {
    try {
      this.feed = xmlMapper.readValue(new URL(this.feedUrl), OutboundFeedDto.class);
    } catch (MalformedURLException e) {
      logger.error(e.getMessage());
      throw new InvalidInputException("invalid URL for outbound feed " + this.feedUrl);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Error parsing outbound feed " + this.feedUrl, e);
    }
    this.feedRefreshTime = LocalDateTime.now();
  }

  /**
   * . Checks whether we have latest feed or not
   *
   * @return returns true when the feed is stale
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected On unexpected Response
   */
  public Boolean isStale() throws ApiRequestException, UnexpectedResponseException {
    return schedulerService
        .getLatestRunMetadata(session, clientId, config.getString("MojoBaseUrl"))
        .getTime()
        .isAfter(this.feedRefreshTime);
  }

  /**
   * . Number of jobs present
   *
   * @return returns number of jobs present
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public int size() throws MojoException {
    if (Boolean.TRUE.equals(this.isStale())) {
      refreshFeed();
    }
    return feed.getJob().size();
  }

  /**
   * . checks presence of Reference number in Outbound Feed
   *
   * @param referenceNumber unique reference number of a Job
   * @return true when reference number present in outbound feed
   * @throws InvalidInputException On invalid input
   */
  public boolean isReferenceNumberPresent(String referenceNumber) throws InvalidInputException {
    if (referenceNumber == null) {
      throw new InvalidInputException("Reference number should not be a NULL value");
    }
    List<OutboundJob> jobs = feed.getJob();
    for (OutboundJob job : jobs) {
      if (job.referencenumber.equals(referenceNumber)) {
        return true;
      }
    }
    return false;
  }

  /**
   * . checks presence of list of Reference numbers in Outbound Feed
   *
   * @param referenceNumbers list of unique reference number of a Job
   * @return true when all reference numbers present in outbound feed
   * @throws InvalidInputException when list of reference numbers is empty
   */
  public boolean isAllReferenceNumbersPresent(List<String> referenceNumbers)
      throws InvalidInputException {
    if (referenceNumbers.isEmpty()) {
      throw new InvalidInputException("Please provide atleast one reference number");
    }
    Set<String> referenceNumbersSet = new HashSet<String>(referenceNumbers);
    Set<String> referenceNumbersInOutBoundFeed = new HashSet<String>();
    List<OutboundJob> jobs = feed.getJob();
    for (OutboundJob job : jobs) {
      referenceNumbersInOutBoundFeed.add(job.referencenumber);
    }
    referenceNumbersSet.removeAll(referenceNumbersInOutBoundFeed);
    return referenceNumbersSet.size() == 0;
  }
}
