package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Conditions {

  @NotNull(message = "condition's value can't be null in Automation")
  Double value;

  @NotNull(message = "condition's parameter can't be null in Automation")
  Parameter parameter;

  @NotNull(message = "condition's operator can't be null in Automation")
  @JsonIgnore
  ConditionOperator conditionOperator;

  @NotNull(message = "condition's operator can't be null in Automation")
  String condition;

  @NotNull(message = "condition's duration can't be null in Automation")
  ConditionDuration conditionDuration;

  @NotNull(message = "condition's unit can't be null in Automation")
  String unit;

  /** . Condition constructor for Automation */
  public Conditions(
      Double value,
      Parameter parameter,
      ConditionOperator condition,
      ConditionDuration conditionDuration,
      ConditionParameterUnit unit) {
    this.value = value;
    this.parameter = parameter;
    this.conditionOperator = condition;
    this.condition = condition.getValue();
    this.conditionDuration = conditionDuration;
    this.unit = unit.value;
  }

  /** . Validation function for checking compatibility of parameter with unit */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  @AssertTrue(
      message = "conditions in Automation is not valid: parameter is not compatible with unit")
  @JsonIgnore
  public boolean isConditionUnitValid() {

    if ((parameter.getValue().contains("Clicks") || parameter.getValue().contains("Applies"))) {
      return unit.equals("#");
    }

    if (parameter.equals(Parameter.spent)) {
      return unit.equals("$") || unit.equals("%");
    }

    if (parameter.equals(Parameter.cpa) || parameter.equals(Parameter.cpc)) {
      return unit.equals("$");
    }

    return unit.equals("%");
  }
}
