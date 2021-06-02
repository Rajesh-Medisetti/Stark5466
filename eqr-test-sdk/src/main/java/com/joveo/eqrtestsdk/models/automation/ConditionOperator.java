package com.joveo.eqrtestsdk.models.automation;

public enum ConditionOperator {
  equals("equals"),
  not_equals("not equals"),
  greater_than("greater than"),
  less_than("less than"),
  greater_than_equal_to("greater than equal to"),
  less_than_equal_to("less than equal to");

  private String value;

  public String getValue() {
    return this.value;
  }

  ConditionOperator(String value) {
    this.value = value;
  }
}
