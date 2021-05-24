package com.joveo.eqrtestsdk.core.mojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.api.Wait;
import com.joveo.eqrtestsdk.api.Waitable;
import com.joveo.eqrtestsdk.core.models.ConversionCodes;
import com.joveo.eqrtestsdk.core.models.MajorMinorVersions;
import com.joveo.eqrtestsdk.core.models.SponsoredEvents;
import com.joveo.eqrtestsdk.core.services.AwsService;
import com.joveo.eqrtestsdk.core.services.DatabaseService;
import com.joveo.eqrtestsdk.core.services.TrackingService;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.RedisIoException;
import com.joveo.eqrtestsdk.exception.SqsEventFailedException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.OutboundJob;
import com.joveo.eqrtestsdk.models.clickmeterevents.StatsRequest;
import com.typesafe.config.Config;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficGenerator implements Waitable {
  private static Logger logger = LoggerFactory.getLogger(TrafficGenerator.class);

  private Session session;
  private Config config;
  private AwsService awsService;
  private TrackingService trackingService;
  private DatabaseService databaseService;

  private String clicksQueue;
  private String appliesQueue;
  private Duration timeout;
  private Duration refreshInterval;
  private ObjectMapper objectMapper;
  private static Random random;

  private MajorMinorVersions prevVersions;

  /**
   * Constructor of Traffic Generator.
   *
   * @param session Session
   * @param config Configuration
   * @param awsService AWS Service used for pushing messages to SQS
   * @param trackingService Tracking Service used for querying redis and gandalf API
   * @param databaseService Database Service used for querying MongoDB
   */
  public TrafficGenerator(
      Session session,
      Config config,
      AwsService awsService,
      TrackingService trackingService,
      DatabaseService databaseService) {
    this.session = session;
    this.config = config;
    this.awsService = awsService;
    this.trackingService = trackingService;
    this.databaseService = databaseService;
    this.clicksQueue = config.getString("BaseCMQueue") + config.getString("CMClickQueue");
    this.appliesQueue = config.getString("BaseCMQueue") + config.getString("CMApplyQueue");
    this.timeout = config.getDuration("RedisTimeout");
    this.refreshInterval = config.getDuration("RedisPollingInterval");
  }

  private void pushClickMeterEventsToSqs(SponsoredEvents sponsoredEvents)
      throws SqsEventFailedException {
    logger.info("Click Queue URL : " + this.clicksQueue);
    awsService.sendSqsMessages(sponsoredEvents.getAllSponsoredClickEvents(), this.clicksQueue);

    awsService.sendSqsMessages(sponsoredEvents.getAllSponsoredBotClickEvents(), this.clicksQueue);

    logger.info("Apply Queue URL : " + this.appliesQueue);
    awsService.sendSqsMessages(
        sponsoredEvents.getAllSponsoredApplyStartEvents(), this.appliesQueue);

    awsService.sendSqsMessages(
        sponsoredEvents.getAllSponsoredApplyFinishEvents(), this.appliesQueue);
  }

  private void populateClickMeterEventsToMojo()
      throws MojoException, RedisIoException, InterruptWaitException, ApiRequestException,
          UnexpectedResponseException {
    prevVersions = trackingService.getVersions();
    logger.info(
        "Previous major and minor version in redis : "
            + prevVersions.getMajorVersion()
            + " and "
            + prevVersions.getMinorVersion());

    logger.info("Wait for 180 seconds for the events to record in MongoDB...");

    try {
      Thread.sleep(180000);
    } catch (InterruptedException e) {
      logger.error(
          "Three minutes wait time for getting records into mongo failed due to an interrupt");
      throw new InterruptWaitException(
          "Three minutes wait time for getting records into mongo failed due to an interrupt", e);
    }

    trackingService.runGandalf(session, config.getString("GandalfMajorRunUrl"));

    Wait.until(this);

    logger.info("Click and Apply events are now available in Mojo UI : " + LocalDateTime.now());
  }

  /**
   * This method is responsible for creating and pushing events to respective Queues and triggering
   * Gandalf.
   *
   * @param statsRequestList List of Stats requests
   * @throws MojoException throws custom mojo exception Something went wrong
   * @throws InterruptWaitException on Interrupt
   */
  public void run(List<StatsRequest> statsRequestList)
      throws MojoException, InterruptWaitException {
    SponsoredEvents sponsoredEvents = new SponsoredEvents();
    EventGenerator eventGenerator = new EventGenerator();
    Map<String, ConversionCodes> clientIdToConversionCodes = new HashMap<>();

    for (StatsRequest statsRequest : statsRequestList) {
      List<OutboundJob> outboundJobs = statsRequest.getJobsInStats();
      String clientId = statsRequest.getClientId();

      if (!clientIdToConversionCodes.containsKey(clientId)) {
        clientIdToConversionCodes.put(clientId, databaseService.getApplyConversionCodes(clientId));
      }

      eventGenerator.generateClickMeterEvents(
          sponsoredEvents, statsRequest, outboundJobs, clientIdToConversionCodes.get(clientId));
    }

    pushClickMeterEventsToSqs(sponsoredEvents);

    populateClickMeterEventsToMojo();
  }

  @Override
  public Boolean isComplete() throws MojoException {

    MajorMinorVersions currentVersions = trackingService.getVersions();

    if (currentVersions.getMajorVersion() > prevVersions.getMajorVersion()) {
      logger.info(
          "Major run updated at: "
              + LocalDateTime.now()
              + " and major run version number : "
              + currentVersions.getMajorVersion());
      if (currentVersions.getMinorVersion() == prevVersions.getMinorVersion() + 2) {
        logger.info(
            "Minor run updated at: "
                + LocalDateTime.now()
                + " and minor run version number : "
                + currentVersions.getMinorVersion());
        return true;
      }
      logger.info("Wait for 5 minutes for minor run to complete");
      try {
        Thread.sleep(300000);
      } catch (InterruptedException e) {
        logger.error("Wait for " + this.getWaiterMessage() + " failed due to an interrupt");
        throw new InterruptWaitException(
            "Wait for " + this.getWaiterMessage() + " failed due to an interrupt", e);
      }
      return true;
    }
    logger.info("Major and Minor run didn't complete...");
    return false;
  }

  @Override
  public Duration getTimeout() {
    return this.timeout;
  }

  @Override
  public String getWaiterMessage() {
    return "Gandalf";
  }

  @Override
  public Duration getRefreshInterval() {
    return this.refreshInterval;
  }
}
