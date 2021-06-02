package com.joveo.eqrtestsdk.models.automation;

public enum Parameter {
  clicks("Sponsored Clicks"),
  allClicks("Total Clicks"),
  applies("Sponsored Applies"),
  allApplies("Total Applies"),
  spent("Spend"),
  cpc("CPC"),
  cpa("CPA"),
  cta("CTA");

  private final String value;

  public String getValue() {
    return this.value;
  }

  Parameter(String value) {
    this.value = value;
  }
}
