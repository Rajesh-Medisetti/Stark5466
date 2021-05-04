package com.joveo.eqrtestsdk.models;

public class ClientStats extends Stats {
  private String timezone;
  private double proratedBudget;
  private String country;

  public String getTimezone() {
    return timezone;
  }

  public double getProratedBudget() {
    return proratedBudget;
  }

  public String getCountry() {
    return country;
  }
}
