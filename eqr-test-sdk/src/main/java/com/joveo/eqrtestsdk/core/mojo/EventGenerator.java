package com.joveo.eqrtestsdk.core.mojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.core.models.ConversionCodes;
import com.joveo.eqrtestsdk.core.models.SponsoredEvents;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.OutboundJob;
import com.joveo.eqrtestsdk.models.clickmeterevents.ApplyEvent;
import com.joveo.eqrtestsdk.models.clickmeterevents.ClickEvent;
import com.joveo.eqrtestsdk.models.clickmeterevents.StatsRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventGenerator {
  private static final Logger logger = LoggerFactory.getLogger(EventGenerator.class);
  private final ObjectMapper objectMapper;
  private static Random random;

  EventGenerator() {
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

  private String generateSingleClick(LocalDateTime eventDate, OutboundJob job, Boolean isBot)
      throws MojoException {
    ClickEvent clickEvent = new ClickEvent();

    clickEvent.setDefaultValues();

    if (isBot) {
      clickEvent.setAgentType("bot");
    }

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
      Map<LocalDateTime, Integer> clicks, List<OutboundJob> outboundJobs, Boolean isBot)
      throws MojoException {
    List<String> clickEvents = new ArrayList<>();
    for (OutboundJob job : outboundJobs) {
      for (Map.Entry<LocalDateTime, Integer> click : clicks.entrySet()) {
        LocalDateTime eventDate = click.getKey();
        Integer clicksCount = click.getValue();
        for (long clickItr = 0; clickItr < clicksCount; clickItr++) {
          clickEvents.add(generateSingleClick(eventDate, job, isBot));
        }
      }
    }
    return clickEvents;
  }

  private String generateSingleApply(
      LocalDateTime eventDate, OutboundJob job, long conversionId, String conversionCode)
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
      long conversionId,
      String conversionCode)
      throws MojoException {
    List<String> applyEvents = new ArrayList<>();
    for (OutboundJob job : outboundJobs) {
      for (Map.Entry<LocalDateTime, Integer> apply : applies.entrySet()) {
        LocalDateTime eventDate = apply.getKey();
        Integer appliesCount = apply.getValue();
        for (long applyItr = 0; applyItr < appliesCount; applyItr++) {
          applyEvents.add(generateSingleApply(eventDate, job, conversionId, conversionCode));
        }
      }
    }
    return applyEvents;
  }

  /**
   * This method is used for generating click meter events.
   *
   * @param sponsoredEvents Sponsored Events
   * @param statsRequest Stats Request
   * @param outboundJobs Outbound Jobs
   * @param conversionCodes Conversion Codes
   * @throws MojoException throws custom mojo exception on unexpected behaviour
   */
  public void generateClickMeterEvents(
      SponsoredEvents sponsoredEvents,
      StatsRequest statsRequest,
      List<OutboundJob> outboundJobs,
      ConversionCodes conversionCodes)
      throws MojoException {
    logger.info("Events generation started...");
    // generateSponsoredClickEvents
    sponsoredEvents.setAllSponsoredClickEvents(
        generateClickEvents(statsRequest.getSponsoredClicks(), outboundJobs, false));

    // generateSponsoredBotClicks
    sponsoredEvents.setAllSponsoredBotClickEvents(
        generateClickEvents(statsRequest.getSponsoredBotClicks(), outboundJobs, true));

    // generateSponsoredApplyStartsEvents
    sponsoredEvents.setAllSponsoredApplyStartEvents(
        generateApplyEvents(
            statsRequest.getSponsoredApplyStarts(),
            outboundJobs,
            conversionCodes.getApplyStartConversionId(),
            conversionCodes.getApplyStartConversionCode()));

    // generateSponsoredApplyFinishesEvents
    sponsoredEvents.setAllSponsoredApplyFinishEvents(
        generateApplyEvents(
            statsRequest.getSponsoredApplyFinishes(),
            outboundJobs,
            conversionCodes.getApplyFinishConversionId(),
            conversionCodes.getApplyFinishConversionCode()));
  }
}
