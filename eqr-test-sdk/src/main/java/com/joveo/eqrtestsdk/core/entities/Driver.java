package com.joveo.eqrtestsdk.core.entities;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.mojo.MojoSession;
import com.joveo.eqrtestsdk.core.services.CampaignService;
import com.joveo.eqrtestsdk.core.services.ClientService;
import com.joveo.eqrtestsdk.core.services.JobGroupService;
import com.joveo.eqrtestsdk.core.services.JobService;
import com.joveo.eqrtestsdk.core.services.PublisherService;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidCredentialsException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import com.joveo.eqrtestsdk.models.PublisherDto;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Driver {

  public Session session;
  public Config conf;

  @Inject ClientService clientService;
  @Inject CampaignService campaignService;
  @Inject JobGroupService jobGroupService;
  @Inject PublisherService publisherService;
  @Inject JobService jobService;

  /**
   * Start an instance of Driver.
   *
   * @param username Mojo username
   * @param password Mojo passwords
   * @param JOVEO_ENV Joveo environment
   * @return Driver
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:ParameterName"})
  public static Driver start(String username, String password, JoveoEnvironment JOVEO_ENV)
      throws MojoException {
    Injector injector = Guice.createInjector(new DriverModule());
    Config config = ConfigFactory.load(JOVEO_ENV.toString().toLowerCase() + ".conf");
    Driver driver = injector.getInstance(Driver.class);
    driver.conf = config;
    driver.setup(username, password);
    return driver;
  }

  /**
   * Create a new client.
   *
   * @param client Client DTO
   * @return Client
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Client createClient(ClientDto client)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    String clientId = clientService.create(session, conf, client);
    return new Client(this, clientId);
  }

  /**
   * Create a new campaign.
   *
   * @param campaign Campaign DTO
   * @return Campaign
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Campaign createCampaign(CampaignDto campaign)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    String campaignId = campaignService.create(session, conf, campaign);
    return new Campaign(this, campaign.getClientId(), campaignId);
  }

  /**
   * Create a new job group.
   *
   * @param jobGroup Job group DTO
   * @return JobGroup
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public JobGroup createJobGroup(JobGroupDto jobGroup)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    String jobGroupId = jobGroupService.create(session, conf, jobGroup);
    return new JobGroup(this, jobGroup.getClientId(), jobGroupId);
  }

  /**
   * Create a new.
   *
   * @param publisher Publisher DTO
   * @return Publisher
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public Publisher createPublisher(PublisherDto publisher)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    String publisherID = publisherService.create(session, conf, publisher);
    return new Publisher(this, publisherID);
  }

  public Publisher getExistingPublisher(String publisherId)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    return new Publisher(this, publisherId);
  }

  public Client getExistingClient(String clientId)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    return new Client(this, clientId);
  }

  public Campaign getExistingCampaign(String clientId, String campaignId)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    return new Campaign(this, clientId, campaignId);
  }

  public JobGroup getExistingJobGroup(String clientId, String jobGroupId)
      throws MojoException, UnexpectedResponseException, InvalidInputException,
          ApiRequestException {
    return new JobGroup(this, clientId, jobGroupId);
  }

  private void setup(String username, String password)
      throws UnexpectedResponseException, ApiRequestException, InvalidCredentialsException {
    session = new MojoSession(username, password, this.conf.getString("MojoBaseUrl"));
  }
}
