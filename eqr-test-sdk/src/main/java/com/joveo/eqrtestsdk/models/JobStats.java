package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class JobStats {
  private int allApplies;
  private int allBotClicks;
  private String city;
  private boolean isCpcBidOverridden;
  private String name;
  private boolean inferredStatus;
  private boolean isMarkdownOverridden;
  private String endDate;
  private String state;
  private String netSpent;
  private String profit;
  private String campaign;
  private int applies;
  private double allCph;
  private double tradingGoalsCpa;
  private double ath;
  private boolean expandable;
  private String cpc;
  private String company;
  private int customConv1;
  private String id;
  private boolean isCpaBidOverridden;
  private double allAth;
  private int allHires;
  private String status;
  private int applyStarts;
  private String cta;
  private int clicks;
  private int avgApplyDuration;
  private String category;
  private String markedUpSpent;
  private String refNumber;
  private int hires;
  private int allClicks;
  private String averageMargin;
  private int allApplyStarts;
  private double cph;
  private double tradingGoalsCpc;
  private String startDate;
  private String jobGroup;
  private int allViews;
  private int botClicks;
  private int customConv2;
  private String cpa;
  private String allCta;
  private String spent;
  private boolean liveStatus;
  private int latentClicks;
  private int customConv3;

  public int getAllApplies() {
    return allApplies;
  }

  public void setAllApplies(int allApplies) {
    this.allApplies = allApplies;
  }

  public int getAllBotClicks() {
    return allBotClicks;
  }

  public void setAllBotClicks(int allBotClicks) {
    this.allBotClicks = allBotClicks;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @JsonGetter("isCPCBidOverridden")
  public boolean isCpcBidOverridden() {
    return isCpcBidOverridden;
  }

  @JsonSetter("isCpcBidOverridden")
  public void setCpcBidOverridden(boolean cpcBidOverridden) {
    isCpcBidOverridden = cpcBidOverridden;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isInferredStatus() {
    return inferredStatus;
  }

  public void setInferredStatus(boolean inferredStatus) {
    this.inferredStatus = inferredStatus;
  }

  public boolean isMarkdownOverridden() {
    return isMarkdownOverridden;
  }

  public void setMarkdownOverridden(boolean markdownOverridden) {
    isMarkdownOverridden = markdownOverridden;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getNetSpent() {
    return netSpent;
  }

  public void setNetSpent(String netSpent) {
    this.netSpent = netSpent;
  }

  public String getProfit() {
    return profit;
  }

  public void setProfit(String profit) {
    this.profit = profit;
  }

  public String getCampaign() {
    return campaign;
  }

  public void setCampaign(String campaign) {
    this.campaign = campaign;
  }

  public int getApplies() {
    return applies;
  }

  public void setApplies(int applies) {
    this.applies = applies;
  }

  public double getAllCph() {
    return allCph;
  }

  public void setAllCph(double allCph) {
    this.allCph = allCph;
  }

  public double getTradingGoalsCpa() {
    return tradingGoalsCpa;
  }

  public void setTradingGoalsCpa(double tradingGoalsCpa) {
    this.tradingGoalsCpa = tradingGoalsCpa;
  }

  public double getAth() {
    return ath;
  }

  public void setAth(double ath) {
    this.ath = ath;
  }

  public boolean isExpandable() {
    return expandable;
  }

  public void setExpandable(boolean expandable) {
    this.expandable = expandable;
  }

  public String getCpc() {
    return cpc;
  }

  public void setCpc(String cpc) {
    this.cpc = cpc;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public int getCustomConv1() {
    return customConv1;
  }

  public void setCustomConv1(int customConv1) {
    this.customConv1 = customConv1;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonGetter("isCPABidOverridden")
  public boolean isCpaBidOverridden() {
    return isCpaBidOverridden;
  }

  @JsonSetter("isCpaBidOverridden")
  public void setCpaBidOverridden(boolean cpaBidOverridden) {
    isCpaBidOverridden = cpaBidOverridden;
  }

  public double getAllAth() {
    return allAth;
  }

  public void setAllAth(double allAth) {
    this.allAth = allAth;
  }

  public int getAllHires() {
    return allHires;
  }

  public void setAllHires(int allHires) {
    this.allHires = allHires;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getApplyStarts() {
    return applyStarts;
  }

  public void setApplyStarts(int applyStarts) {
    this.applyStarts = applyStarts;
  }

  public String getCta() {
    return cta;
  }

  public void setCta(String cta) {
    this.cta = cta;
  }

  public int getClicks() {
    return clicks;
  }

  public void setClicks(int clicks) {
    this.clicks = clicks;
  }

  public int getAvgApplyDuration() {
    return avgApplyDuration;
  }

  public void setAvgApplyDuration(int avgApplyDuration) {
    this.avgApplyDuration = avgApplyDuration;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getMarkedUpSpent() {
    return markedUpSpent;
  }

  public void setMarkedUpSpent(String markedUpSpent) {
    this.markedUpSpent = markedUpSpent;
  }

  public String getRefNumber() {
    return refNumber;
  }

  public void setRefNumber(String refNumber) {
    this.refNumber = refNumber;
  }

  public int getHires() {
    return hires;
  }

  public void setHires(int hires) {
    this.hires = hires;
  }

  public int getAllClicks() {
    return allClicks;
  }

  public void setAllClicks(int allClicks) {
    this.allClicks = allClicks;
  }

  public String getAverageMargin() {
    return averageMargin;
  }

  public void setAverageMargin(String averageMargin) {
    this.averageMargin = averageMargin;
  }

  public int getAllApplyStarts() {
    return allApplyStarts;
  }

  public void setAllApplyStarts(int allApplyStarts) {
    this.allApplyStarts = allApplyStarts;
  }

  public double getCph() {
    return cph;
  }

  public void setCph(double cph) {
    this.cph = cph;
  }

  public double getTradingGoalsCpc() {
    return tradingGoalsCpc;
  }

  public void setTradingGoalsCpc(double tradingGoalsCpc) {
    this.tradingGoalsCpc = tradingGoalsCpc;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getJobGroup() {
    return jobGroup;
  }

  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }

  public int getAllViews() {
    return allViews;
  }

  public void setAllViews(int allViews) {
    this.allViews = allViews;
  }

  public int getBotClicks() {
    return botClicks;
  }

  public void setBotClicks(int botClicks) {
    this.botClicks = botClicks;
  }

  public int getCustomConv2() {
    return customConv2;
  }

  public void setCustomConv2(int customConv2) {
    this.customConv2 = customConv2;
  }

  public String getCpa() {
    return cpa;
  }

  public void setCpa(String cpa) {
    this.cpa = cpa;
  }

  public String getAllCta() {
    return allCta;
  }

  public void setAllCta(String allCta) {
    this.allCta = allCta;
  }

  public String getSpent() {
    return spent;
  }

  public void setSpent(String spent) {
    this.spent = spent;
  }

  public boolean isLiveStatus() {
    return liveStatus;
  }

  public void setLiveStatus(boolean liveStatus) {
    this.liveStatus = liveStatus;
  }

  public int getLatentClicks() {
    return latentClicks;
  }

  public void setLatentClicks(int latentClicks) {
    this.latentClicks = latentClicks;
  }

  public int getCustomConv3() {
    return customConv3;
  }

  public void setCustomConv3(int customConv3) {
    this.customConv3 = customConv3;
  }
}
