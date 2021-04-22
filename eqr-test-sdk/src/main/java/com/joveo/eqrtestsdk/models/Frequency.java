package com.joveo.eqrtestsdk.models;

public enum Frequency {
  _1_Hours("1 hour"),
  _3_Hours("3 hours"),
  _6_Hours("6 hours"),
  _12_Hours("12 hours"),
  _24_Hours("24 hours");

  private String value;

  public String getValue() {
    return this.value;
  }

  private Frequency(String value) {
    this.value = value;
  }
}
