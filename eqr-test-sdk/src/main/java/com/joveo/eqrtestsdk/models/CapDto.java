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

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Boolean locked;

  public Boolean getLocked() {
    return locked;
  }

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

  public CapDto() {}

  /** Setting the budget for Campaign. */
  public CapDto(Double value) {
    this.value = value;
    pacing = false;
    freq = Freq.Monthly;
  }

  /** Setting the Budget . */
  public CapDto(Boolean pacing, Freq freq, Double threshold, Double value) {
    this.pacing = pacing;
    this.freq = freq;
    this.threshold = threshold;
    this.value = value;
  }

  /**
   * Setting the Budget with locking option.
   *
   * @param pacing pacing
   * @param freq freq
   * @param threshold threshold
   * @param value value
   * @param locked locked
   */
  public CapDto(Boolean pacing, Freq freq, Double threshold, Double value, Boolean locked) {
    this(pacing, freq, threshold, value);
    this.locked = locked;
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
