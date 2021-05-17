package com.joveo.eqrtestsdk.core.mojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.api.Wait;
import com.joveo.eqrtestsdk.api.Waitable;
import com.joveo.eqrtestsdk.core.models.ConversionCodes;
import com.joveo.eqrtestsdk.core.models.MajorMinorVersions;
import com.joveo.eqrtestsdk.core.services.AwsService;
import com.joveo.eqrtestsdk.core.services.DatabaseService;
import com.joveo.eqrtestsdk.core.services.TrackingService;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.OutboundJob;
import com.joveo.eqrtestsdk.models.clickmeterevents.ApplyEvent;
import com.joveo.eqrtestsdk.models.clickmeterevents.ClickEvent;
import com.joveo.eqrtestsdk.models.clickmeterevents.StatsRequest;
import com.typesafe.config.Config;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    this.objectMapper = new ObjectMapper();
    random = new Random();
  }

  private long getRandomNumberWithLength(int n) {
    long numberOfLengthN;
    numberOfLengthN =
        (long) (Math.pow(10.0, n - 1) + random.nextInt((int) (9 * Math.pow(10.0, n - 1))));
    return numberOfLengthN;
  }

  private String getEventId(LocalDateTime eventDate) {
    String uniqueId = "";
    uniqueId =
        uniqueId
            + eventDate.getYear()
            + String.format("%02d", eventDate.getMonthValue())
            + String.format("%02d", eventDate.getDayOfMonth());
    long sevenDigit = getRandomNumberWithLength(7);
    long eightDigit = getRandomNumberWithLength(8);
    uniqueId = uniqueId + sevenDigit + eightDigit;
    return uniqueId;
  }

  private String getRandomIp() {
    String randomIp = "";
    randomIp =
        randomIp
            + random.nextInt(256)
            + "."
            + random.nextInt(256)
            + "."
            + random.nextInt(256)
            + "."
            + random.nextInt(256);
    return randomIp;
  }

  private String generateSingleClick(LocalDateTime eventDate, OutboundJob job)
      throws MojoException {
    ClickEvent clickEvent = new ClickEvent();

    clickEvent.setDefaultValues();

    clickEvent.setId(getEventId(eventDate));
    clickEvent.setTimestamp(eventDate);
    clickEvent.setUser(getRandomNumberWithLength(6));

    clickEvent.setLocation(getRandomIp(), "in", "Visakhapatnam", 17.6868, 83.2185);

    long dataPointId = getRandomNumberWithLength(9);

    URL url = null;
    try {
      url = new URL(URLDecoder.decode(job.getUrl(), "UTF-8"));
    } catch (MalformedURLException e) {
      String errorMessage = "Malformed job url : " + e.getMessage();
      logger.error(errorMessage);
      throw new MojoException(errorMessage);
    } catch (UnsupportedEncodingException e) {
      logger.error(e.getMessage());
      throw new MojoException(e.getMessage());
    }

    String encodedParams = URLEncoder.encode(url.getQuery());
    String shortUrl = URLEncoder.encode(url.toString());
    String destinationUrl = URLEncoder.encode("https://trk-staging.jometer.com/?" + encodedParams);

    clickEvent.setDatapoint(dataPointId, "/datapoints/" + dataPointId, shortUrl, destinationUrl);

    clickEvent.setParams(encodedParams);

    String event = null;
    try {
      event = objectMapper.writeValueAsString(clickEvent);
    } catch (JsonProcessingException e) {
      String errorMessage = "Exception raised while serializing click event : " + e.getMessage();
      logger.error(errorMessage);
      throw new MojoException(errorMessage);
    }
    logger.info("Click event : " + event);
    return event;
  }

  private List<String> generateClickEvents(
      Map<LocalDateTime, Integer> clicks, List<OutboundJob> outboundJobs) throws MojoException {
    List<String> clickEvents = new ArrayList<>();
    for (OutboundJob job : outboundJobs) {
      for (Map.Entry<LocalDateTime, Integer> click : clicks.entrySet()) {
        LocalDateTime eventDate = click.getKey();
        Integer clicksCount = click.getValue();
        for (long clickItr = 0; clickItr < clicksCount; clickItr++) {
          clickEvents.add(generateSingleClick(eventDate, job));
        }
      }
    }
    return clickEvents;
  }

  private String generateSingleApply(
      LocalDateTime eventDate, OutboundJob job, String clientId, ConversionCodes conversionCodes)
      throws MojoException {
    ClickEvent clickEvent = new ClickEvent();

    clickEvent.setEvent("click");
    clickEvent.setTimestamp(eventDate);
    clickEvent.setId(getEventId(eventDate));
    clickEvent.setUser(getRandomNumberWithLength(6));

    long dataPointId = getRandomNumberWithLength(9);

    URL url = null;
    try {
      url = new URL(URLDecoder.decode(job.getUrl(), "UTF-8"));
    } catch (MalformedURLException e) {
      String errorMessage = "Malformed job url : " + e.getMessage();
      logger.error(errorMessage);
      throw new MojoException(errorMessage);
    } catch (UnsupportedEncodingException e) {
      logger.error(e.getMessage());
      throw new MojoException(e.getMessage());
    }

    String encodedParams = URLEncoder.encode(url.getQuery());
    String destinationUrl = URLEncoder.encode("https://trk-staging.jometer.com/?" + encodedParams);
    clickEvent.setDatapoint(dataPointId, "/datapoints/" + dataPointId, null, destinationUrl);

    clickEvent.setDomain(1501, "/domains/1501", "9nl.es");
    clickEvent.setGroup(11096543, "/groups/" + dataPointId);
    clickEvent.setUnique(null);

    clickEvent.setAgent(
        "human", null, null, null, null, null, "en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8");

    clickEvent.setLocation(getRandomIp(), null, null, null, null);

    clickEvent.setParams(encodedParams);

    ApplyEvent applyEvent = new ApplyEvent();
    applyEvent.setEvent("conversion");
    applyEvent.setTimestamp(eventDate);
    applyEvent.setId(getEventId(LocalDateTime.now()));

    Long conversionId = conversionCodes.getConversionId();
    String conversionCode = conversionCodes.getConversionCode();
    applyEvent.setData(
        clickEvent, conversionId, "/conversions/" + conversionId, "?id=" + conversionCode);

    String event = null;
    try {
      event = objectMapper.writeValueAsString(applyEvent);
    } catch (JsonProcessingException e) {
      String errorMessage = "Exception raised while trying to serialize pojo to json : " + e;
      logger.error(errorMessage);
      throw new MojoException(errorMessage);
    }
    return event;
  }

  private List<String> generateApplyEvents(
      Map<LocalDateTime, Integer> applies,
      List<OutboundJob> outboundJobs,
      String clientId,
      ConversionCodes conversionCodes)
      throws MojoException {
    List<String> applyEvents = new ArrayList<>();
    for (OutboundJob job : outboundJobs) {
      for (Map.Entry<LocalDateTime, Integer> apply : applies.entrySet()) {
        LocalDateTime eventDate = apply.getKey();
        Integer appliesCount = apply.getValue();
        for (long applyItr = 0; applyItr < appliesCount; applyItr++) {
          applyEvents.add(generateSingleApply(eventDate, job, clientId, conversionCodes));
        }
      }
    }
    return applyEvents;
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
    logger.info("Events generation started...");
    List<String> allClickEvents = new ArrayList<>();
    List<String> allApplyEvents = new ArrayList<>();
    Map<String, ConversionCodes> clientIdToConversionCodes = new HashMap<>();

    for (StatsRequest statsRequest : statsRequestList) {
      List<OutboundJob> outboundJobs = statsRequest.getJobsInStats();
      String clientId = statsRequest.getClientId();
      // generateClickEvents
      allClickEvents.addAll(generateClickEvents(statsRequest.getClicks(), outboundJobs));

      if (!clientIdToConversionCodes.containsKey(clientId)) {
        clientIdToConversionCodes.put(clientId, databaseService.getApplyConversionCodes(clientId));
      }
      // generateAppliesEvents();
      allApplyEvents.addAll(
          generateApplyEvents(
              statsRequest.getApplies(),
              outboundJobs,
              clientId,
              clientIdToConversionCodes.get(clientId)));
    }

    logger.info("Click Queue URL : " + this.clicksQueue);
    awsService.sendSqsMessages(allClickEvents, this.clicksQueue);

    logger.info("Apply Queue URL : " + this.appliesQueue);
    awsService.sendSqsMessages(allApplyEvents, this.appliesQueue);

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
    try {
      Thread.sleep(180000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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
