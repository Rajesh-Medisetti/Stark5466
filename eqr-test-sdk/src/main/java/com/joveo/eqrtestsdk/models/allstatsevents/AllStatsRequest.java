package com.joveo.eqrtestsdk.models.allstatsevents;

public class AllStatsRequest {
  private AllStatsEvent conversionType;
  private Integer count;
  private String clientId;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public AllStatsEvent getConversionType() {
    return conversionType;
  }

  public void setConversionType(AllStatsEvent conversion) {
    this.conversionType = conversion;
  }
}
