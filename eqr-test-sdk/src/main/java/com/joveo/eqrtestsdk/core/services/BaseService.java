package com.joveo.eqrtestsdk.core.services;

import static com.joveo.eqrtestsdk.utils.DateUtils.formatAsMojoDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.MojoData;
import com.joveo.eqrtestsdk.models.MojoResponse;
import com.joveo.eqrtestsdk.models.PFfields;
import com.joveo.eqrtestsdk.models.PfOperators;
import com.joveo.eqrtestsdk.models.PlatformFiltersDto;
import com.joveo.eqrtestsdk.models.Stats;
import com.typesafe.config.Config;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseService {
  private static Logger logger = LoggerFactory.getLogger(BaseService.class);
  public ObjectMapper objectMapper;
  public Validator validator;
  JoveoHttpExecutor executor;

  /**
   * validate entity.
   *
   * @param entity JoveoEntity
   * @param validator Validator object
   * @param <T> generic param Entity type
   * @return Errors if invalid, else null
   */
  public <T> String validateEntity(T entity, Validator validator) {
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
    if (!constraintViolations.isEmpty()) {
      StringBuilder message =
          new StringBuilder(
              "Invalid Data of " + entity.getClass().getName() + " Object, Reasons of Failure -");
      for (ConstraintViolation<T> violation : constraintViolations) {
        message.append(violation.getMessage()).append(", ");
      }
      return message.toString();
    }
    return null;
  }

  /**
   * getting validation message.
   *
   * @param constraintViolations contains violations
   * @param <T> generic param
   * @return string
   */
  public <T> String validationMessages(Set<ConstraintViolation<T>> constraintViolations) {

    StringBuilder message = new StringBuilder("");
    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<T> violation : constraintViolations) {
        message.append(violation.getMessage()).append(", ");
      }
    }
    if (!message.toString().equals("")) {
      return message.toString();
    }
    return null;
  }

  /**
   * date validation.
   *
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return boolean
   */
  public Boolean isValidDate(String startDate, LocalDate endDate) {

    return (endDate == null
        || LocalDate.parse(startDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")).compareTo(endDate)
            <= 0);
  }

  /**
   * .
   *
   * @param response response from Api Call
   * @param errorMessage errorMessage
   * @throws UnexpectedResponseException on UnexpectedResponse.
   */
  public void checkResponse(RestResponse response, String errorMessage)
      throws UnexpectedResponseException {

    if (!response.isSuccess()) {
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
  }

  /**
   * getting statistics.
   *
   * @param session session details
   * @param config config details
   * @param clientId clientId as a string
   * @param entityId joveo entity Id
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return stats
   * @throws MojoException throws custom mojo exception
   */
  public Stats getStats(
      Session session,
      Config config,
      String clientId,
      String entityId,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto = enrichFiltersWithEntityId(platformFiltersDto, clientId, entityId);

    String url = config.getString("MojoBaseUrl") + "/flash/api/" + getEntity();
    return getStatsData(session, platformFiltersDto, url, entityId);
  }

  /**
   * getting statistics for entity x placement.
   *
   * @param session session details
   * @param config config details
   * @param clientId clientId as a string
   * @param id passing id as string
   * @param entityId joveo entity Id
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return stats
   * @throws MojoException throws custom mojo exception
   */
  public Stats getStats(
      Session session,
      Config config,
      String clientId,
      String id,
      String entityId,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);
    platformFiltersDto.addRule(entityId, PFfields.entityId, PfOperators.IN);
    platformFiltersDto = enrichFiltersWithEntityId(platformFiltersDto, id);

    String url = config.getString("MojoBaseUrl") + "/flash/api/placements";
    return getStatsData(session, platformFiltersDto, url, entityId);
  }

  private Stats getStatsData(
      Session session, PlatformFiltersDto platformFiltersDto, String url, String entityId)
      throws MojoException, UnexpectedResponseException, ApiRequestException {
    RestResponse response = executor.post(session, url, platformFiltersDto);
    if (!response.isSuccess()) {
      logger.error("failed to get client's stats " + response.getJoveoErrorMessage());
      throw new UnexpectedResponseException(
          "failed to get client's stats " + response.getJoveoErrorMessage());
    }
    MojoResponse<Stats> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<Stats>>() {});

    for (MojoData<Stats> data : mojoResponse.getData()) {
      if (data.getId().equals(entityId)) {
        return data.getFields();
      }
    }
    logger.error("failed to get Stats");
    throw new UnexpectedResponseException("data is missing in response");
  }

  public abstract String getEntity();

  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String id) {
    return platformFiltersDto;
  }

  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String clientId, String id) {
    return platformFiltersDto;
  }
}
