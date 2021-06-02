package com.joveo.eqrtestsdk.models.automation;

public enum BidStrategyActionParameter {
  cpa_bid("cpa bid"),
  cpc_bid("cpc bid");

  private String value;

  public String getValue() {
    return this.value;
  }

  BidStrategyActionParameter(String value) {
    this.value = value;
  }
}
