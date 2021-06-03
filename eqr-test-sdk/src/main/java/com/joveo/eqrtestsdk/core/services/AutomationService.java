package com.joveo.eqrtestsdk.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.models.fetcher.PlacementGetResponse;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.JoveoEntity;
import com.joveo.eqrtestsdk.models.automation.ActionType;
import com.joveo.eqrtestsdk.models.automation.AutomationData;
import com.joveo.eqrtestsdk.models.automation.AutomationDto;
import com.joveo.eqrtestsdk.models.automation.FilterLevel;
import com.joveo.eqrtestsdk.models.automation.PauseDto;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.Administration;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.BidStrategy;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.ClientCampaignJobGroupLevel;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.JobLevel;
import com.joveo.eqrtestsdk.models.automation.automationvalidationgroups.Performance;
import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomationService extends BaseService {
  private static final Logger logger = LoggerFactory.getLogger(AutomationService.class);

  @Inject
  public AutomationService(JoveoHttpExecutor executor, Validator validator) {
    this.executor = executor;
    this.validator = validator;
  }

  /** . setUp Automation */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public String create(Session session, Config conf, AutomationDto automation)
      throws UnexpectedResponseException, InvalidInputException, ApiRequestException,
          JsonProcessingException {

    automation.setEntityIds();

    String validationMessages;

    if (automation.getFilterLevel() == null) {
      validationMessages = "filterLevel can't be null";
    } else if (automation.getActionType() == null) {
      validationMessages = "actionType can't be null";
    } else {
      validationMessages = validateDto(automation);
    }

    if (validationMessages != null) {
      logger.error(validationMessages);
      throw new InvalidInputException(validationMessages);
    }

    checkPublishers(automation, session, conf);

    AutomationData automationData = new AutomationData();
    automationData.setAutomationDto(automation);

    List<AutomationDto> orderedRules = new ArrayList<>();
    orderedRules.add(automation);
    automationData.setAutomationDtoList(orderedRules);

    RestResponse response =
        executor.post(
            session,
            conf.getString("MojoBaseUrl")
                + "/api/rules/save?v2=true&clientID="
                + automation.getClientId()
                + "&campaignID="
                + automation.getCampaignId()
                + "&jobGroupID="
                + automation.getJobGroupId(),
            automationData);

    if (response.getResponseCode() == 200 && response.extractByKeyWithData("success")) {
      return response.extractByPath("data", "id");
    } else {
      logger.error("could not create automation " + response.toString());
      throw new UnexpectedResponseException("could not create automation " + response.toString());
    }
  }

  private String validateDto(AutomationDto automationDto) {

    Set<ConstraintViolation<AutomationDto>> constraintViolations;

    if (!automationDto.getFilterLevel().equals(FilterLevel.jobs)) {
      if (automationDto.getActionType().equals(ActionType.bidStrategy)) {
        constraintViolations =
            validator.validate(
                automationDto, Default.class, BidStrategy.class, ClientCampaignJobGroupLevel.class);

      } else if (automationDto.getActionType().equals(ActionType.performance)) {
        constraintViolations =
            validator.validate(
                automationDto, Default.class, Performance.class, ClientCampaignJobGroupLevel.class);
      } else {
        constraintViolations =
            validator.validate(
                automationDto,
                Default.class,
                Administration.class,
                ClientCampaignJobGroupLevel.class);
      }
    } else {
      constraintViolations = new HashSet<>(validateDtoAtJobLevel(automationDto));
    }

    return getValidationMessages(constraintViolations);
  }

  private Set<ConstraintViolation<AutomationDto>> validateDtoAtJobLevel(
      AutomationDto automationDto) {

    Set<ConstraintViolation<AutomationDto>> constraintViolations = new HashSet<>();

    if (automationDto.getActionType().equals(ActionType.bidStrategy)) {
      constraintViolations =
          validator.validate(automationDto, Default.class, BidStrategy.class, JobLevel.class);

    } else if (automationDto.getActionType().equals(ActionType.performance)) {
      constraintViolations =
          validator.validate(automationDto, Default.class, Performance.class, JobLevel.class);
    } else {
      constraintViolations =
          validator.validate(automationDto, Default.class, Administration.class, JobLevel.class);
    }
    return constraintViolations;
  }

  private String getValidationMessages(
      Set<ConstraintViolation<AutomationDto>> constraintViolations) {

    StringBuilder message = new StringBuilder();
    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<AutomationDto> violation : constraintViolations) {
        message.append(violation.getMessage()).append(", ");
      }
    }
    if (!message.toString().equals("")) {
      return message.toString();
    }
    return null;
  }

  private void checkPublishers(AutomationDto automation, Session session, Config conf)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    RestResponse getResponse =
        executor.get(
            session,
            conf.getString("MojoBaseUrl")
                + "/api/recengine/v2/publishers?cat=&budget=0&deviceType=all&bidType=all");

    if (getResponse.getResponseCode() != 200) {
      String errorMessage =
          "Unable to make getClient Request , check clientId " + getResponse.toString();

      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    List<PlacementGetResponse> getResponsePlacements =
        Arrays.asList(getResponse.toEntity(PlacementGetResponse[].class).clone());

    List<String> placementIds = automation.getFilters().get(0).getPlacementIds();

    List<String> responsePlacementIds = new ArrayList<>();

    for (PlacementGetResponse response : getResponsePlacements) {
      responsePlacementIds.add(response.getPublisher());
    }

    for (String id : placementIds) {
      if (!responsePlacementIds.contains(id)) {
        String errorMessage = id + " is not present in available in placements";
        logger.error(errorMessage);
        throw new InvalidInputException(errorMessage);
      }
    }
  }

  /** . pause the Automation */
  public void pause(String id, Session session, Config config, PauseDto pauseDto)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    String errorMessage = validateEntity(pauseDto, validator);

    if (errorMessage != null) {
      logger.error(errorMessage);
      throw new InvalidInputException(errorMessage);
    }

    RestResponse response =
        executor.put(session, config.getString("MojoBaseUrl") + "/api/rules/v2", pauseDto);

    errorMessage = "Unable to pause Automation: ";

    if (response.getResponseCode() != 200 || !response.isSuccess()) {
      logger.info(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
    logger.info(id + " automation paused successfully");
  }

  @Override
  public String getEntity() {
    return JoveoEntity.Automation.toString();
  }
}
