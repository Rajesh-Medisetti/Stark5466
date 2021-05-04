package com.joveo.eqrtestsdk.core.mojo;

import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.api.Wait;
import com.joveo.eqrtestsdk.api.Waitable;
import com.joveo.eqrtestsdk.core.models.SchedulerRunMetadata;
import com.joveo.eqrtestsdk.core.services.SchedulerService;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerRunner implements Waitable {
  private LocalDateTime triggeredTime;
  private String clientId;
  private String baseUrl;
  private Session session;
  private SchedulerService schedulerService;
  private Duration timeout;
  private Duration refreshInterval;

  private static Logger logger = LoggerFactory.getLogger(SchedulerRunner.class);

  /**
   * .
   *
   * @param clientId clientId
   * @param baseUrl url
   * @param session session
   * @param schedulerService schedulerService
   * @param timeout timeout
   * @param refreshInterval refreshInterval
   */
  public SchedulerRunner(
      String clientId,
      String baseUrl,
      Session session,
      SchedulerService schedulerService,
      Duration timeout,
      Duration refreshInterval) {
    this.clientId = clientId;
    this.baseUrl = baseUrl;
    this.session = session;
    this.schedulerService = schedulerService;
    this.timeout = timeout;
    this.refreshInterval = refreshInterval;
    this.triggeredTime = null;
  }

  /**
   * .
   *
   * @throws TimeoutException On Request TimeOut
   * @throws InterruptWaitException On Interrupted
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException On unexpected Response
   * @throws MojoException On unexpected behaviour
   */
  public void run()
      throws TimeoutException, InterruptWaitException, ApiRequestException,
          UnexpectedResponseException, MojoException {
    logger.info("Scheduler run is triggered at: " + LocalDateTime.now());
    this.triggeredTime = LocalDateTime.now(ZoneOffset.UTC);

    schedulerService.schedule(session, baseUrl, clientId);

    Wait.until(this);
    logger.info("Scheduler run is ended at: " + LocalDateTime.now());
  }

  /**
   * .
   *
   * @return status of completion
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException On unexpected Response
   */
  public Boolean isComplete() throws ApiRequestException, UnexpectedResponseException {
    SchedulerRunMetadata getLatestRunData =
        schedulerService.getLatestRunMetadata(this.session, this.clientId, this.baseUrl);

    if (getLatestRunData != null && getLatestRunData.getTime().isAfter(this.triggeredTime)) {
      return true;
    }
    logger.info("Last scheduled service is not executed yet");
    return false;
  }

  public Duration getTimeout() {
    return this.timeout;
  }

  public Duration getRefreshInterval() {
    return refreshInterval;
  }

  public String getWaiterMessage() {
    return "Scheduler run for clientId " + clientId;
  }
}
