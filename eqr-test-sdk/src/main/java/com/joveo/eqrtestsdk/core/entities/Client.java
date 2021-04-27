package com.joveo.eqrtestsdk.core.entities;

import static com.joveo.eqrtestsdk.utils.DateUtils.startOfMonth;

import com.joveo.eqrtestsdk.core.mojo.OutboundFeed;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.Stats;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Client {
  public final String id;
  private final Driver driver;

  public Client(Driver driver, String id) {
    this.id = id;
    this.driver = driver;
  }

  public void runScheduler() throws MojoException {
    driver.clientService.runScheduler(driver.session, driver.conf, this.id);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Client)) {
      return false;
    }

    return this.id.equals(((Client) other).id);
  }

  public void removeClient() throws MojoException {
    driver.clientService.removeClient(driver.session, driver.conf, this.id);
  }

  public Campaign getDefaultCampaign() {
    return new Campaign(driver, this.id, this.id + "$campaign$default");
  }

  public JobGroup getDefaultJobGroup() {
    return new JobGroup(driver, this.id, this.id + "$jobgroup$default");
  }

  public List<Campaign> getCampaigns() throws MojoException {
    return driver.campaignService.getCampaigns(
        driver, this.id, startOfMonth(LocalDate.now()), LocalDate.now());
  }

  public List<JobGroup> getJobGroups() throws MojoException {
    return driver.jobGroupService.getJobGroups(
        driver, this.id, startOfMonth(LocalDate.now()), LocalDate.now());
  }

  /**
   * Get jobs for a client.
   *
   * @param page page numebr
   * @param limit limit on mojo
   * @return List of Jobs
   * @throws MojoException throws custom mojo exception Something went wrong
   */
  public List<Job> getJobs(int page, int limit) throws MojoException {
    return driver.clientService.getJobs(
        driver,
        driver.jobService,
        this.id,
        page,
        limit,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public List<Job> getJobs(int page, int limit, LocalDate startDate, LocalDate endDate)
      throws MojoException {
    return driver.clientService.getJobs(
        driver, driver.jobService, this.id, page, limit, startDate, endDate);
  }

  public Optional<JobStats> getJobDetails(String reqId) throws MojoException {
    return driver.clientService.getJobDetails(
        driver, driver.jobService, this.id, reqId, startOfMonth(LocalDate.now()), LocalDate.now());
  }

  public Optional<JobStats> getJobDetails(String reqId, LocalDate startDate, LocalDate endDate)
      throws MojoException {
    return driver.clientService.getJobDetails(
        driver, driver.jobService, this.id, reqId, startDate, endDate);
  }

  public Stats getStats() throws MojoException {
    return driver.clientService.getStats(
        driver.session, driver.conf, this.id, startOfMonth(LocalDate.now()), LocalDate.now());
  }

  public Stats getStats(LocalDate startDate, LocalDate endDate) throws MojoException {
    return driver.clientService.getStats(driver.session, driver.conf, this.id, startDate, endDate);
  }

  /**
   * Stats for Client cross Publisher.
   *
   * @param publisher publisher object
   * @return Stats object
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Stats getStats(Publisher publisher) throws MojoException {
    return driver.clientService.getStats(
        driver.session,
        driver.conf,
        this.id,
        publisher.id,
        startOfMonth(LocalDate.now()),
        LocalDate.now());
  }

  public Stats getStats(Publisher publisher, LocalDate startDate, LocalDate endDate)
      throws MojoException {
    return driver.clientService.getStats(
        driver.session, driver.conf, this.id, publisher.id, startDate, endDate);
  }

  public List<String> getInboundFeeds() throws ApiRequestException, UnexpectedResponseException {
    return driver.clientService.getInboundFeedData(this.id, driver.session, driver.conf);
  }

  public OutboundFeed getOutboundFeed(String publisherId) throws MojoException {
    return driver.clientService.getOutboundFeedData(
        publisherId, this.id, driver.session, driver.conf);
  }

  /**
   * Edit a client.
   *
   * @param clientDto ClientDto
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  public void edit(ClientDto clientDto)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    clientDto.setClientId(this.id);
    driver.clientService.edit(driver.session, driver.conf, clientDto);
  }
}
