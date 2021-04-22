package com.joveo.eqrtestsdk.core.entities;

import static com.joveo.eqrtestsdk.utils.DateUtils.startOfMonth;

import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.Stats;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Campaign {
  public final String id;
  private final String clientId;
  private final Driver driver;

  /**
   * Campaign facade to interact with Mojo Campaigns.
   *
   * @param driver Instance of driver
   * @param clientId clientId as a string Client identifier
   * @param id passing id as string Campaign identifier
   */
  public Campaign(Driver driver, String clientId, String id) {
    this.driver = driver;
    this.clientId = clientId;
    this.id = id;
  }

  /**
   * Edits the campaign.
   *
   * @param campaign Campaign DTO
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  public void edit(CampaignDto campaign)
      throws MojoException, UnexpectedResponseException, ApiRequestException,
          InvalidInputException {
    campaign.setCampaignId(this.id);
    campaign.setClientId(this.clientId);
    driver.campaignService.edit(driver.session, driver.conf, campaign);
  }

  public List<JobGroup> getJobGroups() throws MojoException {
    return driver.jobGroupService.getJobGroups(
        driver, this.clientId, this.id, startOfMonth(LocalDate.now()), LocalDate.now());
  }

  /**
   * Get jobs within the campaign.
   *
   * @param page page number on Mojo
   * @param limit limit within the page
   * @return List of Jobs.
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public List<Job> getJobs(int page, int limit) throws MojoException {
    return driver.campaignService.getJobs(
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
    return driver.campaignService.getJobs(
        driver, driver.jobService, this.clientId, this.id, page, limit, startDate, endDate);
  }

  /**
   * Job details for a given Job.
   *
   * @param reqId Ref number
   * @return Optional.empty if job not found, else job details
   * @throws MojoException throws custom mojo exception Something went wrong
   */
  public Optional<JobStats> getJobDetails(String reqId) throws MojoException {
    return driver.campaignService.getJobDetails(
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
    return driver.campaignService.getJobDetails(
        driver, driver.jobService, this.clientId, this.id, reqId, startDate, endDate);
  }

  /**
   * Get stats for the campaign.
   *
   * @return Stats object
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Stats getStats() throws MojoException {
    return driver.campaignService.getStats(
        driver.session,
        driver.conf,
        this.clientId,
        this.id,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public Stats getStats(LocalDate startDate, LocalDate endDate) throws MojoException {
    return driver.campaignService.getStats(
        driver.session, driver.conf, this.clientId, this.id, startDate, endDate);
  }

  /**
   * Get stats for the campaign cross Publisher.
   *
   * @param publisher Publisher object
   * @return Stats object
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Stats getStats(Publisher publisher) throws MojoException {
    return driver.campaignService.getStats(
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
    return driver.campaignService.getStats(
        driver.session, driver.conf, this.clientId, this.id, publisher.id, startDate, endDate);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Campaign)) {
      return false;
    }
    return this.id.equals(((Campaign) other).id);
  }
}
