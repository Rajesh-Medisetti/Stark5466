package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.validationgroups.EditJobGroup;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JobGroupDto {

  @Valid private JobGroupParams params;

  @NotEmpty(
      message = "jobGroupId can't be null/empty",
      groups = {EditJobGroup.class})
  @JsonIgnore
  private String jobGroupId;

  public void setJobGroupId(String id) {
    this.jobGroupId = id;
  }

  @JsonIgnore
  public String getJobGroupId() {
    return this.jobGroupId;
  }

  @JsonIgnore
  public String getCampaignId() {
    return this.params.campaignId;
  }

  @JsonIgnore
  public String getClientId() {
    return this.params.clientId;
  }

  public JobGroupDto() {
    params = new JobGroupParams();
  }

  public void setName(String name) {
    params.name = name;
  }

  /** setting ClientId. */
  public void setClientId(String clientId) {

    this.params.clientId = clientId;
    this.params.clientIds = new ArrayList<>();
    this.params.clientIds.add(clientId);
  }

  public void setCampaignId(String campaignId) {

    this.params.campaignId = campaignId;
  }

  public void setStartDate(LocalDate startDate) {
    params.startDate = startDate;
  }

  public void setEndDate(LocalDate endDate) {
    params.endDate = endDate;
  }

  public void setPriority(Integer priority) {
    params.priority = priority;
  }

  public void setCategory(String category) {
    params.category = category;
  }

  public void setCpcBid(Double cpcBid) {
    params.cpcBid = cpcBid;
  }

  public void setCpaBid(Double cpaBid) {
    params.cpaBid = cpaBid;
  }

  public void setSign(String sign) {
    params.sign = sign;
  }

  public void setBudgetCap(Double value) {
    this.setBudgetCap(false, Freq.Monthly, 80.0, value);
  }

  public void setBudgetCap(Boolean pacing, Freq frequency, Double threshold, Double value) {
    params.budgetCap = new CapDto(pacing, frequency, threshold, value);
  }

  public void setClickCap(Boolean pacing, Freq frequency, Double threshold, int value) {
    params.clicksCap = new CapDto(pacing, frequency, threshold, (double) value);
  }

  public void setApplyCap(Boolean pacing, Freq frequency, Double threshold, int value) {
    params.appliesCap = new CapDto(pacing, frequency, threshold, (double) value);
  }

  public void setJobBudgetCap(Boolean pacing, Freq frequency, Double threshold, int value) {
    params.jobBudgetCap = new CapDto(pacing, frequency, threshold, (double) value);
  }

  public void setJobClickCap(Boolean pacing, Freq frequency, Double threshold, int value) {
    params.jobClicksCap = new CapDto(pacing, frequency, threshold, (double) value);
  }

  public void setJobApplyCap(Boolean pacing, Freq frequency, Double threshold, int value) {
    params.jobAppliesCap = new CapDto(pacing, frequency, threshold, (double) value);
  }

  public void setJobFilter(GroupingJobFilter filter) {
    params.filters = filter;
  }

  /** adding IoDetails. */
  public void addIoDetail(String ioNumber, int value, LocalDate startDate, LocalDate endDate) {

    if (params.tradingGoals == null) {
      params.tradingGoals = new JobGroupParams.TradingGoals();
    }

    params.tradingGoals.addIoDetails(ioNumber, value, startDate, endDate);
  }

  /** Adding performance targets. */
  public void addPerformanceTargets(String type, Double value) {
    if (params.tradingGoals == null) {
      params.tradingGoals = new JobGroupParams.TradingGoals();
    }

    params.tradingGoals.addPerformanceTargets(type, value);
  }

  /** add placement with id. */
  public void addPlacement(String placementId) {

    if (params.placements == null) {
      params.placements = new ArrayList<>();
    }

    JobGroupParams.Placements placementValue = new JobGroupParams.Placements(placementId);
    params.placements.add(placementValue);
  }

  /** add placement with bid. */
  public void addPlacementWithBid(String placementId, Double bid) {

    if (params.placements == null) {
      params.placements = new ArrayList<>();
    }
    JobGroupParams.Placements placementValue = new JobGroupParams.Placements(placementId);
    placementValue.setBid(bid);
    params.placements.add(placementValue);
  }

  /** adding Placement with Budget. */
  public void addPlacementWithBudget(
      String placementId,
      Double budgetCap,
      Freq freq,
      Boolean pacing,
      Double threshold,
      Boolean locked) {

    this.addPlacementWithBudgetAndBid(
        placementId, null, budgetCap, freq, pacing, threshold, locked);
  }

  /** Adding placements with budget and Bid. */
  public void addPlacementWithBudgetAndBid(
      String placementId,
      Double bid,
      Double budgetCap,
      Freq freq,
      Boolean pacing,
      Double threshold,
      Boolean locked) {

    if (params.placements == null) {
      params.placements = new ArrayList<>();
    }

    JobGroupParams.Placements placementValue = new JobGroupParams.Placements(placementId);
    placementValue.setBid(bid);
    placementValue.setBudget(new CapDto(pacing, freq, threshold, budgetCap, locked));
    params.placements.add(placementValue);
  }

  /** removing publisher. */
  public void removePlacement(String publisher) {

    if (params.placements == null) {
      params.placements = new ArrayList<>();
    }

    JobGroupParams.Placements placementValue = new JobGroupParams.Placements(publisher, true);
    params.placements.add(placementValue);
  }

  /** set days to schedule. */
  public void setDaysToSchedule(List<Integer> daysToSchedule) {
    if (params.daysToSchedule == null) {
      params.daysToSchedule = new ArrayList<>();
    }

    params.daysToSchedule.addAll(daysToSchedule);
  }

  public void setDefaultValues() {
    this.params.setDefaultValues();
  }

  @JsonIgnore
  public GroupingJobFilter getFilter() {
    return params.filters;
  }

  @JsonIgnore
  public Boolean isValidJobFilter(GroupingJobFilter filter, int depth) {

    return this.params.isValidJobFilter(filter, depth);
  }

  @JsonIgnore
  public LocalDate getEndDate() {
    return params.endDate;
  }

  @JsonIgnore
  public List<JobGroupParams.Placements> getPlacements() {
    return this.params.placements;
  }

  /** setting Trading Goals. */
  public void setTradingGoals(JobGroupParams.TradingGoals tradingGoals) {

    if (this.params.tradingGoals == null) {
      this.params.tradingGoals = tradingGoals;
    }
  }

  /** setting Placements. */
  public void setPlacements(List<JobGroupParams.Placements> placements) {

    if (this.params.placements == null) {
      this.params.placements = placements;
    }
  }

  @JsonIgnore
  public List<JobGroupParams.TradingGoals.IoDetails> getIoDetails() {

    return this.params.tradingGoals.ioDetails;
  }

  /** setting IoDetails. */
  public void setIoDetails(ArrayList<JobGroupParams.TradingGoals.IoDetails> ioDetails) {

    if (this.params.tradingGoals.ioDetails == null) {
      this.params.tradingGoals.ioDetails = ioDetails;
    }
  }

  @JsonIgnore
  public JobGroupParams.TradingGoals getTradingGoals() {

    return this.params.tradingGoals;
  }

  @JsonIgnore
  public CapDto getBudget() {
    return params.budgetCap;
  }

  public static class JobGroupParams {

    @NotEmpty(message = "JobGroup name can't be null/empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String name;

    @NotEmpty(message = "campaignId can't be null/empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String campaignId;

    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "startDate can't be null")
    @FutureOrPresent(message = "startDate can't be past")
    @Null(
        message = "startDate is not Editable",
        groups = {EditJobGroup.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDate startDate;

    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "endDate can't be null")
    @FutureOrPresent(message = "endDate can't be past")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDate endDate;

    @Min(
        value = 1,
        message = "JobGroupPriority must bw >=1",
        groups = {EditJobGroup.class, Default.class})
    @Max(
        value = 10,
        message = "JobGroupPriority must bw <=10",
        groups = {EditJobGroup.class, Default.class})
    @NotNull(message = "set Priority")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer priority;

    public String category;

    @Min(
        value = 0,
        groups = {EditJobGroup.class, Default.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double cpcBid;

    @Min(
        value = 0,
        groups = {EditJobGroup.class, Default.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double cpaBid;

    @NotEmpty(
        message = "clientId can't be null/empty",
        groups = {EditJobGroup.class, Default.class})
    public String clientId;

    @Size(min = 1, max = 1, message = "list of clientIds can't be greater than 1")
    @NotNull(
        message = "list of clientId can't be null",
        groups = {EditJobGroup.class, Default.class})
    public List<String> clientIds;

    public String sign;

    @Valid
    @NotNull(message = "JobFilter can't be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public GroupingJobFilter filters;

    @NotNull(message = "budgetCap can't be null")
    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public CapDto budgetCap;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    public CapDto clicksCap;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    public CapDto appliesCap;

    public OverspendCap overspendCap;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    public CapDto jobBudgetCap;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    public CapDto jobClicksCap;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    public CapDto jobAppliesCap;

    @NotNull(message = "tradingGoals can't be null")
    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public TradingGoals tradingGoals;

    @NotNull(message = "placements can't be null")
    @Size(min = 1, message = "There must be at least 1 placement")
    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Placements> placements;

    public RecommendationAudit recommendationAudit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Integer> daysToSchedule;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("isPPC")
    public Boolean isPpc;

    /** startDate must be <= endDate for Campaign. */
    @AssertTrue(message = "startDate must be <= endDate for Campaign")
    @JsonIgnore
    public boolean isValidRange() {

      if (startDate == null || endDate == null) {
        return true;
      }

      return endDate.compareTo(startDate) >= 0;
    }

    /** checking budgetCap. */
    @AssertTrue(
        message = "budgetCap is Invalid, there is no lifetime option in budgetCap",
        groups = {EditJobGroup.class, Default.class})
    @JsonIgnore
    public boolean isValidBudgetCap() {
      return (budgetCap == null
          || budgetCap.freq == Freq.Daily
          || budgetCap.freq == Freq.Weekly
          || budgetCap.freq == Freq.Monthly);
    }

    @AssertTrue(
        message = "ClickCap is Invalid, there is no daily,lifetime option in ClickCap",
        groups = {EditJobGroup.class, Default.class})
    @JsonIgnore
    public boolean isValidClickCap() {
      return (clicksCap == null || clicksCap.freq == Freq.Weekly || clicksCap.freq == Freq.Monthly);
    }

    /** checking applyCap. */
    @AssertTrue(
        message = "ApplyCap is Invalid, there is no daily,lifetime option in ApplyCap",
        groups = {EditJobGroup.class, Default.class})
    @JsonIgnore
    public boolean isValidApplyCap() {
      return (appliesCap == null
          || appliesCap.freq == Freq.Weekly
          || appliesCap.freq == Freq.Monthly);
    }

    @AssertTrue(
        message = "jobBudgetCap is Invalid, there is no daily option in jobBudgetCap",
        groups = {EditJobGroup.class, Default.class})
    @JsonIgnore
    public boolean isValidJobBudgetCap() {
      return jobBudgetCap == null || jobBudgetCap.freq != Freq.Daily;
    }

    @AssertTrue(
        message = "jobClickCap is Invalid, there is no daily option in jobClickCap",
        groups = {EditJobGroup.class, Default.class})
    @JsonIgnore
    public boolean isValidJobClickCap() {
      return jobClicksCap == null || jobClicksCap.freq != Freq.Daily;
    }

    @AssertTrue(
        message = "jobApplyCap is Invalid, there is no daily option in jobApplyCap",
        groups = {EditJobGroup.class, Default.class})
    @JsonIgnore
    public boolean isValidJobApplyCap() {
      return jobAppliesCap == null || jobAppliesCap.freq != Freq.Daily;
    }

    /** checking jobFilter. */
    @JsonIgnore
    public Boolean isValidJobFilter(GroupingJobFilter groupingJobFilter, Integer depth) {
      if (depth > 3) {
        return false;
      }
      for (Filter filter : groupingJobFilter.getRules()) {
        if (filter.getClass().getSimpleName().equals("GroupingJobFilter")) {
          if (!isValidJobFilter((GroupingJobFilter) filter, depth + 1)) {
            return false;
          }
        }
      }
      return true;
    }

    /** checking if an individual placement's budget is more than budgetCap or not. */
    @AssertTrue(
        message = "one of placements budget is more than the budgetCap",
        groups = {Default.class, EditJobGroup.class})
    @JsonIgnore
    public boolean isValidPlacements() {

      if (isInvalidPlacementBudgetCap(placements, budgetCap)) {
        return true;
      }

      for (Placements placement : placements) {
        if (placement.budget != null && placement.budget.value > budgetCap.value) {
          return false;
        }
      }
      return true;
    }

    @JsonIgnore
    private boolean isInvalidPlacementBudgetCap(List<Placements> placements, CapDto budgetCap) {
      return placements == null || budgetCap == null || budgetCap.value == null;
    }

    /** checking if total Locked Budget is more than budgetCap or not. */
    @AssertTrue(
        message = "total locked budget of placements is greater than the budgetCap",
        groups = {Default.class, EditJobGroup.class})
    @JsonIgnore
    public boolean isValidLockedPlacements() {

      if (isInvalidPlacementBudgetCap(placements, budgetCap)) {
        return true;
      }

      Double totalLockedBudget = 0.0;

      for (Placements placement : placements) {
        if (placement.budget != null && placement.budget.locked) {
          totalLockedBudget += placement.budget.value;
        }
      }
      return totalLockedBudget <= budgetCap.value;
    }

    /** setting DefaultValues. */
    public void setDefaultValues() {

      if (priority == null) {
        this.priority = 1;
      }
      this.category = "";
      this.sign = "$";
      this.overspendCap = new OverspendCap();
      this.recommendationAudit = new RecommendationAudit();

      if (this.tradingGoals == null) {
        this.tradingGoals = new TradingGoals();

        if (this.tradingGoals.ioDetails == null) {
          this.tradingGoals.ioDetails = new ArrayList<>();
        }

        if (this.tradingGoals.performanceTargets == null) {
          this.tradingGoals.performanceTargets = new ArrayList<>();
        }
      }

      this.isPpc = true;

      setDefaultDates();

      setDefaultBids();
    }

    private void setDefaultBids() {

      if (cpaBid == null) {
        cpaBid = 0.00;
      }
      if (cpcBid == null) {
        cpcBid = 0.00;
      }
    }

    private void setDefaultDates() {

      if (startDate == null) {
        this.startDate = LocalDate.now();
      }

      if (endDate == null) {
        this.endDate = LocalDate.now().plusYears(1);
      }
    }

    public static class OverspendCap {

      public Integer maxJobCount;

      public OverspendCap() {
        this.maxJobCount = null;
      }
    }

    public static class TradingGoals {

      @Valid
      @JsonInclude(JsonInclude.Include.NON_NULL)
      public List<IoDetails> ioDetails;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      @Size(
          max = 2,
          message = "maximum size of Performance targets can be 2(cpa, cpc)",
          groups = {EditJobGroup.class, Default.class})
      public List<PerformanceTargets> performanceTargets;

      public List<IoDetails> getIoDetails() {
        return ioDetails;
      }

      public List<PerformanceTargets> getPerformanceTargets() {
        return performanceTargets;
      }

      public TradingGoals() {}

      /** Taking IoDetails. */
      public void addIoDetails(String ioNumber, int value, LocalDate startDate, LocalDate endDate) {
        if (ioDetails == null) {
          ioDetails = new ArrayList<>();
        }

        ioDetails.add(new IoDetails(ioNumber, value, startDate, endDate));
      }

      /** Adding Performance Targets. */
      public void addPerformanceTargets(String type, Double value) {
        if (performanceTargets == null) {
          performanceTargets = new ArrayList<>();
        }

        performanceTargets.add(new PerformanceTargets(type, value));
      }

      public static class IoDetails {
        public String number;

        public Integer value;

        public IoDetails() {}

        public String getNumber() {
          return number;
        }

        public Integer getValue() {
          return value;
        }

        public LocalDate getStartDate() {
          return startDate;
        }

        public LocalDate getEndDate() {
          return endDate;
        }

        @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
        @NotNull(
            message = "startDate can't be null",
            groups = {EditJobGroup.class, Default.class})
        public LocalDate startDate;

        @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
        @NotNull(
            message = "endDate can't be null",
            groups = {EditJobGroup.class, Default.class})
        public LocalDate endDate;

        @AssertTrue(
            message = "startDate must be <= endDate for IoDetail",
            groups = {EditJobGroup.class, Default.class})
        @JsonIgnore
        public boolean isValidDate() {

          return startDate == null || endDate == null || endDate.compareTo(startDate) >= 0;
        }

        /** constructor for IoDetails. */
        public IoDetails(String number, Integer value, LocalDate startDate, LocalDate endDate) {
          this.number = number;
          this.value = value;
          this.startDate = startDate;
          this.endDate = endDate;
        }
      }

      public static class PerformanceTargets {
        public String type;
        public Double value;

        public PerformanceTargets() {}

        public String getType() {
          return type;
        }

        public Double getValue() {
          return value;
        }

        public PerformanceTargets(String type, Double value) {
          this.type = type;
          this.value = value;
        }
      }
    }

    public static class Placements {

      @NotEmpty(
          message = "placementId can't be null/empty",
          groups = {EditJobGroup.class, Default.class})
      @JsonProperty("pValue")
      public String publisher;

      @JsonIgnore public boolean delete;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      @Min(
          value = 0,
          message = "bid can't be negative,",
          groups = {EditJobGroup.class, Default.class})
      public Double bid;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      @Valid
      public CapDto budget;

      public Placements(String publisher, boolean delete) {
        this.publisher = publisher;
        this.delete = delete;
      }

      /** Placement constructor with bid and budget. */
      public Placements(String publisher, Double bid, CapDto budget) {

        this.publisher = publisher;
        this.bid = bid;
        this.budget = budget;
      }

      /** constructor for placements with bid. */
      public Placements(String publisher, Double bid) {

        this.publisher = publisher;
        this.bid = bid;
      }

      public void setPublisher(String publisher) {
        this.publisher = publisher;
      }

      public void setDelete(boolean delete) {
        this.delete = delete;
      }

      public void setBid(Double bid) {
        this.bid = bid;
      }

      public void setBudget(CapDto budget) {
        this.budget = budget;
      }

      public Placements(String publisher) {
        this.publisher = publisher;
      }

      @AssertTrue(
          message = "one of placement's budget is Invalid, there is no daily option",
          groups = {EditJobGroup.class, Default.class})
      @JsonIgnore
      public boolean isValidBudget() {
        return budget == null || budget.freq != Freq.Daily;
      }
    }

    public static class RecommendationAudit {

      @NotNull public List<String> result;

      @NotNull public List<String> acceptedResult;

      /** Recommendation Audit. */
      public RecommendationAudit() {
        this.result = new ArrayList<>();

        this.acceptedResult = new ArrayList<>();
      }
    }
  }
}
