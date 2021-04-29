package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joveo.eqrtestsdk.models.JobGroupDto.JobGroupParams.TradingGoals;
import java.util.List;

public class JobGroupStats extends Stats {

  private CapDto appliesCap;
  private CapDto clicksCap;
  private CapDto jobBudgetCap;
  private CapDto jobAppliesCap;
  private CapDto jobClicksCap;
  private int priority;
  private TradingGoals tradingGoals;
  private String cpcBid;
  private String cpaBid;
  private int tradingGoalsCpc;
  private int tradingGoalsCpa;
  private List<Placement> placements;
  private List<Integer> daysToSchedule;
  private boolean isMarkdownOverridden;

  @JsonProperty("isPPC")
  private boolean isPpc;

  public JobGroupStats() {}

  public CapDto getAppliesCap() {
    return appliesCap;
  }

  public CapDto getClicksCap() {
    return clicksCap;
  }

  public CapDto getJobBudgetCap() {
    return jobBudgetCap;
  }

  public CapDto getJobAppliesCap() {
    return jobAppliesCap;
  }

  public CapDto getJobClicksCap() {
    return jobClicksCap;
  }

  public int getPriority() {
    return priority;
  }

  public TradingGoals getTradingGoals() {
    return tradingGoals;
  }

  public String getCpcBid() {
    return cpcBid;
  }

  public String getCpaBid() {
    return cpaBid;
  }

  public int getTradingGoalsCpc() {
    return tradingGoalsCpc;
  }

  public int getTradingGoalsCpa() {
    return tradingGoalsCpa;
  }

  public List<Placement> getPlacements() {
    return placements;
  }

  public List<Integer> getDaysToSchedule() {
    return daysToSchedule;
  }

  public boolean isMarkdownOverridden() {
    return isMarkdownOverridden;
  }

  public boolean isPpc() {
    return isPpc;
  }

  public static class Placement {
    @JsonProperty("pValue")
    private String publisher;

    private double bid;
    private CapDto budget;
    private boolean isActive;
    private boolean isRecommended;
    private String currency;

    public String getpValue() {
      return publisher;
    }

    public double getBid() {
      return bid;
    }

    public CapDto getBudget() {
      return budget;
    }

    public boolean isActive() {
      return isActive;
    }

    public boolean isRecommended() {
      return isRecommended;
    }

    public String getCurrency() {
      return currency;
    }
  }
}
