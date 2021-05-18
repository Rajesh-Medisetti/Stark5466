package com.joveo.eqrtestsdk.models.clickmeterevents;

import com.joveo.eqrtestsdk.core.mojo.OutboundFeed;
import com.joveo.eqrtestsdk.models.OutboundJob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsRequest {
  private String clientId;

  private Map<LocalDateTime, Integer> sponsoredClicks;
  private Map<LocalDateTime, Integer> sponsoredBotClicks;
  private Map<LocalDateTime, Integer> sponsoredApplyStarts;
  private Map<LocalDateTime, Integer> sponsoredApplyFinishes;

  private OutboundFeed outboundFeed;
  private List<String> refNumbers;

  public StatsRequest() {}

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public Map<LocalDateTime, Integer> getSponsoredClicks() {
    return sponsoredClicks;
  }

  public void setSponsoredClicks(Map<LocalDateTime, Integer> sponsoredClicks) {
    this.sponsoredClicks = sponsoredClicks;
  }

  public Map<LocalDateTime, Integer> getSponsoredBotClicks() {
    return sponsoredBotClicks;
  }

  public void setSponsoredBotClicks(Map<LocalDateTime, Integer> sponsoredBotClicks) {
    this.sponsoredBotClicks = sponsoredBotClicks;
  }

  public Map<LocalDateTime, Integer> getSponsoredApplyStarts() {
    return sponsoredApplyStarts;
  }

  public void setSponsoredApplyStarts(Map<LocalDateTime, Integer> sponsoredApplyStarts) {
    this.sponsoredApplyStarts = sponsoredApplyStarts;
  }

  public Map<LocalDateTime, Integer> getSponsoredApplyFinishes() {
    return sponsoredApplyFinishes;
  }

  public void setSponsoredApplyFinishes(Map<LocalDateTime, Integer> sponsoredApplyFinishes) {
    this.sponsoredApplyFinishes = sponsoredApplyFinishes;
  }

  public OutboundFeed getOutboundFeed() {
    return outboundFeed;
  }

  public void setOutboundFeed(OutboundFeed outboundFeed) {
    this.outboundFeed = outboundFeed;
  }

  public List<String> getRefNumbers() {
    return refNumbers;
  }

  public void setRefNumbers(List<String> refNumbers) {
    this.refNumbers = refNumbers;
  }

  /**
   * This method provides a list of jobs filtered based on given list of reference numbers.
   *
   * @return List of jobs
   */
  public List<OutboundJob> getJobsInStats() {
    List<OutboundJob> alljobs = outboundFeed.getFeed().getJobs();
    if (refNumbers == null) {
      return alljobs;
    }
    List<OutboundJob> outboundJobs = new ArrayList<>();
    for (OutboundJob job : alljobs) {
      for (String refNo : refNumbers) {
        if (refNo.equals(job.referencenumber)) {
          outboundJobs.add(job);
          break;
        }
      }
    }
    return outboundJobs;
  }
}
