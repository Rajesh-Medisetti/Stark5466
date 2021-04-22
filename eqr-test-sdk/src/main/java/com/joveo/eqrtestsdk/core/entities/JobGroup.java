package com.joveo.eqrtestsdk.core.entities;

import static com.joveo.eqrtestsdk.utils.DateUtils.startOfMonth;

import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.Stats;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class JobGroup {

  public final String id;
  private final String clientId;
  private final Driver driver;

  /**
   * Job group constructor.
   *
   * @param driver Driver
   * @param clientId clientId as a string Client identifier
   * @param id passing id as string job group identifier
   */
  public JobGroup(Driver driver, String clientId, String id) {
    this.driver = driver;
    this.clientId = clientId;
    this.id = id;
  }

  /**
   * Edit the JobGroup.
   *
   * @param jobGroup Jobgroup DTO
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws InvalidInputException invalid input provided
   * @throws ApiRequestException something wrong with request
   */
  public void edit(JobGroupDto jobGroup)
      throws UnexpectedResponseException, InvalidInputException, ApiRequestException {

    jobGroup.setJobGroupId(this.id);
    jobGroup.setClientId(this.clientId);
    driver.jobGroupService.edit(driver.session, driver.conf, jobGroup);
  }

  /**
   * Get jobs within a Job Group.
   *
   * @param page page on mojo
   * @param limit limit on mojo
   * @return Jobs
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public List<Job> getJobs(int page, int limit) throws MojoException {
    return driver.jobGroupService.getJobs(
        driver,
        driver.jobService,
        this.clientId,
        this.id,
        page,
        limit,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public List<Job> getJobs(int page, int limit, LocalDate startDate, LocalDate endDate)
      throws MojoException {
    return driver.jobGroupService.getJobs(
        driver, driver.jobService, this.clientId, this.id, page, limit, startDate, endDate);
  }

  /**
   * Get details for a job.
   *
   * @param reqId refNumber
   * @return empty if no jobs, stats of the job otherwise
   * @throws MojoException throws custom mojo exception Something went wrong
   */
  public Optional<JobStats> getJobDetails(String reqId) throws MojoException {
    return driver.jobGroupService.getJobDetails(
        driver,
        driver.jobService,
        this.clientId,
        this.id,
        reqId,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public Optional<JobStats> getJobDetails(String reqId, LocalDate startDate, LocalDate endDate)
      throws MojoException {
    return driver.jobGroupService.getJobDetails(
        driver, driver.jobService, this.clientId, this.id, reqId, startDate, endDate);
  }

  /**
   * Get stats for a jobGroup.
   *
   * @return Stats object
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Stats getStats() throws MojoException {
    return driver.jobGroupService.getStats(
        driver.session,
        driver.conf,
        this.clientId,
        this.id,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public Stats getStats(LocalDate startDate, LocalDate endDate) throws MojoException {
    return driver.jobGroupService.getStats(
        driver.session, driver.conf, this.clientId, this.id, startDate, endDate);
  }

  /**
   * Get stats for Job group cross publisher.
   *
   * @param publisher Publisher object
   * @return Stats object
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Stats getStats(Publisher publisher) throws MojoException {
    return driver.jobGroupService.getStats(
        driver.session,
        driver.conf,
        this.clientId,
        this.id,
        publisher.id,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public Stats getStats(Publisher publisher, LocalDate startDate, LocalDate endDate)
      throws MojoException {
    return driver.jobGroupService.getStats(
        driver.session, driver.conf, this.clientId, this.id, publisher.id, startDate, endDate);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof JobGroup)) {
      return false;
    }

    return this.id.equals(((JobGroup) other).id);
  }
}
