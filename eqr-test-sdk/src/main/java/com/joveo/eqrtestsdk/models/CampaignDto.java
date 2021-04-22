package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.validationgroups.EditCampaign;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CampaignDto {

  @Valid private CampaignParams params;

  @NotEmpty(
      message = "campaignId can't be empty/null",
      groups = {EditCampaign.class})
  @JsonIgnore
  private String campaignId;

  public CampaignDto() {
    this.params = new CampaignParams();
  }

  /** Setting ClientId. */
  public void setClientId(String clientId) {

    this.params.clientId = clientId;
    this.params.clientIds = new ArrayList<>();
    this.params.clientIds.add(clientId);
  }

  public void setCampaignId(String id) {
    this.campaignId = id;
  }

  public void setName(String name) {
    this.params.name = name;
  }

  public void setBudget(Double budget) {
    params.budgetCap = new CapDto(budget);
  }

  public void setEndDate(LocalDate endDate) {
    params.endDate = endDate;
  }

  public void setStartDate(LocalDate startDate) {
    params.startDate = startDate;
  }

  @JsonIgnore
  public LocalDate getStartDate() {
    return params.startDate;
  }

  @JsonIgnore
  public LocalDate getEndDate() {
    return params.endDate;
  }

  @JsonIgnore
  public String getClientId() {
    return this.params.clientId;
  }

  @JsonIgnore
  public String getCampaignId() {
    return campaignId;
  }

  @JsonIgnore
  public CapDto getBudgetCap() {
    return this.params.budgetCap;
  }

  @JsonIgnore
  public String getName() {
    return this.params.name;
  }

  /** setting Default dates for campaign. */
  public void setDefaultDates() {

    if (this.params.startDate == null) {
      this.params.startDate = LocalDate.now();
    }
    if (this.params.endDate == null) {
      this.params.endDate = LocalDate.now().plusYears(1);
    }
  }

  public static class CampaignParams {

    @NotEmpty(
        message = "clientId can't be null/empty",
        groups = {EditCampaign.class, Default.class})
    public String clientId;

    public List<String> clientIds;

    @NotEmpty(message = "Campaign name can't be null/empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String name;

    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "startDate can't be null")
    @FutureOrPresent(message = "start-date must be a future or present date")
    @Null(message = "campaign startDate is not Editable", groups = EditCampaign.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDate startDate;

    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "endDate can't be null")
    @FutureOrPresent(
        message = "end-date must be a future or present date",
        groups = {com.joveo.eqrtestsdk.models.validationgroups.EditCampaign.class, Default.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDate endDate;

    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public CapDto budgetCap;

    /** StartDate must less than EndDate. */
    @AssertTrue(message = "startDate must be <= endDate")
    @JsonIgnore
    public boolean isValidRange() {
      if (endDate == null || startDate == null) {
        return true;
      }
      return endDate.compareTo(startDate) >= 0;
    }
  }
}
