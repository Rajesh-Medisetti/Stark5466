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
  private BudgetCap budgetCap;
  private String status;
  private String timezone;
  private ResolvedStatus resolvedStatus;
  private int clicks;
  private int latentClicks;
  private int applyStarts;
  private int avgApplyDuration;
  private int applies;
  private int botClicks;
  private String spent;
  private double monthlyBudget;
  private double proratedBudget;
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
  private String country;
  private PacingStatus pacingStatus;
  private String profit;
  private String netSpent;
  private String averageMargin;
  private double minBid;

  public Stats() {}

  public double getMinBid() {
    return minBid;
  }

  public void setMinBid(double minBid) {
    this.minBid = minBid;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getStartDateMillis() {
    return startDateMillis;
  }

  public void setStartDateMillis(long startDateMillis) {
    this.startDateMillis = startDateMillis;
  }

  public long getEndDateMillis() {
    return endDateMillis;
  }

  public void setEndDateMillis(long endDateMillis) {
    this.endDateMillis = endDateMillis;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public int getJobCount() {
    return jobCount;
  }

  public void setJobCount(int jobCount) {
    this.jobCount = jobCount;
  }

  public int getSponsoredJobCount() {
    return sponsoredJobCount;
  }

  public void setSponsoredJobCount(int sponsoredJobCount) {
    this.sponsoredJobCount = sponsoredJobCount;
  }

  public BudgetCap getBudgetCap() {
    return budgetCap;
  }

  public void setBudgetCap(BudgetCap budgetCap) {
    this.budgetCap = budgetCap;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public ResolvedStatus getResolvedStatus() {
    return resolvedStatus;
  }

  public void setResolvedStatus(ResolvedStatus resolvedStatus) {
    this.resolvedStatus = resolvedStatus;
  }

  public int getClicks() {
    return clicks;
  }

  public void setClicks(int clicks) {
    this.clicks = clicks;
  }

  public int getLatentClicks() {
    return latentClicks;
  }

  public void setLatentClicks(int latentClicks) {
    this.latentClicks = latentClicks;
  }

  public int getApplyStarts() {
    return applyStarts;
  }

  public void setApplyStarts(int applyStarts) {
    this.applyStarts = applyStarts;
  }

  public int getAvgApplyDuration() {
    return avgApplyDuration;
  }

  public void setAvgApplyDuration(int avgApplyDuration) {
    this.avgApplyDuration = avgApplyDuration;
  }

  public int getApplies() {
    return applies;
  }

  public void setApplies(int applies) {
    this.applies = applies;
  }

  public int getBotClicks() {
    return botClicks;
  }

  public void setBotClicks(int botClicks) {
    this.botClicks = botClicks;
  }

  public String getSpent() {
    return spent;
  }

  public void setSpent(String spent) {
    this.spent = spent;
  }

  public double getMonthlyBudget() {
    return monthlyBudget;
  }

  public void setMonthlyBudget(double monthlyBudget) {
    this.monthlyBudget = monthlyBudget;
  }

  public double getProratedBudget() {
    return proratedBudget;
  }

  public void setProratedBudget(double proratedBudget) {
    this.proratedBudget = proratedBudget;
  }

  public String getCpc() {
    return cpc;
  }

  public void setCpc(String cpc) {
    this.cpc = cpc;
  }

  public String getCpa() {
    return cpa;
  }

  public void setCpa(String cpa) {
    this.cpa = cpa;
  }

  public String getCta() {
    return cta;
  }

  public void setCta(String cta) {
    this.cta = cta;
  }

  public int getHires() {
    return hires;
  }

  public void setHires(int hires) {
    this.hires = hires;
  }

  public double getCph() {
    return cph;
  }

  public void setCph(double cph) {
    this.cph = cph;
  }

  public double getAth() {
    return ath;
  }

  public void setAth(double ath) {
    this.ath = ath;
  }

  public int getAllClicks() {
    return allClicks;
  }

  public void setAllClicks(int allClicks) {
    this.allClicks = allClicks;
  }

  public int getAllBotClicks() {
    return allBotClicks;
  }

  public void setAllBotClicks(int allBotClicks) {
    this.allBotClicks = allBotClicks;
  }

  public int getAllApplies() {
    return allApplies;
  }

  public void setAllApplies(int allApplies) {
    this.allApplies = allApplies;
  }

  public String getAllCta() {
    return allCta;
  }

  public void setAllCta(String allCta) {
    this.allCta = allCta;
  }

  public int getAllApplyStarts() {
    return allApplyStarts;
  }

  public void setAllApplyStarts(int allApplyStarts) {
    this.allApplyStarts = allApplyStarts;
  }

  public int getAllHires() {
    return allHires;
  }

  public void setAllHires(int allHires) {
    this.allHires = allHires;
  }

  public double getAllCph() {
    return allCph;
  }

  public void setAllCph(double allCph) {
    this.allCph = allCph;
  }

  public double getAllAth() {
    return allAth;
  }

  public void setAllAth(double allAth) {
    this.allAth = allAth;
  }

  public int getCustomConv1() {
    return customConv1;
  }

  public void setCustomConv1(int customConv1) {
    this.customConv1 = customConv1;
  }

  public int getCustomConv2() {
    return customConv2;
  }

  public void setCustomConv2(int customConv2) {
    this.customConv2 = customConv2;
  }

  public int getCustomConv3() {
    return customConv3;
  }

  public void setCustomConv3(int customConv3) {
    this.customConv3 = customConv3;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public PacingStatus getPacingStatus() {
    return pacingStatus;
  }

  public void setPacingStatus(PacingStatus pacingStatus) {
    this.pacingStatus = pacingStatus;
  }

  public String getProfit() {
    return profit;
  }

  public void setProfit(String profit) {
    this.profit = profit;
  }

  public String getNetSpent() {
    return netSpent;
  }

  public void setNetSpent(String netSpent) {
    this.netSpent = netSpent;
  }

  public String getAverageMargin() {
    return averageMargin;
  }

  public void setAverageMargin(String averageMargin) {
    this.averageMargin = averageMargin;
  }

  public static class BudgetCap {
    private double value;
    private String freq;
    private boolean pacing;
    private double threshold;
    private boolean locked;

    public double getValue() {
      return value;
    }

    public void setValue(double value) {
      this.value = value;
    }

    public String getFreq() {
      return freq;
    }

    public void setFreq(String freq) {
      this.freq = freq;
    }

    public boolean isPacing() {
      return pacing;
    }

    public void setPacing(boolean pacing) {
      this.pacing = pacing;
    }

    public double getThreshold() {
      return threshold;
    }

    public void setThreshold(double threshold) {
      this.threshold = threshold;
    }

    public boolean isLocked() {
      return locked;
    }

    public void setLocked(boolean locked) {
      this.locked = locked;
    }
  }

  public static class ResolvedStatus {
    private String description;
    private String status;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

  class PacingStatus {
    private boolean underPacing;
    private double value;

    public boolean isUnderPacing() {
      return underPacing;
    }

    public void setUnderPacing(boolean underPacing) {
      this.underPacing = underPacing;
    }

    public double getValue() {
      return value;
    }

    public void setValue(double value) {
      this.value = value;
    }
  }
}
