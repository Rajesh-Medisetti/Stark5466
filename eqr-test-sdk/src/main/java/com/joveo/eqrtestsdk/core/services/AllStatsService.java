package com.joveo.eqrtestsdk.core.services;

import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.allstatsevents.AllStatsEvent;
import com.joveo.eqrtestsdk.models.allstatsevents.AllStatsRequest;
import com.typesafe.config.Config;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllStatsService {
  private static Logger logger = LoggerFactory.getLogger(AllStatsService.class);
  private JoveoHttpExecutor executor;
  private String ipAddress;

  @Inject
  public AllStatsService(JoveoHttpExecutor executor) throws UnknownHostException {
    this.executor = executor;
    this.ipAddress = InetAddress.getLocalHost().getHostAddress().trim();
  }

  private void checkInvalidationConditions(int statsCount, int botClicksCount)
      throws InvalidInputException {
    String error;
    if (statsCount > 0 && botClicksCount > 0) {
      error = "We have a restriction not to generate bot clicks and other events in one go.";
      logger.error(error);
      throw new InvalidInputException(error);
    }
    if (statsCount >= 30) {
      error = "Total Stats generated from single IP can't be more than 29 for a single day.";
      logger.error(error);
      throw new InvalidInputException(error);
    }
  }

  /* This method is used to check whether AllStats request are
  either bot events or non-bot events */
  private boolean allStatsValidation(List<AllStatsRequest> allStatsRequests)
      throws InvalidInputException {
    int statsCount = 0;
    int botClicksCount = 0;
    for (AllStatsRequest event : allStatsRequests) {
      if (event.getConversionType().equals(AllStatsEvent.BOT_CLICK)) {
        botClicksCount = botClicksCount + event.getCount();
      } else {
        statsCount = statsCount + event.getCount();
      }
      checkInvalidationConditions(statsCount, botClicksCount);
    }
    return botClicksCount > 0;
  }

  /*This method is used to hit jometer urls.*/
  private void triggerJoMeterConversionUrl(Session session, String url, int totalEvents)
      throws UnexpectedResponseException, ApiRequestException {
    logger.info("URL : " + url);
    for (int countItr = 0; countItr < totalEvents; countItr++) {
      RestResponse response = executor.get(session, url);
      if (response.getResponseCode() != 200) {
        String errorMessage =
            "Request not successfully processed, " + ", status code " + response.getResponseCode();

        logger.error(errorMessage);
        throw new UnexpectedResponseException(errorMessage);
      }
    }
  }

  /**
   * This method is used to generate all stats events.
   *
   * @param session Session
   * @param conf Conf
   * @param trackingService Tracking Service
   * @param databaseService Database Service
   * @param allStatsRequests allStatsEvents
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException On unexpected Response
   * @throws InvalidInputException invalid input provided
   */
  public void run(
      Session session,
      Config conf,
      TrackingService trackingService,
      DatabaseService databaseService,
      List<AllStatsRequest> allStatsRequests)
      throws ApiRequestException, UnexpectedResponseException, InvalidInputException {

    boolean areBotEvents = allStatsValidation(allStatsRequests);

    if (areBotEvents) {
      // add to spam list
      trackingService.addElementToSet(ipAddress);
    } else {
      // remove from spam list
      trackingService.removeElementFromSet(ipAddress);
      trackingService.removeKey("1day-" + ipAddress);
      trackingService.removeKey("7days-" + ipAddress);
      trackingService.removeKey("30days-" + ipAddress);
    }

    Map<String, String> pixelHashIds = new HashMap<>();
    for (AllStatsRequest event : allStatsRequests) {

      String clientId = event.getClientId();

      if (!pixelHashIds.containsKey(clientId)) {
        pixelHashIds.put(clientId, databaseService.fetchPixelHashId(clientId));
      }

      String url =
          conf.getString("JoMeterJSPixelUrl")
              + "c="
              + pixelHashIds.get(clientId)
              + "&a="
              + event.getConversionType().getValue()
              + "&r=";

      triggerJoMeterConversionUrl(session, url, event.getCount());
    }
  }
}
