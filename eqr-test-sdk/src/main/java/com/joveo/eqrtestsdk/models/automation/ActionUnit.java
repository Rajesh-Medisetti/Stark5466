package com.joveo.eqrtestsdk.models.automation;

public enum ActionUnit {
  Percentage("%"),
  Dollar("$");

  public String value;

  private ActionUnit(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
