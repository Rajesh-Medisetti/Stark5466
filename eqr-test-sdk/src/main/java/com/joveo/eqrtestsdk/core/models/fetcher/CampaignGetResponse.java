package com.joveo.eqrtestsdk.core.models.fetcher;

import com.joveo.eqrtestsdk.models.Freq;

public class CampaignGetResponse {

  private String startDate;
  private String name;
  private CapDto budgetCap;

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CapDto getBudgetCap() {
    return budgetCap;
  }

  public void setBudgetCap(CapDto budgetCap) {
    this.budgetCap = budgetCap;
  }

  public static class CapDto {

    public Freq freq;
    public Boolean pacing;
    public Double threshold;
    public Double value;

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
}
