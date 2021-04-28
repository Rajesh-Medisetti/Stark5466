package com.joveo.eqrtestsdk.core.models.fetcher;

import com.fasterxml.jackson.annotation.JsonSetter;

public class PlacementGetResponse {

  private String publisher;
  private Double minBid;

  public String getPublisher() {
    return publisher;
  }

  @JsonSetter("pValue")
  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public Double getMinBid() {
    return minBid;
  }

  @JsonSetter("pMinBid")
  public void setMinBid(Double minBid) {
    this.minBid = minBid;
  }
}
