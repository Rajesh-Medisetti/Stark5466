package com.joveo.eqrtestsdk.models;

public enum TimeZone {
  UTC_minus_12_00("66c9"),
  UTC_minus_11_00("7afb"),
  UTC_minus_10_00("1bd0"),
  UTC_minus_09_00("366d"),
  UTC_minus_08_00("af8e"),
  UTC_minus_07_00("bad7"),
  UTC_minus_06_00("b0c1"),
  UTC_minus_05_00("5655"),
  UTC_minus_04_30("3d3e"),
  UTC_minus_04_00("8ec7"),
  UTC_minus_03_30("5328"),
  UTC_minus_03_00("ddd7"),
  UTC_minus_02_00("8607"),
  UTC_minus_01_00("b5d0"),
  UTC("71cf"),
  UTC_plus_01_00("2c15"),
  UTC_plus_02_00("6935"),
  UTC_plus_03_00("6a24"),
  UTC_plus_03_30("feb2"),
  UTC_plus_04_00("9f4d"),
  UTC_plus_04_30("e782"),
  UTC_plus_05_00("1f5d"),
  UTC_plus_05_30("c1d3"),
  UTC_plus_05_45("3eaf"),
  UTC_plus_06_00("8dbc"),
  UTC_plus_06_30("bd9b"),
  UTC_plus_07_00("0e2f"),
  UTC_plus_08_00("303f"),
  UTC_plus_09_00("bbda"),
  UTC_plus_09_30("bcd6"),
  UTC_plus_10_00("0404"),
  UTC_plus_11_00("5e4d"),
  UTC_plus_12_00("a41e"),
  UTC_plus_13_00("08a2");
  private String value;

  public String getValue() {
    return this.value;
  }

  TimeZone(String value) {
    this.value = value;
  }
}
