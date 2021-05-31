package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AutomationData {

  AutomationDto automationDto;
  List<AutomationDto> automationDtoList;

  @JsonSetter("ruleDto")
  public void setAutomationDto(AutomationDto automationDto) {
    this.automationDto = automationDto;
  }

  @JsonSetter("orderedRules")
  public void setAutomationDtoList(List<AutomationDto> automationDtoList) {
    this.automationDtoList = automationDtoList;
  }
}
