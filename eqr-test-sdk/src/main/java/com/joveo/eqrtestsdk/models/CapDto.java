package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joveo.eqrtestsdk.models.validationgroups.EditJobGroup;
import com.joveo.eqrtestsdk.models.validationgroups.JobGroupCap;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

public class CapDto {

  public Freq freq;
  public Boolean pacing;

  @Max(
      value = 100,
      message = "threshold can't be greater than 100 check caps",
      groups = JobGroupCap.class)
  @Min(value = 0, message = "threshold can't be less than 0 check caps", groups = JobGroupCap.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Double threshold;

  @Min(value = 0, message = "minimum budgetValue will be 0")
  @NotNull(
      message = "cap budgetValue can't be null",
      groups = {EditJobGroup.class, Default.class})
  public Double value;

  /** Setting the budget for Campaign. */
  public CapDto(Double value) {
    this.value = value;
    pacing = false;
    freq = Freq.Monthly;
  }

  /** Setting the Budget . */
  public CapDto(Boolean pacing, Freq frequency, Double threshold, Double value) {
    this.pacing = pacing;
    this.freq = frequency;
    this.threshold = threshold;
    this.value = value;
  }

  @AssertTrue(
      message = "pacing is invalid in one of your budgetCap, pacing is valid only for monthly",
      groups = {Default.class, EditJobGroup.class})
  boolean isValidPacing() {

    return pacing == false || freq == Freq.Monthly;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Double getValue() {
    return this.value;
  }

  public void setPacing(boolean pacing) {
    this.pacing = pacing;
  }

  public boolean getPacing() {
    return this.pacing;
  }

  public void setFreq(Freq freq) {
    this.freq = freq;
  }

  public Freq getFreq() {
    return this.freq;
  }

  public Double getThreshold() {
    return threshold;
  }

  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }
}
