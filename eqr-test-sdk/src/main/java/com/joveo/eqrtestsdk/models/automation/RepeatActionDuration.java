package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.validation.constraints.NotNull;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RepeatActionDuration {

  String clause;

  @NotNull(message = "Repeat duration can't be null")
  Double value;

  String unit;

  /** . constructor for repeatAction in Automation */
  public RepeatActionDuration(String clause, Double value, String unit) {
    this.clause = clause;
    this.value = value;
    this.unit = unit;
  }
}
