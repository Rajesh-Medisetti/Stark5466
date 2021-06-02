package com.joveo.eqrtestsdk.core.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.models.SchedulerRunMetadata;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerService {
  private static Logger logger = LoggerFactory.getLogger(SchedulerService.class);
  private JoveoHttpExecutor executor;
  private ObjectMapper objectMapper;

  @Inject
  public SchedulerService(JoveoHttpExecutor executor, ObjectMapper objectMapper) {
    this.executor = executor;
    this.objectMapper = objectMapper;
  }

  /**
   * fetching latest run meta data.
   *
   * @param session session details
   * @param clientId clientId as a string
   * @param baseUrl MojoURL
   * @return schedulerRunMetadata
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public SchedulerRunMetadata getLatestRunMetadata(Session session, String clientId, String baseUrl)
      throws ApiRequestException, UnexpectedResponseException {
    RestResponse response =
        executor.get(session, baseUrl + "/api/batch/dashboard/clients/stats?clientId=" + clientId);

    if (!response.isSuccess()) {
      logger.error(
          "Couldn't get last scheduler executionTime, Mongo has no docs for this client yet. "
              + "status code: "
              + response.getResponseCode());
      // Success false is given before batch picking the request; hence, we have to avoid this
      // false-negative case.
      return null;
    }

    try {
      JsonNode data = response.toJsonNode("data");
      LocalDateTime localDateTime =
          Instant.ofEpochMilli(data.get("latestStats").get("lastSuccessStartTime").asLong())
              .atZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0)))
              .toLocalDateTime();
      return new SchedulerRunMetadata(
          data.get("latestStats").get("LastSuccessExecutionId").textValue(), localDateTime);

    } catch (NullPointerException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("data is missing in response " + response.toString());
    }
  }

  /**
   * scheduler request.
   *
   * @param session session details
   * @param baseUrl MojoURL
   * @param clientId clientId as a string
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   */
  public void schedule(Session session, String baseUrl, String clientId)
      throws UnexpectedResponseException, ApiRequestException {
    ObjectNode client = objectMapper.createObjectNode();
    client.put("clientId", clientId);

    RestResponse response = executor.post(session, baseUrl + "/batch/manualfeed/refresh", client);

    if (!Boolean.parseBoolean(response.extractByPath("data", "success"))) {
      String errorMessage =
          "Unable to run scheduler: "
              + response.extractByPath("data", "data", "message")
              + " status code: "
              + response.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
  }
}
