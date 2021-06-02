package com.joveo.eqrtestsdk.models.automation;

public enum ConditionParameterUnit {
  NumberOf("#"),
  Dollar("$"),
  Percentage("%");

  public String value;

  private ConditionParameterUnit(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
