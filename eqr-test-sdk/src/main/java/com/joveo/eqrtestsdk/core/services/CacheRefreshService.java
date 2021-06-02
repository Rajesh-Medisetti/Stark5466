package com.joveo.eqrtestsdk.core.services;

import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheRefreshService {
  private static Logger logger = LoggerFactory.getLogger(CacheRefreshService.class);
  private JoveoHttpExecutor executor;

  @Inject
  public CacheRefreshService(JoveoHttpExecutor executor) {
    this.executor = executor;
  }

  /**
   * Refresh Cache.
   *
   * @param session session
   * @param awsService instance of aws service
   * @param tag tag
   * @param url url
   * @throws UnexpectedResponseException Custom Exception
   * @throws ApiRequestException Custom Exception
   */
  public void refreshCache(Session session, AwsService awsService, String tag, String url)
      throws UnexpectedResponseException, ApiRequestException {
    List<String> ipList = awsService.getIpByTags(tag, "true");

    for (String ip : ipList) {
      RestResponse response = executor.post(session, "http://" + ip + url, null);

      if (!response.isSuccess()) {
        String errorMessage = "Unable to refresh Cache, Status code: " + response.getResponseCode();
        logger.error(errorMessage);
        throw new UnexpectedResponseException(errorMessage);
      }
      logger.info("Successfully refresh cache at: " + ip);
    }
  }
}
