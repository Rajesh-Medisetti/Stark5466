package com.joveo.eqrtestsdk.core.mojo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.MojoResponse;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestResponse {
  private String request;
  private String result;
  private int status;
  private ObjectMapper objectMapper;
  private static Logger logger = LoggerFactory.getLogger(RestResponse.class);

  public int getResponseCode() {
    return status;
  }

  /**
   * . Constructor for RestResponse
   *
   * @param response HttpResponse object
   * @param objectMapper ObjectMapper instance
   * @param requestString request in string format
   * @throws UnexpectedResponseException The API response was not as expected throws unexcpected
   *     response
   */
  public RestResponse(HttpResponse response, ObjectMapper objectMapper, String requestString)
      throws UnexpectedResponseException {
    this.request = requestString;
    this.status = response.getStatusLine().getStatusCode();
    this.objectMapper = objectMapper;
    try {
      this.result = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Unable to read API response " + this.toString());
    }
  }

  public RestResponse(HttpResponse response, String requestString)
      throws UnexpectedResponseException {
    this(response, new ObjectMapper(), requestString);
  }

  public boolean isSuccess() throws UnexpectedResponseException {
    return status == 200 && checkSuccessInResponse();
  }

  private boolean checkSuccessInResponse() throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get("success").booleanValue();
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "Expected success key in response, got " + this.toString());
    }
  }

  /**
   * .
   *
   * @param entity entity field
   * @param <T> generic param
   * @return Data
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public <T> T toEntityWithData(Class<T> entity) throws UnexpectedResponseException {
    try {
      return objectMapper.readValue(objectMapper.readTree(result).get("data").toString(), entity);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "Expected " + entity.getName() + " ,request: " + this.toString());
    }
  }

  /**
   * .
   *
   * @param entity entity field
   * @param <T> generic param
   * @return Data
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public <T> T toEntity(Class<T> entity) throws UnexpectedResponseException {
    try {
      return objectMapper.readValue(result, entity);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "Expected " + entity.getName() + " ,request: " + this.toString());
    }
  }

  /**
   * .
   *
   * @param key key
   * @return JsonNode
   * @throws UnexpectedResponseException On unexpected Response
   */
  public JsonNode toJsonNode(String key) throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get(key);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "Expected " + key + " in response, request: " + this.toString());
    }
  }

  /**
   * .
   *
   * @return errorMessage
   * @throws UnexpectedResponseException On unexpected Response
   */
  public String getJoveoErrorMessage() throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get("error").toString();
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Couldn't find error key in result " + result);
    }
  }

  /**
   * .
   *
   * @return errorMessage for update
   * @throws UnexpectedResponseException On unexpected Response
   */
  public String getJoveoUpdateErrorMeesage() throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get("data").get("data").get(0).get("error").toString();
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Couldn't find error key in result " + result);
    }
  }

  /**
   * .
   *
   * @param key key
   * @return value associated with key
   * @throws UnexpectedResponseException On unexpected Response
   */
  public String extractByKey(String key) throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get("data").get(key).textValue();
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Couldn't find key " + key + " in result " + result);
    }
  }

  /**
   * .
   *
   * @param key key
   * @return responseStatus
   * @throws UnexpectedResponseException On unexpected Response
   */
  public Boolean extractByKeyWithoutData(String key) throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get(key).booleanValue();
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Couldn't find key " + key + " in result " + result);
    }
  }

  /**
   * .
   *
   * @param key key
   * @return responseStatus
   * @throws UnexpectedResponseException On unexpected Response
   */
  public Boolean extractByKeyWithData(String key) throws UnexpectedResponseException {
    try {
      return objectMapper.readTree(result).get("data").get(key).booleanValue();
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Couldn't find key " + key + " in result " + result);
    }
  }

  /**
   * .
   *
   * @param keys extracting by keys
   * @return values associated with keys
   * @throws UnexpectedResponseException On unexpected Response
   */
  public String extractByPath(String... keys) throws UnexpectedResponseException {
    try {
      JsonNode response = objectMapper.readTree(result);
      for (String key : keys) {
        response = response.get(key);
      }
      return response.toString();
    } catch (IOException e) {
      String path = String.join("/", keys);
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("Couldn't find path " + path + "in result " + result);
    }
  }

  /**
   * .
   *
   * @param typeReference for making it generic
   * @param <T> Generic
   * @return data
   * @throws UnexpectedResponseException On unexpected Response
   */
  public <T> T toMojoResponse(TypeReference typeReference) throws UnexpectedResponseException {
    try {
      T data =
          (T)
              objectMapper.readValue(
                  objectMapper.readTree(result).get("data").toString(), MojoResponse.class);
      if (((MojoResponse) data).getData().isEmpty()) {
        return (T) new MojoResponse<>();
      }
      return (T) objectMapper.convertValue(data, typeReference);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "Expected " + MojoResponse.class.getName() + " ,request: " + this.toString());
    }
  }

  @Override
  public String toString() {
    return "{"
        + "request='"
        + request
        + '\''
        + ", result='"
        + result
        + '\''
        + ", status="
        + status
        + '}';
  }
}
