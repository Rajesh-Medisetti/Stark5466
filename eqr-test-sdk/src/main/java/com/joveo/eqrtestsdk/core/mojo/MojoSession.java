package com.joveo.eqrtestsdk.core.mojo;

import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidCredentialsException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.io.IOException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MojoSession implements Session {
  private static Logger logger = LoggerFactory.getLogger(MojoSession.class);
  private String username;
  private String password;
  private String token;
  private String agencyId;
  private String baseUrl;

  /**
   * . Contains all session related information
   *
   * @param username username
   * @param password password
   * @param baseUrl MojoURL
   * @throws UnexpectedResponseException The API response was not as expected On unexpected Response
   * @throws ApiRequestException something wrong with request
   * @throws InvalidCredentialsException On invalid credentials
   */
  public MojoSession(String username, String password, String baseUrl)
      throws UnexpectedResponseException, ApiRequestException, InvalidCredentialsException {
    this.username = username;
    this.password = password;
    this.baseUrl = baseUrl;
    login(createLoginClient());
  }

  private void login(HttpClient client)
      throws UnexpectedResponseException, ApiRequestException, InvalidCredentialsException {
    HttpPost request = new HttpPost(baseUrl + "/api/loginv2");
    try {
      RestResponse response = new RestResponse(client.execute(request), request.toString());
      if (response.isSuccess()) {
        setLoginState(response.extractByKey("accessToken"), response.extractByKey("agencyId"));
      } else {
        throw new InvalidCredentialsException(response.getJoveoErrorMessage());
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new ApiRequestException("unable to make login API request " + request.toString(), e);
    }
  }

  private void setLoginState(String token, String agencyId) throws IOException {
    this.token = token;
    this.agencyId = agencyId;
  }

  private HttpClient createLoginClient() {
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
    provider.setCredentials(AuthScope.ANY, credentials);

    return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
  }

  public String getAuthToken() {
    return this.token;
  }

  public String getAuthKey() {
    return "MojoAccessToken";
  }

  public String getInstanceIdentifierKey() {
    return "MojoAgencyId";
  }

  public String getInstanceIdentifier() {
    return this.agencyId;
  }

  public String getUsername() {
    return this.username;
  }
}
