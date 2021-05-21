package com.joveo.eqrtestsdk.core.models;

import java.util.ArrayList;
import java.util.List;

public class SponsoredEvents {
  List<String> allSponsoredClickEvents;
  List<String> allSponsoredBotClickEvents;
  List<String> allSponsoredApplyStartEvents;
  List<String> allSponsoredApplyFinishEvents;

  /** Constructor for SponsoredEvents. */
  public SponsoredEvents() {
    allSponsoredClickEvents = new ArrayList<>();
    allSponsoredBotClickEvents = new ArrayList<>();
    allSponsoredApplyStartEvents = new ArrayList<>();
    allSponsoredApplyFinishEvents = new ArrayList<>();
  }

  public List<String> getAllSponsoredClickEvents() {
    return allSponsoredClickEvents;
  }

  public void setAllSponsoredClickEvents(List<String> allSponsoredClickEvents) {
    this.allSponsoredClickEvents.addAll(allSponsoredClickEvents);
  }

  public List<String> getAllSponsoredBotClickEvents() {
    return allSponsoredBotClickEvents;
  }

  public void setAllSponsoredBotClickEvents(List<String> allSponsoredBotClickEvents) {
    this.allSponsoredBotClickEvents.addAll(allSponsoredBotClickEvents);
  }

  public List<String> getAllSponsoredApplyStartEvents() {
    return allSponsoredApplyStartEvents;
  }

  public void setAllSponsoredApplyStartEvents(List<String> allSponsoredApplyStartEvents) {
    this.allSponsoredApplyStartEvents.addAll(allSponsoredApplyStartEvents);
  }

  public List<String> getAllSponsoredApplyFinishEvents() {
    return allSponsoredApplyFinishEvents;
  }

  public void setAllSponsoredApplyFinishEvents(List<String> allSponsoredApplyFinishEvents) {
    this.allSponsoredApplyFinishEvents.addAll(allSponsoredApplyFinishEvents);
  }
}
