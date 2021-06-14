package com.joveo.eqrtestsdk.core.services;

import static com.joveo.eqrtestsdk.utils.DateUtils.formatAsMojoDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.Job;
import com.joveo.eqrtestsdk.core.models.fetcher.ClientGetResponse;
import com.joveo.eqrtestsdk.core.models.fetcher.ClientPostResponse;
import com.joveo.eqrtestsdk.core.models.fetcher.Pixels;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.OutboundFeed;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.core.mojo.SchedulerRunner;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.ClientStats;
import com.joveo.eqrtestsdk.models.FeedUrl;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.JoveoEntity;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import com.joveo.eqrtestsdk.models.MarkUp;
import com.joveo.eqrtestsdk.models.MojoData;
import com.joveo.eqrtestsdk.models.MojoResponse;
import com.joveo.eqrtestsdk.models.PFfields;
import com.joveo.eqrtestsdk.models.PfOperators;
import com.joveo.eqrtestsdk.models.PlatformFiltersDto;
import com.joveo.eqrtestsdk.models.Stats;
import com.joveo.eqrtestsdk.models.validationgroups.EditClient;
import com.typesafe.config.Config;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService extends BaseService {

  private static Logger logger = LoggerFactory.getLogger(ClientService.class);
  private SchedulerService schedulerService;

  /**
   * client service constructor.
   *
   * @param executor Http executor
   * @param schedulerService Instance of scheduler service
   * @param validator Validator object
   * @param objectMapper ObjectMapper instance
   */
  @Inject
  public ClientService(
      JoveoHttpExecutor executor,
      SchedulerService schedulerService,
      Validator validator,
      ObjectMapper objectMapper) {
    this.executor = executor;
    this.schedulerService = schedulerService;
    this.validator = validator;
    this.objectMapper = objectMapper;
  }

  public String entityType() {
    return "Client";
  }

  /**
   * running scheduler.
   *
   * @param session session details
   * @param conf configuration details
   * @param clientId clientId as a string
   * @param timeout duration timeout
   * @throws TimeoutException timeout exception
   * @throws InterruptWaitException Interrupt wait Exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws MojoException throws custom mojo exception
   */
  public void runScheduler(Session session, Config conf, String clientId, Duration timeout)
      throws TimeoutException, InterruptWaitException, ApiRequestException,
          UnexpectedResponseException, MojoException {
    new SchedulerRunner(
            clientId,
            conf.getString("MojoBaseUrl"),
            session,
            this.schedulerService,
            timeout,
            conf.getDuration("SchedulerRefreshInterval"))
        .run();
  }

  public void runScheduler(Session session, Config config, String clientId)
      throws TimeoutException, InterruptWaitException, ApiRequestException,
          UnexpectedResponseException, MojoException {
    this.runScheduler(session, config, clientId, config.getDuration("SchedulerTimeout"));
  }

  /**
   * . This method returns a list of client ids for which the client name contains given
   * searchString.
   *
   * @param session session
   * @param config config
   * @param searchString searchString
   * @return List of client ids.
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException On unexpected Response
   */
  public List<String> getClientIdsFromSearch(Session session, Config config, String searchString)
      throws ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto.addRule(
        formatAsMojoDate(LocalDate.now().minusYears(10)), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(
        formatAsMojoDate(LocalDate.now()), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(searchString, PFfields.name, PfOperators.CONTAINS);

    RestResponse response =
        executor.post(
            session, config.getString("MojoBaseUrl") + "/flash/api/clients", platformFiltersDto);

    if (response.getResponseCode() != 200) {
      String errorMessage =
          "Unable to make POST API call to fetch client Ids of given search pattern"
              + ", status code "
              + response.getResponseCode();

      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    List<String> clientIds = new ArrayList<>();
    List<ClientPostResponse> fields = getPostResponseData(response);
    for (ClientPostResponse field : fields) {
      if (field.getStatus().equals("A")) {
        clientIds.add(field.getId());
      }
    }
    return clientIds;
  }

  /**
   * populating feed.
   *
   * @param session session details
   * @param config config details
   * @param feed Instance of feeds object
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public void populateFeed(Session session, Config config, ClientDto.ClientParams.Feeds feed)
      throws ApiRequestException, UnexpectedResponseException {

    RestResponse response =
        executor.post(session, config.getString("FeedMappingUrl"), new FeedUrl(feed.xmlFeedUrl));

    if (response.getResponseCode() != 200) {
      String errorMessage =
          "Invalid Feed,"
              + response.toJsonNode("message").textValue()
              + ", status code "
              + response.getResponseCode();

      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    JsonNode expectedMapping = response.toJsonNode("expected_mapping");

    Iterator<String> fieldNames = expectedMapping.fieldNames();

    while (fieldNames.hasNext()) {
      String name = fieldNames.next();

      String value = expectedMapping.get(name).textValue();

      switch (value) {
        case "source":
          feed.schemaMappings.schemaMappingsJobCollection = name;
          break;
        case "job":
          feed.schemaMappings.schemaMappingsJob = name;
          break;
        case "title":
          feed.schemaMappings.schemaMappingsTitle = name;
          break;
        case "description":
          feed.schemaMappings.schemaMappingsDescription = name;
          break;
        case "url":
          feed.schemaMappings.schemaMappingsUrl = name;
          break;
        case "referencenumber":
          feed.schemaMappings.schemaMappingsRefNumber = name;
          break;
        case "company":
          feed.schemaMappings.schemaMappingsCompany = name;
          break;
        case "city":
          feed.schemaMappings.schemaMappingsCity = name;
          break;
        case "category":
          feed.schemaMappings.schemaMappingsCategory = name;
          break;
        case "pubDate":
          feed.schemaMappings.schemaMappingsPublishedDate = name;
          break;
        case "lastmodifieddate":
          feed.schemaMappings.schemaMappingsModifiedDate = name;
          break;
        case "zip":
          feed.schemaMappings.schemaMappingsZip = name;
          break;
        case "date":
          feed.schemaMappings.schemaMappingsDatePosted = name;
          break;
        case "country":
          feed.schemaMappings.schemaMappingsCountry = name;
          break;
        case "state":
          feed.schemaMappings.schemaMappingsState = name;
          break;
        case "cpc":
          feed.schemaMappings.schemaMappingsCpcBid = name;
          break;
        case "type":
          feed.schemaMappings.schemaMappingsType = name;
          break;
        default:
      }
    }
  }

  /**
   * creating client.
   *
   * @param session session details
   * @param conf configuration details
   * @param client client Dto
   * @return string
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public String create(Session session, Config conf, ClientDto client, boolean validation)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    client.setDefaultValues();

    for (ClientDto.ClientParams.Feeds feed : client.getFeeds()) {
      populateFeed(session, conf, feed);
    }

    if (validation) {
      String validationErrors = validateEntity(client, validator);
      if (validationErrors != null) {
        logger.error(validationErrors);
        throw new InvalidInputException(validationErrors);
      }
    }

    RestResponse response =
        executor.post(session, conf.getString("MojoBaseUrl") + "/thor/api/clients", client);

    if (!response.isSuccess()) {
      String errorMessage =
          "Unable to create Client: "
              + response.getJoveoErrorMessage()
              + " status code: "
              + response.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    MojoResponse mojoResponse = response.toEntityWithData(MojoResponse.class);

    try {
      return mojoResponse.getFirstData().getId();
    } catch (IndexOutOfBoundsException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException("data at first index missing in response " + response);
    }
  }

  /** . */
  public Pixels getPixels(String clientId, Session session, Config config)
      throws UnexpectedResponseException, ApiRequestException {

    RestResponse getResponse =
        executor.get(
            session,
            config.getString("MojoBaseUrl") + "/api/clients/conversions/v3?clientID=" + clientId);

    checkGetResponse(getResponse, "Could not fetch pixels for clientId -" + clientId);

    return getResponse.toEntityWithData(Pixels.class);
  }

  /**
   * edit client.
   *
   * @param session session details
   * @param conf configuration details
   * @param client client Dto
   * @return string
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  public String edit(Session session, Config conf, ClientDto client)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    RestResponse getResponse =
        executor.get(
            session, conf.getString("MojoBaseUrl") + "/flash/api/clients/" + client.getClientId());

    String errorMessage = "Unable to make getClient Request , check clientId ";

    checkGetResponse(getResponse, errorMessage);

    List<ClientGetResponse> fields = this.getResponseData(getResponse);
    ClientGetResponse getResponseData = fields.get(0);

    List<ClientGetResponse.Feeds> feeds = getResponseData.getFeeds();

    populateAndDeleteFeeds(feeds, client, session, conf);

    String validationErrors = this.validateEditEntity(client, validator);

    LocalDate endDate = client.getEndDate();
    String startDate = getResponseData.getStartDate();

    if (!isValidDate(startDate, endDate)) {
      if (validationErrors != null) {
        validationErrors += " Invalid endDate, startDate is " + startDate + ", ";
      } else {
        validationErrors = "Invalid endDate, startDate is " + startDate + ", ";
      }
    }

    if (validationErrors != null) {
      logger.error(validationErrors);
      throw new InvalidInputException(validationErrors);
    }

    RestResponse response =
        executor.put(
            session,
            conf.getString("MojoBaseUrl") + "/thor/api/clients/" + client.getClientId(),
            client);

    errorMessage = "Unable to edit Client: ";
    checkUpdateResponse(response, errorMessage);

    MojoResponse mojoResponse = response.toEntityWithData(MojoResponse.class);

    try {
      return mojoResponse.getFirstData().getId();
    } catch (IndexOutOfBoundsException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "data at first index missing in response " + response.toString());
    }
  }

  private void populateAndDeleteFeeds(
      List<ClientGetResponse.Feeds> feeds, ClientDto client, Session session, Config conf)
      throws ApiRequestException, UnexpectedResponseException, InvalidInputException {

    if (client.getFeeds() != null) {
      for (ClientDto.ClientParams.Feeds feed : client.getFeeds()) {
        if (!feed.deleted) {
          populateFeed(session, conf, feed);
        } else {
          String feedId = getFeedId(feeds, feed);
          if (feedId == null) {
            String errorMessage = "Feed " + feed.xmlFeedUrl + " to be deleted is not in Client";
            logger.error(errorMessage);
            throw new InvalidInputException(errorMessage);
          } else {
            feed.id = feedId;
          }
        }
      }
    }
  }

  /**
   * getting response data.
   *
   * @param response rest response
   * @return list of clientGetResponse
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<ClientGetResponse> getResponseData(RestResponse response)
      throws UnexpectedResponseException {

    List<ClientGetResponse> dataList = new ArrayList<>();
    MojoResponse<ClientGetResponse> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<ClientGetResponse>>() {});

    for (MojoData<ClientGetResponse> data : mojoResponse.getData()) {
      dataList.add(data.getFields());
    }
    return dataList;
  }

  private List<ClientPostResponse> getPostResponseData(RestResponse response)
      throws UnexpectedResponseException {

    List<ClientPostResponse> dataList = new ArrayList<>();
    MojoResponse<ClientPostResponse> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<ClientPostResponse>>() {});

    for (MojoData<ClientPostResponse> data : mojoResponse.getData()) {
      dataList.add(data.getFields());
    }
    return dataList;
  }

  /**
   * get feed id.
   *
   * @param feeds Instance of feeds objects
   * @param inputFeed Instance of Feeds object
   * @return String
   */
  public String getFeedId(
      List<ClientGetResponse.Feeds> feeds, ClientDto.ClientParams.Feeds inputFeed) {

    for (ClientGetResponse.Feeds feed : feeds) {

      if (feed.deleted != null && !feed.deleted && feed.xmlFeedUrl.equals(inputFeed.xmlFeedUrl)) {
        return feed.id;
      }
    }
    return null;
  }

  /** . Setting MarkUp at Client Level */
  public void setMarkUp(MarkUp markUp, Session session, Config config)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    markUp.setAgencyId(session.getInstanceIdentifier());

    String validationErrors = validateEntity(markUp, validator);
    if (validationErrors != null) {
      logger.error(validationErrors);
      throw new InvalidInputException(validationErrors);
    }

    RestResponse response =
        executor.put(session, config.getString("MojoBaseUrl") + "/api/markup/save", markUp);

    Boolean success = response.extractByKeyWithoutData("success");

    if (!Boolean.TRUE.equals(success)) {
      String errorMessage =
          "Unable to update Markup in Client: "
              + response
              + " status code: "
              + response.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
  }

  /**
   * set client status inactive.
   *
   * @param session session details
   * @param config config details
   * @param clientId clientId as a string
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public void removeClient(Session session, Config config, String clientId)
      throws ApiRequestException, UnexpectedResponseException {
    ClientDto clientDto = new ClientDto();
    List<String> clientIds = new ArrayList<>();
    List<String> status = new ArrayList<>();

    clientIds.add(clientId);
    status.add("I");

    clientDto.setClientIds(clientIds);
    clientDto.setStatus(status);

    RestResponse response =
        executor.put(session, config.getString("MojoBaseUrl") + "/thor/api/clients", clientDto);

    if (!response.isSuccess()) {
      String errorMessage =
          "failed to remove client"
              + response.getJoveoErrorMessage()
              + " status code: "
              + response.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    logger.info("Client removed successfully");
  }

  /**
   * validation.
   *
   * @param entity entity field
   * @param validator Validator object
   * @param <T> generic param generic param
   * @return generics
   */
  public <T> String validateEditEntity(T entity, Validator validator) {

    Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity, EditClient.class);

    return validationMessages(constraintViolations);
  }

  public Stats getStats(
      Session session, Config config, String clientId, LocalDate startDate, LocalDate endDate)
      throws MojoException, UnexpectedResponseException, ApiRequestException {
    return super.getStats(session, config, clientId, clientId, startDate, endDate);
  }

  public Stats getStats(
      Session session,
      Config config,
      String clientId,
      String entityId,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, UnexpectedResponseException, ApiRequestException {
    return super.getStats(session, config, clientId, clientId, entityId, startDate, endDate);
  }

  /**
   * get client jobs.
   *
   * @param driver Instance of driver
   * @param jobService Instance of Job service
   * @param clientId clientId as a string
   * @param page page number on Mojo
   * @param limit limit within the page
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return list of job
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<Job> getJobs(
      Driver driver,
      JobService jobService,
      String clientId,
      int page,
      int limit,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto = enrichFiltersWithEntityId(platformFiltersDto, clientId);
    return jobService.getJobs(
        driver, platformFiltersDto, clientId, page, limit, startDate, endDate);
  }

  /**
   * get job detail.
   *
   * @param driver Instance of driver
   * @param jobService Instance of Job service
   * @param clientId clientId as a string
   * @param reqId Ref number
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return optional of job stats
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public Optional<JobStats> getJobDetails(
      Driver driver,
      JobService jobService,
      String clientId,
      String reqId,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto = enrichFiltersWithEntityId(platformFiltersDto, clientId);
    return jobService.getJobDetails(
        driver.session, driver.conf, platformFiltersDto, clientId, reqId, startDate, endDate);
  }

  private String getmd5Hash(String name) {
    return DigestUtils.md5Hex(name).toLowerCase().substring(0, 8);
  }

  private String getOutboundFeedUrl(String publisherId, String clientId, String agencyName) {
    String bucketName =
        "joveo-"
            + getmd5Hash(
                JoveoEnvironment.Staging.toString().toLowerCase()
                    + "-"
                    + agencyName
                    + "-xml"); // 116ccdfb
    String key = getmd5Hash(publisherId) + "/" + clientId + ".xml";
    return "https://" + bucketName + ".s3-accelerate.amazonaws.com/" + key;
  }

  /**
   * To fetch Inbound Feeds of a client.
   *
   * @param clientId ClientId which uniquely identifies client
   * @param session Session Details
   * @param conf Configuration Details
   * @return List of inbound feeds
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException Something wrong with request
   */
  public List<String> getInboundFeedData(String clientId, Session session, Config conf)
      throws UnexpectedResponseException, ApiRequestException {
    RestResponse getResponse =
        executor.get(session, conf.getString("MojoBaseUrl") + "/flash/api/clients/" + clientId);

    if (!getResponse.isSuccess()) {
      String errorMessage =
          "Unable to make getClient Request , check clientId "
              + getResponse
              + " Status code: "
              + getResponse.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    List<ClientGetResponse> fields = this.getResponseData(getResponse);
    List<String> feedUrls = new ArrayList<>();

    if (fields.isEmpty()) {
      return feedUrls;
    }

    ClientGetResponse getResponseData = fields.get(0);

    for (ClientGetResponse.Feeds feed : getResponseData.getFeeds()) {
      if (!feed.deleted) {
        feedUrls.add(feed.xmlFeedUrl);
      }
    }
    return feedUrls;
  }

  /**
   * get outbound feed data.
   *
   * @param publisherId Mojo publisher Id
   * @param clientId clientId as a string
   * @param session session details
   * @param conf configuration details
   * @return outbound feed
   * @throws InvalidInputException invalid input provided
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   */
  @Deprecated
  public OutboundFeed getOutboundFeedData(
      String publisherId, String clientId, Session session, Config conf)
      throws InvalidInputException, UnexpectedResponseException, ApiRequestException {
    String agencyName = session.getInstanceIdentifier();
    String outboundFeedUrl = getOutboundFeedUrl(publisherId, clientId, agencyName);
    return new OutboundFeed(outboundFeedUrl, clientId, schedulerService, session, conf);
  }

  /**
   * get outbound feed data.
   *
   * @param publisherId Mojo publisher Id
   * @param clientId clientId as a string
   * @param session session details
   * @param conf configuration details
   * @return outbound feed
   * @throws InvalidInputException invalid input provided
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   */
  public OutboundFeed getOutboundFeedData(
      String publisherId, String clientId, Session session, Config conf, boolean isCombined)
      throws InvalidInputException, UnexpectedResponseException, ApiRequestException {
    String agencyName = session.getInstanceIdentifier();
    String outboundFeedUrl = getOutboundFeedUrl(publisherId, clientId, agencyName);
    //will check the isCombined
    return new OutboundFeed(outboundFeedUrl, clientId, schedulerService, session, conf, isCombined);
  }

  @Override
  public String getEntity() {
    return JoveoEntity.clients.toString();
  }

  @Override
  public TypeReference getMojoStatsTypeReference() {
    return new TypeReference<MojoResponse<ClientStats>>() {};
  }

  @Override
  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String clientId, String id) {
    platformFiltersDto.addRule(clientId, PFfields.entityId, PfOperators.IN);
    return platformFiltersDto;
  }
}
