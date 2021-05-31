package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.BidStrategy;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.Performance;
import javax.validation.constraints.NotNull;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Actions {

  Action action;

  String parameter;

  @NotNull(message = "action value can't be null in automation")
  Double value;

  @JsonIgnore
  @NotNull(
      groups = {BidStrategy.class},
      message = "Action is Wrong," + " your actionType is bidStrategy use setBidStrategyAction")
  BidStrategyActionParameter bidAction;

  @JsonIgnore
  @NotNull(
      groups = {Performance.class},
      message = "Action is Wrong, " + "your actionType is performance use setPerformanceAction")
  PerformanceActionParameter actionParameter;

  String unit;

  /** . Constructor for BidStrategy Automation Action */
  public Actions(
      Action action, BidStrategyActionParameter parameter, Double value, ActionUnit unit) {
    this.action = action;
    this.parameter = parameter.getValue();
    this.bidAction = parameter;
    this.value = value;
    this.unit = unit.getValue();
  }

  /** . Constructor for Performance Automation Action */
  public Actions(
      Action action, PerformanceActionParameter parameter, Double value, ActionUnit unit) {
    this.action = action;
    this.parameter = parameter.toString();
    this.actionParameter = parameter;
    this.value = value;
    this.unit = unit.getValue();
  }
}
