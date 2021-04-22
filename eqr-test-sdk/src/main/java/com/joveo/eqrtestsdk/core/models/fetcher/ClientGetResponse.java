package com.joveo.eqrtestsdk.core.models.fetcher;

import java.util.List;

public class ClientGetResponse {

  private List<Feeds> feeds;
  private String startDate;

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public List<Feeds> getFeeds() {
    return feeds;
  }

  public void setFeeds(List<Feeds> feeds) {
    this.feeds = feeds;
  }

  public static class Feeds {

    public String xmlFeedUrl;
    public String id;
    public Boolean deleted;
  }
}
