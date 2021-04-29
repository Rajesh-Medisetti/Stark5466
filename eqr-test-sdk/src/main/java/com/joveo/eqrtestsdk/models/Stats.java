package com.joveo.eqrtestsdk.models;

public class Stats {
  private String id;
  private String name;
  private long startDateMillis;
  private long endDateMillis;
  private String startDate;
  private String endDate;
  private int jobCount;
  private int sponsoredJobCount;
  private CapDto budgetCap;
  private String status;
  private ResolvedStatus resolvedStatus;
  private int clicks;
  private int latentClicks;
  private int applyStarts;
  private int avgApplyDuration;
  private int applies;
  private int botClicks;
  private String spent;
  private double projectedSpent;
  private double monthlyBudget;
  private String cpc;
  private String cpa;
  private String cta;
  private int hires;
  private double cph;
  private double ath;
  private int allClicks;
  private int allBotClicks;
  private int allApplies;
  private String allCta;
  private int allApplyStarts;
  private int allHires;
  private double allCph;
  private double allAth;
  private int customConv1;
  private int customConv2;
  private int customConv3;
  private PacingStatus pacingStatus;
  private String profit;
  private String netSpent;
  private String averageMargin;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public long getStartDateMillis() {
    return startDateMillis;
  }

  public long getEndDateMillis() {
    return endDateMillis;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public int getJobCount() {
    return jobCount;
  }

  public int getSponsoredJobCount() {
    return sponsoredJobCount;
  }

  public CapDto getBudgetCap() {
    return budgetCap;
  }

  public String getStatus() {
    return status;
  }

  public ResolvedStatus getResolvedStatus() {
    return resolvedStatus;
  }

  public int getClicks() {
    return clicks;
  }

  public int getLatentClicks() {
    return latentClicks;
  }

  public int getApplyStarts() {
    return applyStarts;
  }

  public int getAvgApplyDuration() {
    return avgApplyDuration;
  }

  public int getApplies() {
    return applies;
  }

  public int getBotClicks() {
    return botClicks;
  }

  public String getSpent() {
    return spent;
  }

  public double getProjectedSpent() {
    return projectedSpent;
  }

  public double getMonthlyBudget() {
    return monthlyBudget;
  }

  public String getCpc() {
    return cpc;
  }

  public String getCpa() {
    return cpa;
  }

  public String getCta() {
    return cta;
  }

  public int getHires() {
    return hires;
  }

  public double getCph() {
    return cph;
  }

  public double getAth() {
    return ath;
  }

  public int getAllClicks() {
    return allClicks;
  }

  public int getAllBotClicks() {
    return allBotClicks;
  }

  public int getAllApplies() {
    return allApplies;
  }

  public String getAllCta() {
    return allCta;
  }

  public int getAllApplyStarts() {
    return allApplyStarts;
  }

  public int getAllHires() {
    return allHires;
  }

  public double getAllCph() {
    return allCph;
  }

  public double getAllAth() {
    return allAth;
  }

  public int getCustomConv1() {
    return customConv1;
  }

  public int getCustomConv2() {
    return customConv2;
  }

  public int getCustomConv3() {
    return customConv3;
  }

  public PacingStatus getPacingStatus() {
    return pacingStatus;
  }

  public String getProfit() {
    return profit;
  }

  public String getNetSpent() {
    return netSpent;
  }

  public String getAverageMargin() {
    return averageMargin;
  }

  public static class ResolvedStatus {
    private String description;
    private String status;

    public String getDescription() {
      return description;
    }

    public String getStatus() {
      return status;
    }
  }

  public static class PacingStatus {
    private boolean underPacing;
    private double value;

    public boolean isUnderPacing() {
      return underPacing;
    }

    public double getValue() {
      return value;
    }
  }
}
