package com.joveo.eqrtestsdk.core.services;

import static com.joveo.eqrtestsdk.utils.DateUtils.formatAsMojoDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.Job;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.core.models.fetcher.JobGroupGetResponse;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.CapDto;
import com.joveo.eqrtestsdk.models.Freq;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.JoveoEntity;
import com.joveo.eqrtestsdk.models.MojoData;
import com.joveo.eqrtestsdk.models.MojoResponse;
import com.joveo.eqrtestsdk.models.PFfields;
import com.joveo.eqrtestsdk.models.PfOperators;
import com.joveo.eqrtestsdk.models.PlatformFiltersDto;
import com.joveo.eqrtestsdk.models.Stats;
import com.joveo.eqrtestsdk.models.validationgroups.EditJobGroup;
import com.joveo.eqrtestsdk.models.validationgroups.JobGroupCap;
import com.typesafe.config.Config;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobGroupService extends BaseService {

  private static final Logger logger = LoggerFactory.getLogger(JobGroupService.class);

  @Inject
  public JobGroupService(JoveoHttpExecutor executor, Validator validator) {
    this.executor = executor;
    this.validator = validator;
  }

  /**
   * creating job group.
   *
   * @param session session details
   * @param conf configuration details
   * @param jobGroup Job group DTO
   * @return string
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws ApiRequestException something wrong with request
   * @throws InvalidInputException invalid input provided
   */
  public String create(Session session, Config conf, JobGroupDto jobGroup)
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    jobGroup.setDefaultValues();

    String validationErrors = this.validateEntity(jobGroup, validator);
    if (validationErrors != null) {
      throw new InvalidInputException(validationErrors);
    }

    RestResponse response =
        executor.post(session, conf.getString("MojoBaseUrl") + "/thor/api/jobgroups", jobGroup);

    if (!response.isSuccess()) {
      String errorMessage = "Unable to create Job Group: " + response.getJoveoErrorMessage();
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

  /**
   * edit job group.
   *
   * @param session session details
   * @param config config details
   * @param jobGroup Job group DTO
   * @return string
   * @throws InvalidInputException invalid input provided
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public String edit(Session session, Config config, JobGroupDto jobGroup)
      throws InvalidInputException, ApiRequestException, UnexpectedResponseException {

    RestResponse getResponse =
        executor.get(
            session,
            config.getString("MojoBaseUrl")
                + "/flash/api/jobgroups/"
                + jobGroup.getJobGroupId()
                + "?clientIds="
                + jobGroup.getClientId());

    if (!getResponse.isSuccess()) {
      String errorMessage =
          "Unable to make getJobGroup Request ,"
              + " check clientId,campaignId,JobGroupId "
              + getResponse;

      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }

    String validationErrors = this.validateEditEntity(jobGroup, validator);

    LocalDate endDate = jobGroup.getEndDate();
    List<JobGroupGetResponse> fields = getResponseData(getResponse);

    JobGroupGetResponse responseData = fields.get(0);

    String startDate = responseData.getStartDate();

    if (!isValidDate(startDate, endDate)) {
      String errorMessage = " Invalid endDate, startDate is " + startDate + ", ";

      if (validationErrors != null) {
        validationErrors += errorMessage;
      } else {
        validationErrors = errorMessage;
      }
    }

    if (validationErrors != null) {
      logger.error(validationErrors);
      throw new InvalidInputException(validationErrors);
    }

    jobGroup.setCampaignId(responseData.getCampaignId());
    deleteAndCopyActivePlacements(jobGroup, responseData);
    copyTradingGoals(jobGroup, responseData.getTradingGoals());

    RestResponse response =
        executor.put(
            session,
            config.getString("MojoBaseUrl") + "/thor/api/jobgroups/" + jobGroup.getJobGroupId(),
            jobGroup);

    if (!response.isSuccess()) {
      String errorMessage = "Unable to create Job Group: " + response.getJoveoUpdateErrorMeesage();
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

  private void deleteAndCopyActivePlacements(JobGroupDto jobGroup, JobGroupGetResponse responseData)
      throws InvalidInputException {

    List<JobGroupDto.JobGroupParams.Placements> placements = jobGroup.getPlacements();

    if (placements != null) {
      Iterator<JobGroupDto.JobGroupParams.Placements> itr = placements.iterator();

      while (itr.hasNext()) {
        JobGroupDto.JobGroupParams.Placements placement = itr.next();

        if (placement.delete) {
          if (containsPlacement(placement.publisher, responseData.getPlacements())) {
            itr.remove();
          } else {
            String errorMessage =
                "Placement " + placement.publisher + " to be deleted is not in placements";
            logger.error(errorMessage);
            throw new InvalidInputException(errorMessage);
          }
        }
      }
      copyActivePlacements(jobGroup, responseData.getPlacements());
    }
  }

  /**
   * fetch response data.
   *
   * @param response rest response
   * @return list of jobGroupGetResponse
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<JobGroupGetResponse> getResponseData(RestResponse response)
      throws UnexpectedResponseException {

    List<JobGroupGetResponse> dataList = new ArrayList<>();
    MojoResponse<JobGroupGetResponse> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<JobGroupGetResponse>>() {});

    for (MojoData<JobGroupGetResponse> data : mojoResponse.getData()) {
      dataList.add(data.getFields());
    }
    return dataList;
  }

  void copyActivePlacements(
      JobGroupDto jobGroup, List<JobGroupGetResponse.Placement> responsePlacements) {

    List<JobGroupDto.JobGroupParams.Placements> placements = jobGroup.getPlacements();

    for (JobGroupGetResponse.Placement placement : responsePlacements) {

      if (placement.getActive()) {
        String placementValue = placement.getPublisher();
        Double bid = placement.getBid();

        if (placement.getBudget() != null) {
          Double value = placement.getBudget().getValue();
          Freq freq = placement.getBudget().getFreq();
          Double threshold = placement.getBudget().getThreshold();
          Boolean pacing = placement.getBudget().getPacing();

          CapDto budget = new CapDto(pacing, freq, threshold, value);

          placements.add(new JobGroupDto.JobGroupParams.Placements(placementValue, bid, budget));
        } else {
          placements.add(new JobGroupDto.JobGroupParams.Placements(placementValue, bid));
        }
      }
    }
  }

  private boolean containsPlacement(
      String placementValue, List<JobGroupGetResponse.Placement> placements) {

    for (JobGroupGetResponse.Placement placement : placements) {
      if (placement.getActive() && placement.getPublisher().equals(placementValue)) {
        placement.setActive(false);
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  void copyTradingGoals(
      JobGroupDto jobGroup, JobGroupGetResponse.TradingGoals responseTradingGoals) {

    JobGroupDto.JobGroupParams.TradingGoals tradingGoals = jobGroup.getTradingGoals();

    List<JobGroupDto.JobGroupParams.TradingGoals.IoDetails> ioDetails;

    List<JobGroupDto.JobGroupParams.TradingGoals.PerformanceTargets> performanceTargets;

    if (tradingGoals != null) {

      ioDetails = tradingGoals.ioDetails;
      performanceTargets = tradingGoals.performanceTargets;

      if (ioDetails == null && performanceTargets != null) {
        jobGroup.setIoDetails(new ArrayList<>());
        copyIoDetails(jobGroup, responseTradingGoals);
      }

      if (performanceTargets == null || performanceTargets.size() == 1) {

        List<JobGroupGetResponse.TradingGoals.IoDetails.PerformanceTargets> performanceTargetsList =
            responseTradingGoals.getPerformanceTargets();

        if (performanceTargets == null) {
          copyPerformanceTargets(jobGroup, performanceTargetsList);
        } else {
          if (!performanceTargets.get(0).type.equals(performanceTargetsList.get(0).type)) {
            tradingGoals.addPerformanceTargets(
                performanceTargetsList.get(0).type, performanceTargetsList.get(0).value);
          } else {
            tradingGoals.addPerformanceTargets(
                performanceTargetsList.get(1).type, performanceTargetsList.get(1).value);
          }
        }
      }
    }
  }

  private void copyPerformanceTargets(
      JobGroupDto jobGroup,
      List<JobGroupGetResponse.TradingGoals.IoDetails.PerformanceTargets> performanceTargetsList) {

    for (JobGroupGetResponse.TradingGoals.IoDetails.PerformanceTargets performanceTarget :
        performanceTargetsList) {

      String type = performanceTarget.type;
      Double value = performanceTarget.value;
      jobGroup.addPerformanceTargets(type, value);
    }
  }

  private void copyIoDetails(
      JobGroupDto jobGroup, JobGroupGetResponse.TradingGoals responseTradingGoals) {

    List<JobGroupGetResponse.TradingGoals.IoDetails> ioDetailsList = responseTradingGoals.ioDetails;

    for (JobGroupGetResponse.TradingGoals.IoDetails ioDetail : ioDetailsList) {

      String number = ioDetail.number;
      Integer value = ioDetail.value;
      LocalDate startDate =
          LocalDate.parse(ioDetail.startDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      LocalDate endDate =
          LocalDate.parse(ioDetail.endDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

      jobGroup.addIoDetail(number, value, startDate, endDate);
    }
  }

  /**
   * validate edit entity.
   *
   * @param entity entity field
   * @param validator Validator object
   * @param <T> generic param
   * @return generic
   */
  public <T> String validateEditEntity(T entity, Validator validator) {

    Set<ConstraintViolation<T>> constraintViolations =
        validator.validate(entity, JobGroupCap.class, EditJobGroup.class);

    return getValidationMessages(constraintViolations, entity);
  }

  @Override
  public <T> String validateEntity(T entity, Validator validator) {

    Set<ConstraintViolation<T>> constraintViolations =
        validator.validate(entity, JobGroupCap.class, Default.class);

    return getValidationMessages(constraintViolations, entity);
  }

  /**
   * get validation message.
   *
   * @param constraintViolations contains violations
   * @param entity entity field
   * @param <T> generic param
   * @return generic
   */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  private <T> String getValidationMessages(
      Set<ConstraintViolation<T>> constraintViolations, T entity) {

    JobGroupDto jobGroupDto = (JobGroupDto) entity;

    Boolean isValidJobFilter = true;

    if (jobGroupDto.getFilter() != null) {
      isValidJobFilter = jobGroupDto.isValidJobFilter(jobGroupDto.getFilter(), 1);
    }

    StringBuilder message = new StringBuilder();
    if (!isValidJobFilter) {
      message.append("Invalid JobFilter,nesting of grouping is >3 ");
    }
    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<T> violation : constraintViolations) {
        message.append(violation.getMessage()).append(", ");
      }
    }
    if (!message.toString().equals("")) {
      return message.toString();
    }
    return null;
  }

  /**
   * getting all job groups.
   *
   * @param driver Instance of driver
   * @param clientId clientId as a string
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return list of job group
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<JobGroup> getJobGroups(
      Driver driver, String clientId, LocalDate startDate, LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);

    return getJobGroupsData(driver, platformFiltersDto, clientId);
  }

  /**
   * get all job groups for job group placement.
   *
   * @param driver Instance of driver
   * @param clientId clientId as a string
   * @param campaignId Campaign id
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return list of job group
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<JobGroup> getJobGroups(
      Driver driver, String clientId, String campaignId, LocalDate startDate, LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    PlatformFiltersDto platformFiltersDto = new PlatformFiltersDto();
    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(campaignId, PFfields.campaignId, PfOperators.IN);

    return getJobGroupsData(driver, platformFiltersDto, clientId);
  }

  private List<JobGroup> getJobGroupsData(
      Driver driver, PlatformFiltersDto platformFiltersDto, String clientId)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    List<JobGroup> jobGroups = new ArrayList<>();
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);
    RestResponse response =
        executor.post(
            driver.session,
            driver.conf.getString("MojoBaseUrl") + "/flash/api/jobgroups",
            platformFiltersDto);

    if (!response.isSuccess()) {
      logger.error("failed to get campaigns " + response.getJoveoErrorMessage());
      throw new UnexpectedResponseException(
          "failed to get campaigns " + response.getJoveoErrorMessage());
    }
    MojoResponse<Stats> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<Stats>>() {});

    for (MojoData<Stats> data : mojoResponse.getData()) {
      jobGroups.add(new JobGroup(driver, clientId, data.getFields().getId()));
    }
    return jobGroups;
  }

  /**
   * get jobs for job groups.
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
   * fetching job details.
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
    return JoveoEntity.jobgroups.toString();
  }

  @Override
  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String jobGroupId) {
    platformFiltersDto.addRule(jobGroupId, PFfields.jobGroupId, PfOperators.IN);
    return platformFiltersDto;
  }

  @Override
  public PlatformFiltersDto enrichFiltersWithEntityId(
      PlatformFiltersDto platformFiltersDto, String clientId, String jobGroupId) {
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);
    platformFiltersDto.addRule(jobGroupId, PFfields.entityId, PfOperators.IN);
    return platformFiltersDto;
  }
}
