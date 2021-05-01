package com.joveo.eqrtestsdk.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.JoveoEntity;
import com.joveo.eqrtestsdk.models.PublisherDto;
import com.joveo.eqrtestsdk.models.validationgroups.EditPublisher;
import com.typesafe.config.Config;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublisherService extends BaseService {

  private static Logger logger = LoggerFactory.getLogger(PublisherService.class);

  /**
   * injecting mapper, executor and validator.
   *
   * @param executor Http executor
   * @param objectMapper ObjectMapper instance
   * @param validator Validator object
   */
  @Inject
  public PublisherService(
      JoveoHttpExecutor executor, ObjectMapper objectMapper, Validator validator) {
    this.executor = executor;
    this.validator = validator;
    this.objectMapper = objectMapper;
  }

  /**
   * setting metadata.
   *
   * @param publisher publisher Dto
   * @param session session details
   */
  public void setAgencyMetadataForClickDefinitions(PublisherDto publisher, Session session) {

    String agencyId = session.getInstanceIdentifier();

    publisher.setAgency(agencyId, publisher.getAgencyIdClass());
    publisher.setCreatedBy(session.getUsername());
    publisher.setAgencyIdForClickDefinitions(agencyId);
  }

  /**
   * create publisher.
   *
   * @param session session details
   * @param conf configuration details
   * @param publisher publisher Dto
   * @return string
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  public String create(Session session, Config conf, PublisherDto publisher)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    publisher.setDefaultValue();
    this.setAgencyMetadataForClickDefinitions(publisher, session);

    String validationErrors = validateEntity(publisher, validator);

    if (validationErrors != null) {
      logger.error(validationErrors);
      throw new InvalidInputException(validationErrors);
    }

    RestResponse response =
        executor.post(
            session,
            conf.getString("MojoBaseUrl") + "/api/admin/publishers/perPublisher",
            publisher);

    Boolean success = response.extractByKeyWithoutData("success");

    if (Boolean.TRUE.equals(success)) {
      return publisher.getValue();
    } else {
      logger.error("Unable to create Publisher: ");
      throw new UnexpectedResponseException("Unable to create Publisher ");
    }
  }

  /**
   * edit publisher.
   *
   * @param session session details
   * @param config config details
   * @param publisher publisher Dto
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  public void edit(Session session, Config config, PublisherDto publisher)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    RestResponse getResponse =
        executor.get(
            session,
            config.getString("MojoBaseUrl")
                + "/api/admin/publishers/perPublisher?agency=undefined&placementValue"
                + "="
                + publisher.getValue());

    if (getResponse.getResponseCode() != 200) {
      String errorMessage =
          "Unable to make getApi call to Publisher, check placementValue: "
              + getResponse.toString();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
    String bidType = getResponse.extractByPath("placement", "bidType", "name").replace("\"", "");

    String url = getResponse.extractByPath("placement", "url").replace("\"", "");

    String name = getResponse.extractByPath("placement", "name").replace("\"", "");

    publisher.setBidType(bidType);
    publisher.setPublisherUrl(url);
    publisher.setName(name);

    String validationErrors = validateEditEntity(publisher, validator);

    if (validationErrors != null) {
      logger.error(validationErrors);
      throw new InvalidInputException(validationErrors);
    }

    RestResponse response =
        executor.put(
            session,
            config.getString("MojoBaseUrl") + "/api/admin/publishers/perPublisher",
            publisher);

    Boolean success = response.extractByKeyWithoutData("success");

    if (Boolean.TRUE.equals(success)) {
      return;
    } else {
      logger.error("Unable to update Publisher: " + response.toString());
      throw new UnexpectedResponseException("Unable to update Publisher " + response.toString());
    }
  }

  /**
   * Gets min bid of a given publisher.
   *
   * @param session Session
   * @param conf Configuration
   * @param publisherId Publisher ID
   * @return MinBid of given publisher
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   */
  public Double getMinBid(Session session, Config conf, String publisherId)
      throws UnexpectedResponseException, ApiRequestException {
    String agencyId = session.getInstanceIdentifier();
    RestResponse getResponse =
        executor.get(
            session,
            conf.getString("MojoBaseUrl")
                + "/api/admin/publishers/perPublisher?agency="
                + agencyId
                + "&placementValue="
                + publisherId);

    if (getResponse.getResponseCode() != 200) {
      String errorMessage = "Unable to make GET API call to Publisher: " + getResponse.toString();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    Double minBid = Double.parseDouble(getResponse.extractByPath("placement", "minBid"));
    return minBid;
  }

  /**
   * . validation.
   *
   * @param entity entity field
   * @param validator Validator object
   * @param <T> generic param generic param
   * @return generics
   */
  public <T> String validateEditEntity(T entity, Validator validator) {

    Set<ConstraintViolation<T>> constraintViolations =
        validator.validate(entity, EditPublisher.class);

    return validationMessages(constraintViolations);
  }

  @Override
  public String getEntity() {
    return JoveoEntity.publishers.toString();
  }
}
