package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.Administration;
import javax.validation.constraints.NotNull;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AdministrationAction {

  @NotNull(
      message = "administration action can't be null",
      groups = {Administration.class})
  AdminAction type;

  public AdministrationAction(AdminAction adminActions) {
    this.type = adminActions;
  }
}
