package com.joveo.eqrtestsdk.core.entities;

import static com.joveo.eqrtestsdk.utils.DateUtils.startOfMonth;

import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.PlatformFiltersDto;
import java.time.LocalDate;
import java.util.Optional;

public class Job {
  public final String id;
  public final String reqId;
  public final String clientId;
  private final Driver driver;

  /**
   * job constructor.
   *
   * @param driver Driver instance
   * @param clientId client id
   * @param jobId job id
   * @param reqId ref number
   */
  public Job(Driver driver, String clientId, String jobId, String reqId) {
    this.driver = driver;
    this.clientId = clientId;
    this.id = jobId;
    this.reqId = reqId;
  }

  /**
   * fetch job details.
   *
   * @return Optional
   * @throws MojoException custom MOjo exception
   */
  public Optional<JobStats> getJobDetails() throws MojoException {
    return driver.jobService.getJobDetails(
        driver.session,
        driver.conf,
        new PlatformFiltersDto(),
        this.clientId,
        this.reqId,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }
}
