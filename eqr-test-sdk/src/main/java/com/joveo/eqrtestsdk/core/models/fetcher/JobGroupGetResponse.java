package com.joveo.eqrtestsdk.core.models.fetcher;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.joveo.eqrtestsdk.models.Freq;
import java.util.List;

public class JobGroupGetResponse {

  private List<Placement> placements;
  private String startDate;
  private String campaignId;
  private Placement.CapDto budgetCap;

  public Placement.CapDto getBudgetCap() {
    return budgetCap;
  }

  public void setBudgetCap(Placement.CapDto budgetCap) {
    this.budgetCap = budgetCap;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  private TradingGoals tradingGoals;

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public List<Placement> getPlacements() {
    return placements;
  }

  public TradingGoals getTradingGoals() {
    return tradingGoals;
  }

  public void setTradingGoals(TradingGoals tradingGoals) {
    this.tradingGoals = tradingGoals;
  }

  public void setPlacements(List<Placement> placements) {
    this.placements = placements;
  }

  public static class Placement {

    public String publisher;
    public Boolean isActive;
    public Double bid;
    public CapDto budget;

    public String getPublisher() {
      return publisher;
    }

    @JsonSetter("pValue")
    public void setPublisher(String publisher) {
      this.publisher = publisher;
    }

    public Double getBid() {
      return bid;
    }

    public void setBid(Double bid) {
      this.bid = bid;
    }

    public void setActive(Boolean active) {
      isActive = active;
    }

    public static class CapDto {

      public Freq freq;
      public Boolean pacing;
      public Double threshold;
      public Double value;
      public Boolean locked;

      public void setPacing(Boolean pacing) {
        this.pacing = pacing;
      }

      public Boolean getLocked() {
        return locked;
      }

      public void setLocked(Boolean locked) {
        this.locked = locked;
      }

      public void setValue(Double value) {
        this.value = value;
      }

      public Double getValue() {
        return this.value;
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

    public Boolean getActive() {
      return isActive;
    }

    public CapDto getBudget() {
      return budget;
    }

    public void setBudget(CapDto budget) {
      this.budget = budget;
    }
  }

  public static class TradingGoals {

    public List<IoDetails> ioDetails;

    public List<IoDetails.PerformanceTargets> performanceTargets;

    public List<IoDetails> getIoDetails() {
      return ioDetails;
    }

    public void setIoDetails(List<IoDetails> ioDetails) {
      this.ioDetails = ioDetails;
    }

    public List<IoDetails.PerformanceTargets> getPerformanceTargets() {
      return performanceTargets;
    }

    public void setPerformanceTargets(List<IoDetails.PerformanceTargets> performanceTargets) {
      this.performanceTargets = performanceTargets;
    }

    public static class IoDetails {

      public String number;
      public Integer value;
      public String startDate;
      public String endDate;

      public static class PerformanceTargets {
        public String type;
        public Double value;
      }
    }
  }
}
