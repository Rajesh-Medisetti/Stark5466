package com.joveo.eqrtestsdk.core.services;

import static com.joveo.eqrtestsdk.utils.DateUtils.formatAsMojoDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.Job;
import com.joveo.eqrtestsdk.core.models.fetcher.CampaignGetResponse;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.CampaignStats;
import com.joveo.eqrtestsdk.models.EntityStatus;
import com.joveo.eqrtestsdk.models.EntityStatusDto;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.JoveoEntity;
import com.joveo.eqrtestsdk.models.MojoData;
import com.joveo.eqrtestsdk.models.MojoResponse;
import com.joveo.eqrtestsdk.models.PFfields;
import com.joveo.eqrtestsdk.models.PfOperators;
import com.joveo.eqrtestsdk.models.PlatformFiltersDto;
import com.joveo.eqrtestsdk.models.Stats;
import com.joveo.eqrtestsdk.models.validationgroups.EditCampaign;
import com.typesafe.config.Config;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CampaignService extends BaseService {

  private static Logger logger = LoggerFactory.getLogger(CampaignService.class);

  @Inject
  public CampaignService(JoveoHttpExecutor executor, Validator validator) {
    this.executor = executor;
    this.validator = validator;
  }

  /**
   * creating a campaign.
   *
   * @param session session details
   * @param conf configuration details
   * @param campaign Campaign DTO
   * @return String
   * @throws InvalidInputException invalid input provided
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   */
  public String create(Session session, Config conf, CampaignDto campaign, boolean validation)
      throws InvalidInputException, UnexpectedResponseException, ApiRequestException {

    campaign.setDefaultDates();

    if (validation) {
      String validationErrors = validateEntity(campaign, validator);
      if (validationErrors != null) {
        logger.error(validationErrors);
        throw new InvalidInputException(validationErrors);
      }
    }

    RestResponse response =
        executor.post(session, conf.getString("MojoBaseUrl") + "/thor/api/campaigns", campaign);

    if (!response.isSuccess()) {
      String errorMessage = "Unable to Create Campaign: " + response.getJoveoErrorMessage();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    MojoResponse mojoResponse = response.toEntityWithData(MojoResponse.class);

    try {
      return mojoResponse.getFirstData().getId();
    } catch (IndexOutOfBoundsException e) {
      logger.error(e.getMessage());
      throw new UnexpectedResponseException(
          "data at first index missing in response " + response.toString());
    }
  }

  /**
   * edit campaign.
   *
   * @param session session details
   * @param conf configuration details
   * @param campaign Campaign DTO
   * @return String
   * @throws InvalidInputException invalid input provided
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   */
  public String edit(Session session, Config conf, CampaignDto campaign)
      throws InvalidInputException, UnexpectedResponseException, ApiRequestException {

    RestResponse getResponse =
        executor.get(
            session,
            conf.getString("MojoBaseUrl")
                + "/flash/api/campaigns/"
                + campaign.getCampaignId()
                + "?clientIds="
                + campaign.getClientId()
                + "&campaignIds="
                + campaign.getCampaignId());

    String errorMessage = "Unable to make getCampaign Request , check clientId,campaignId";
    checkGetResponse(getResponse, errorMessage);

    String validationErrors = this.validatEditEntity(campaign, validator);

    List<CampaignGetResponse> fields = getResponseData(getResponse);
    CampaignGetResponse responseData = fields.get(0);

    LocalDate endDate = campaign.getEndDate();
    String startDate = responseData.getStartDate();

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

    copyNameEndDateBudgetCap(campaign, responseData);

    RestResponse response =
        executor.put(
            session,
            conf.getString("MojoBaseUrl") + "/thor/api/campaigns/" + campaign.getCampaignId(),
            campaign);

    errorMessage = "Unable to edit Campaign: ";
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

  private void copyNameEndDateBudgetCap(CampaignDto campaign, CampaignGetResponse responseData) {

    if (campaign.getName() == null) {
      campaign.setName(responseData.getName());
    }

    if (campaign.getEndDate() == null) {
      campaign.setEndDate(
          LocalDate.parse(responseData.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    }

    if (campaign.getBudgetCap() == null) {
      if (responseData.getBudgetCap() != null) {
        campaign.setBudget(responseData.getBudgetCap().getValue());
      }
    }
  }

  /**
   * get response data.
   *
   * @param response rest response
   * @return list of CampaignGetResponse
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<CampaignGetResponse> getResponseData(RestResponse response)
      throws UnexpectedResponseException {

    List<CampaignGetResponse> dataList = new ArrayList<>();
    MojoResponse<CampaignGetResponse> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<CampaignGetResponse>>() {});

    for (MojoData<CampaignGetResponse> data : mojoResponse.getData()) {
      dataList.add(data.getFields());
    }
    return dataList;
  }

  /**
   * validate edit entity.
   *
   * @param entity entity field
   * @param validator Validator object
   * @param <T> generic param
   * @return generic
   */
  public <T> String validatEditEntity(T entity, Validator validator) {

    Set<ConstraintViolation<T>> constraintViolations =
        validator.validate(entity, EditCampaign.class);
    return validationMessages(constraintViolations);
  }

  /**
   * fetching campaigns.
   *
   * @param driver Instance of driver
   * @param clientId clientId as a string
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return list of campaign
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<Campaign> getCampaigns(
      Driver driver, String clientId, LocalDate startDate, LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    List<Campaign> campaigns = new ArrayList<>();
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);

    RestResponse response =
        executor.post(
            driver.session,
            driver.conf.getString("MojoBaseUrl") + "/flash/api/campaigns",
            platformFiltersDto);

    if (!response.isSuccess()) {
      logger.error("failed to get campaigns " + response.getJoveoErrorMessage());
      throw new UnexpectedResponseException(
          "failed to get campaigns " + response.getJoveoErrorMessage());
    }
    MojoResponse<Stats> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<Stats>>() {});

    for (MojoData<Stats> data : mojoResponse.getData()) {
      campaigns.add(new Campaign(driver, clientId, data.getFields().getId()));
    }
    return campaigns;
  }

  /**
   * get job with pagination and limit.
   *
   * @param driver Instance of driver
   * @param jobService Instance of Job service
   * @param clientId clientId as a string
   * @param entityId joveo entity Id
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
      String entityId,
      int page,
      int limit,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto = enrichFiltersWithEntityId(platformFiltersDto, entityId);
    return jobService.getJobs(
        driver, platformFiltersDto, clientId, page, limit, startDate, endDate);
  }

  /**
   * getting details of a job.
   *
   * @param driver Instance of driver
   * @param jobService Instance of Job service
   * @param clientId clientId as a string
   * @param entityId joveo entity Id
   * @param reqId Ref number
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return optional
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public Optional<JobStats> getJobDetails(
      Driver driver,
      JobService jobService,
      String clientId,
      String entityId,
      String reqId,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto = enrichFiltersWithEntityId(platformFiltersDto, entityId);
    return jobService.getJobDetails(
        driver.session, driver.conf, platformFiltersDto, clientId, reqId, startDate, endDate);
  }

  @Override
  public String getEntity() {
    return JoveoEntity.campaigns.toString();
  }

  @Override
  public TypeReference getMojoStatsTypeReference() {
    return new TypeReference<MojoResponse<CampaignStats>>() {};
  }

  @Override
  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String campaignId) {
    platformFiltersDto.addRule(campaignId, PFfields.campaignId, PfOperators.IN);
    return platformFiltersDto;
  }

  @Override
  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String clientId, String campaignId) {
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);
    platformFiltersDto.addRule(campaignId, PFfields.entityId, PfOperators.IN);
    return platformFiltersDto;
  }

  /**
   * change campaign status.
   *
   * @param session session
   * @param config configuration
   * @param clientId client id
   * @param campaignId campaign id
   * @param status status
   * @throws ApiRequestException api exception
   * @throws UnexpectedResponseException response exception
   */
  public void changeCampaignStatus(
      Session session, Config config, String clientId, String campaignId, EntityStatus status)
      throws ApiRequestException, UnexpectedResponseException {
    EntityStatusDto entityStatusDto = new EntityStatusDto();
    entityStatusDto.addCampaignId(campaignId);
    entityStatusDto.addClientId(clientId);

    if (status.equals(EntityStatus.enable)) {
      entityStatusDto.addStatus("A");
    } else {
      entityStatusDto.addStatus("P");
    }

    RestResponse response =
        executor.put(
            session, config.getString("MojoBaseUrl") + "/thor/api/campaigns", entityStatusDto);

    if (!response.isSuccess()) {
      String errorMessage = "failed to " + status + " campaign" + response.getJoveoErrorMessage();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
    logger.info("Campaign " + status + " successfully");
  }
}
