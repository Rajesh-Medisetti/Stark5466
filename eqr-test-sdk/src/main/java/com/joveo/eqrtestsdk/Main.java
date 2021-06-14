package com.joveo.eqrtestsdk;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.mojo.OutboundFeed;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import com.joveo.eqrtestsdk.models.OutboundFeedDto;
import com.joveo.eqrtestsdk.models.OutboundJob;
import com.joveo.eqrtestsdk.models.clickmeterevents.StatsRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
  /**
   * .
   *
   * @param args args
   * @throws MojoException MojoException
   */
  public static void main(String[] args) throws MojoException {
    Driver driver = Driver.start("reliability@joveo.com", "joveo1520", JoveoEnvironment.Staging);
    driver = driver.addTrackingAndDatabaseService();
    //21784 - 24 - 16 - 21784
    Client client = driver.getExistingClient("7f79119e-0ae8-43f0-a6df-c80a981a75af");
    OutboundFeed outboundFeed = client.getOutboundFeed("Naukri", true);
    OutboundFeedDto job = outboundFeed.getFeed();
    OutboundFeed outboundFeed1 = client.getOutboundFeed("Indeed", false);
    OutboundFeedDto job1 = outboundFeed1.getFeed();
    System.out.println("hey rajesh");
    Map<LocalDateTime, Integer> clicks = new HashMap<>();
    clicks.put(LocalDateTime.now(), 1);
    Map<LocalDateTime, Integer> botClicks = new HashMap<>();
    botClicks.put(LocalDateTime.now(), 1);
    Map<LocalDateTime, Integer> applyStarts = new HashMap<>();
    applyStarts.put(LocalDateTime.now(), 1);
    Map<LocalDateTime, Integer> applyFinishes = new HashMap<>();
    applyFinishes.put(LocalDateTime.now(), 1);
    List<StatsRequest> statsRequests = new ArrayList<>();
    statsRequests.add(outboundFeed.createStatsRequest(clicks, botClicks, applyStarts, applyFinishes));
    statsRequests.add(outboundFeed1.createStatsRequest(clicks, botClicks, applyStarts, applyFinishes));
    System.out.println("hey rajesh");
    //driver.generateSponsoredStats(statsRequests);
  }
}
