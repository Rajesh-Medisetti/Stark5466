package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderedRules {

  AutomationDto automationDto;

  public OrderedRules(AutomationDto automationDto) {
    this.automationDto = automationDto;
  }
}
