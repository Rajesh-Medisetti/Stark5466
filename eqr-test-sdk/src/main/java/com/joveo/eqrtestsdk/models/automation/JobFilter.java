package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.GroupingJobFilter;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.CampaignLevel;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.ClientLevel;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.JobGroupLevel;
import javax.validation.constraints.Null;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JobFilter {

  @Null(
      message = "jobFilter Rule is not available in Client/Campaign/JobGroup level ",
      groups = {ClientLevel.class, CampaignLevel.class, JobGroupLevel.class})
  GroupingJobFilter filter;

  public JobFilter(
      @Null(
              message = "jobFilter Rule is not available in Client/Campaign/JobGroup level ",
              groups = {ClientLevel.class, CampaignLevel.class, JobGroupLevel.class})
          GroupingJobFilter filter) {
    this.filter = filter;
  }
}
