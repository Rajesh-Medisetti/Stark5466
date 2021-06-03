package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.GroupingJobFilter;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.Administration;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.BidStrategy;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.ClientCampaignJobGroupLevel;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.JobLevel;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.Performance;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@JsonSerialize
@Valid
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AutomationDto {

  @NotNull(message = "filerLevel can't be null")
  FilterLevel filterLevel;

  @NotNull(message = "filter can't be null")
  List<Filter> filters;

  @Valid
  @Size(min = 1, message = "there must be 1 condition in Automation Rule")
  @NotNull(message = "condition can't be empty, there must be at least 1 condition")
  List<Conditions> conditions;

  @NotNull(message = "actionType can't be null/empty")
  ActionType actionType;

  public void setActionType(ActionType actionType) {
    this.actionType = actionType;
  }

  @JsonSetter("adminActions")
  public void setAdministrationAction(AdminAction adminActions) {
    administrationAction = new AdministrationAction(adminActions);
  }

  @Valid
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @NotNull(
      message = "Action can't be null",
      groups = {Performance.class, BidStrategy.class})
  @Null(
      groups = {Administration.class},
      message = "ActionType is Administration don't " + "set action for bidStrategy/Performance")
  Actions actions;

  @Valid
  @NotNull(
      message = "Administration action can't be null",
      groups = {Administration.class})
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Null(
      groups = {Performance.class, BidStrategy.class},
      message = "ActionType is bid/performance don't " + "set action for administration")
  AdministrationAction administrationAction;

  @Valid
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Null(
      message = "repeatAction is not allowed in Administration Automation",
      groups = {Administration.class})
  RepeatActionDuration repeatActionDuration;

  @NotNull(message = "startDate can't be null/empty in Automation")
  @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
  @FutureOrPresent(message = "startDate can't be past")
  LocalDate startDate;

  @NotNull(message = "endDate can't be null/empty in Automation")
  @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
  LocalDate endDate;

  @NotEmpty(message = "automation name can't be null/empty")
  String name;

  @NotEmpty(message = "clientId can't be null/empty in Automation")
  String clientId;

  boolean allIds;

  @NotEmpty(message = "campaignId can't be null/empty in Automation")
  String campaignId;

  @NotEmpty(message = "jobGroupId can't be null/empty")
  String jobGroupId;

  boolean notifyUser;
  String status;
  boolean applyToChildren;

  @JsonProperty("conditionOp")
  GroupConditionOperator groupConditionOperator;

  @Null(
      message = "jobFilter Rule is not available in Client/Campaign/JobGroup level ",
      groups = {ClientCampaignJobGroupLevel.class})
  @NotNull(
      groups = {JobLevel.class},
      message = "jobFilter can't be null in JobLevel Automation")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  JobFilter jobFilter;

  @NotNull(
      groups = {JobLevel.class},
      message = "jobFilterLevel can't be null")
  @Null(
      message = "jobFilterLevel shouldn't be used ",
      groups = {ClientCampaignJobGroupLevel.class})
  @JsonInclude(JsonInclude.Include.NON_NULL)
  JobFilterLevel jobFilterLevel;

  public void setJobFilter(GroupingJobFilter groupingJobFilter) {
    this.jobFilter = new JobFilter(groupingJobFilter);
  }

  public void setJobFilterLevel(JobFilterLevel jobFilterLevel) {
    this.jobFilterLevel = jobFilterLevel;
  }

  /** . Validation Function for valid Dates */
  @AssertTrue(message = "startDate must be <= endDate")
  @JsonIgnore
  public boolean isValidDates() {
    if (endDate == null || startDate == null) {
      return true;
    }
    return endDate.compareTo(startDate) >= 0;
  }

  /** . Setting Default values in Constructor */
  public AutomationDto() {

    allIds = false;
    notifyUser = false;
    status = "A";
    applyToChildren = true;
    groupConditionOperator = GroupConditionOperator.AND;
    startDate = LocalDate.now();
    endDate = LocalDate.now().plusWeeks(1);
  }

  public void setClientId(String id) {
    this.clientId = id;
  }

  public void setCampaignId(String id) {
    this.campaignId = id;
  }

  public void setJobGroupId(String id) {
    this.jobGroupId = id;
  }

  public void setFilterLevel(FilterLevel filterLevel) {
    this.filterLevel = filterLevel;
  }

  /** . Setting Entity Ids */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public void setEntityIds() {

    if (filters == null
        || filterLevel == null
        || (filterLevel.equals(FilterLevel.jobs) && jobFilterLevel == null)) {
      return;
    }

    if (filterLevel.equals(FilterLevel.clients)) {
      clientId = filters.get(0).id;
      campaignId = "0";
      jobGroupId = "0";
    } else if (filterLevel.equals(FilterLevel.campaigns)) {
      campaignId = filters.get(0).id;
      jobGroupId = "0";
    } else if (filterLevel.equals(FilterLevel.jobgroups)) {
      jobGroupId = filters.get(0).id;
      campaignId = "0";
    } else if (jobFilterLevel.equals(JobFilterLevel.clients)) {
      clientId = filters.get(0).id;
      campaignId = "0";
      jobGroupId = "0";
    } else if (jobFilterLevel.equals(JobFilterLevel.campaigns)) {
      campaignId = filters.get(0).id;
      jobGroupId = "0";
    } else {
      jobGroupId = filters.get(0).id;
      campaignId = "0";
    }
    setDefaultJobFilter();
  }

  private void setDefaultJobFilter() {
    if (filterLevel.equals(FilterLevel.jobs) && jobFilter == null) {
      jobFilter = new JobFilter(com.joveo.eqrtestsdk.models.JobFilter.and());
    }
  }

  /** . Setter for filter in Automation */
  public void setFilters(String filterId, String filterName, String... pubIds) {

    if (filters == null) {
      filters = new ArrayList<>();
    }

    List<String> placementIds = new ArrayList<>();

    if (pubIds.length >= 1) {
      Collections.addAll(placementIds, pubIds);
    }

    filters.add(new Filter(filterId, filterName, placementIds, false));
  }

  /** . setting conditions in Automation */
  public void addCondition(
      Parameter parameter,
      ConditionOperator conditionOperator,
      Double value,
      ConditionDuration duration,
      ConditionParameterUnit unit) {
    if (conditions == null) {
      conditions = new ArrayList<>();
    }
    conditions.add(new Conditions(value, parameter, conditionOperator, duration, unit));
  }

  public void setBidStrategyAction(
      BidStrategyActionParameter parameter, Action action, Double value, ActionUnit unit) {
    actions = new Actions(action, parameter, value, unit);
  }

  public void setPerformanceAction(
      PerformanceActionParameter parameter, Action action, Double value, ActionUnit actionUnit) {
    actions = new Actions(action, parameter, value, actionUnit);
  }

  public void setRepeatActionAfterDays(Double days) {
    repeatActionDuration = new RepeatActionDuration("every", days, "days");
  }

  public void setAutomationName(String name) {
    this.name = name;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getClientId() {
    return clientId;
  }

  public FilterLevel getFilterLevel() {
    return filterLevel;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public Actions getActions() {
    return actions;
  }

  public AdministrationAction getAdministrationAction() {
    return administrationAction;
  }

  public RepeatActionDuration getRepeatActionDuration() {
    return repeatActionDuration;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public String getName() {
    return name;
  }

  public boolean isAllIds() {
    return allIds;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public String getJobGroupId() {
    return jobGroupId;
  }

  public boolean isNotifyUser() {
    return notifyUser;
  }

  public String getStatus() {
    return status;
  }

  public boolean isApplyToChildren() {
    return applyToChildren;
  }

  public GroupConditionOperator getConditionOp() {
    return groupConditionOperator;
  }

  public JobFilter getJobFilter() {
    return jobFilter;
  }

  public List<Conditions> getConditions() {
    return conditions;
  }

  public JobFilterLevel getJobFilterLevel() {
    return jobFilterLevel;
  }

  public List<Filter> getFilters() {
    return filters;
  }
}
