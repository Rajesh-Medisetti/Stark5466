package com.joveo.eqrtestsdk.core.entities;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.mojo.MojoSession;
import com.joveo.eqrtestsdk.core.mojo.TrafficGenerator;
import com.joveo.eqrtestsdk.core.services.AwsService;
import com.joveo.eqrtestsdk.core.services.CacheRefreshService;
import com.joveo.eqrtestsdk.core.services.CampaignService;
import com.joveo.eqrtestsdk.core.services.ClientService;
import com.joveo.eqrtestsdk.core.services.DatabaseService;
import com.joveo.eqrtestsdk.core.services.FeedService;
import com.joveo.eqrtestsdk.core.services.JobGroupService;
import com.joveo.eqrtestsdk.core.services.JobService;
import com.joveo.eqrtestsdk.core.services.PublisherService;
import com.joveo.eqrtestsdk.core.services.TrackingService;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.InvalidCredentialsException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import com.joveo.eqrtestsdk.models.PublisherDto;
import com.joveo.eqrtestsdk.models.clickmeterevents.StatsRequest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.List;

public class Driver {

  public Session session;
  public Config conf;

  @Inject ClientService clientService;
  @Inject CampaignService campaignService;
  @Inject JobGroupService jobGroupService;
  @Inject PublisherService publisherService;
  @Inject JobService jobService;
  @Inject FeedService feedService;
  @Inject AwsService awsService;
  @Inject CacheRefreshService cacheRefreshService;
  @Inject TrackingService trackingService;
  @Inject DatabaseService databaseService;

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
    driver.databaseService.setup(
        config.getString("MongoHost_Name"),
        config.getString("MongoDB_Name"),
        config.getString("MongoCollection_Name"),
        config.getString("Mongo_Username"),
        config.getString("Mongo_Password"));
    driver.trackingService.setup(config.getString("RedisUrl"), config.getString("RedisMapName"));
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

  public String generateInboundFeed(FeedDto feedDto) throws InvalidInputException {
    return feedService.getFeedUrl(conf, awsService, feedDto);
  }

  public void deleteInboundFeed(String feedUrl) throws InvalidInputException {
    feedService.deleteFeedUrl(conf, awsService, feedUrl);
  }

  /**
   * This method will generate click and apply stats for the a given list of stats requests.
   *
   * @param statsRequests List of Stats Requests
   * @throws MojoException throws custom mojo exception Something went wrong
   * @throws InterruptWaitException on Interrupt
   */
  public void generateStats(List<StatsRequest> statsRequests)
      throws MojoException, InterruptWaitException {

    TrafficGenerator trafficGenerator =
        new TrafficGenerator(session, conf, awsService, trackingService, databaseService);

    trafficGenerator.run(statsRequests);
  }

  public void refreshJobCount() throws ApiRequestException, UnexpectedResponseException {
    cacheRefreshService.refreshCache(
        session, awsService, conf.getString("InstanceTag"), conf.getString("FlashRefreshUrl"));
  }

  public void refreshEntityCache() throws ApiRequestException, UnexpectedResponseException {
    cacheRefreshService.refreshCache(
        session, awsService, conf.getString("InstanceTag"), conf.getString("AragonRefreshUrl"));
  }

  private void setup(String username, String password)
      throws UnexpectedResponseException, ApiRequestException, InvalidCredentialsException {
    session = new MojoSession(username, password, this.conf.getString("MojoBaseUrl"));
  }
}
