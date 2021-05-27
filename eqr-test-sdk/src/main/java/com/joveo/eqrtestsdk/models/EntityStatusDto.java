package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EntityStatusDto {

  @Valid private Params params;

  public EntityStatusDto() {
    this.params = new Params();
  }

  public void addClientId(String clientId) {
    this.params.clientIds.add(clientId);
  }

  public void addCampaignId(String campaignId) {
    this.params.campaignIds.add(campaignId);
  }

  public void addJobGroupId(String jobGroupId) {
    this.params.jobGroupIds.add(jobGroupId);
  }

  public void addStatus(String status) {
    this.params.status.add(status);
  }

  public static class Params {

    public List<String> clientIds;

    public List<String> campaignIds;

    public List<String> jobGroupIds;

    public List<String> status;

    Params() {
      this.clientIds = new ArrayList<>();
      this.campaignIds = new ArrayList<>();
      this.jobGroupIds = new ArrayList<>();
      this.status = new ArrayList<>();
    }
  }
}
