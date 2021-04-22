package com.joveo.eqrtestsdk.core.mojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.HttpExecutor;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoveoHttpExecutor implements HttpExecutor {
  private HttpClient client;
  private ObjectMapper objectMapper;
  private static Logger logger = LoggerFactory.getLogger(JoveoHttpExecutor.class);

  @Inject
  public JoveoHttpExecutor(HttpClient client, ObjectMapper objectMapper) {
    this.client = client;
    this.objectMapper = objectMapper;
  }

  public RestResponse post(
      Session session,
      String url,
      Object body,
      Map<String, String> headers,
      Map<String, String> urlParams)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpPost(), session, url, body, headers, urlParams);
  }

  public RestResponse post(Session session, String url, Object body)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpPost(), session, url, body, new HashMap<>(), new HashMap<>());
  }

  private Header[] getHeaders(Session session, Map<String, String> requestHeaders) {
    ArrayList<Header> headers = new ArrayList<>();
    for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
      headers.add(new BasicHeader(header.getKey(), header.getValue()));
    }
    headers.addAll(getJoveoAuthHeaders(session));
    return headers.toArray(new Header[headers.size()]);
  }

  private URI buildUri(String url, Map<String, String> urlParams) throws URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(url);
    for (Map.Entry<String, String> param : urlParams.entrySet()) {
      uriBuilder.addParameter(param.getKey(), param.getValue());
    }
    return uriBuilder.build();
  }

  private List<Header> getJoveoAuthHeaders(Session session) {
    Header authHeader = new BasicHeader(session.getAuthKey(), session.getAuthToken());
    Header agencyHeader =
        new BasicHeader(session.getInstanceIdentifierKey(), session.getInstanceIdentifier());
    return new ArrayList<>(Arrays.asList(authHeader, agencyHeader));
  }

  public RestResponse get(
      Session session, String url, Map<String, String> headers, Map<String, String> urlParams)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpGet(), session, url, null, headers, urlParams);
  }

  public RestResponse get(Session session, String url)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpGet(), session, url, null, new HashMap<>(), new HashMap<>());
  }

  public RestResponse put(
      Session session,
      String url,
      Object body,
      Map<String, String> headers,
      Map<String, String> urlParams)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpPut(), session, url, body, headers, urlParams);
  }

  public RestResponse put(Session session, String url, Object body)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpPut(), session, url, body, new HashMap<>(), new HashMap<>());
  }

  public RestResponse delete(
      Session session, String url, Map<String, String> headers, Map<String, String> urlParams)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpDelete(), session, url, null, headers, urlParams);
  }

  public RestResponse delete(Session session, String url)
      throws ApiRequestException, UnexpectedResponseException {
    return httpRequest(new HttpDelete(), session, url, null, new HashMap<>(), new HashMap<>());
  }

  /**
   * . HttpRequest method
   *
   * @param request Request
   * @param session session details Stores all session related information.
   * @param url Request url
   * @param body Request body
   * @param headers Request headers
   * @param urlParams Request urlParams
   * @return RestResponse Object
   * @throws ApiRequestException throws API request exception
   * @throws UnexpectedResponseException The API response was not as expected when unexpected
   */
  public RestResponse httpRequest(
      HttpRequestBase request,
      Session session,
      String url,
      Object body,
      Map<String, String> headers,
      Map<String, String> urlParams)
      throws ApiRequestException, UnexpectedResponseException {

    try {
      String jsonBody = objectMapper.writeValueAsString(body);
      request.setURI(buildUri(url, urlParams));
      request.setHeaders(getHeaders(session, headers));

      if (body != null) {
        ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(jsonBody));
      }

      return new RestResponse(client.execute(request), objectMapper, request.toString());
    } catch (URISyntaxException e) {
      logger.error(e.getMessage());
      throw new ApiRequestException("Incorrect URL " + url);
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
      throw new ApiRequestException("Unable to serialize body of request" + e.getMessage());
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new ApiRequestException("unable to make POST API request " + request.toString(), e);
    }
  }
}
