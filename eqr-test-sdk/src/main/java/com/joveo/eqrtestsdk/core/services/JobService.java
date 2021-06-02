package com.joveo.eqrtestsdk.core.services;

import static com.joveo.eqrtestsdk.utils.DateUtils.formatAsMojoDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.Job;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.JobStats;
import com.joveo.eqrtestsdk.models.JoveoEntity;
import com.joveo.eqrtestsdk.models.MojoData;
import com.joveo.eqrtestsdk.models.MojoResponse;
import com.joveo.eqrtestsdk.models.PFfields;
import com.joveo.eqrtestsdk.models.PfOperators;
import com.joveo.eqrtestsdk.models.PlatformFiltersDto;
import com.typesafe.config.Config;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobService extends BaseService {
  private static Logger logger = LoggerFactory.getLogger(JobService.class);

  @Inject
  JobService(JoveoHttpExecutor executor, Validator validator) {
    this.executor = executor;
    this.validator = validator;
  }

  /**
   * fetch job details.
   *
   * @param session session details
   * @param config config details
   * @param platformFiltersDto Instance of platformFilterDto
   * @param clientId clientId as a string
   * @param reqId Ref number
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return optional
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public Optional<JobStats> getJobDetails(
      Session session,
      Config config,
      PlatformFiltersDto platformFiltersDto,
      String clientId,
      String reqId,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);
    platformFiltersDto.addRule(reqId, PFfields.refNumber, PfOperators.EQUAL);

    String url = config.getString("MojoBaseUrl") + "/api/jobsv2";

    RestResponse response = executor.post(session, url, platformFiltersDto);
    if (!response.isSuccess()) {
      String errorMessage =
          "failed to get jobs data "
              + response.getJoveoErrorMessage()
              + " status code: "
              + response.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
    MojoResponse<JobStats> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<JobStats>>() {});

    for (MojoData<JobStats> data : mojoResponse.getData()) {
      if (data.getFields().getRefNumber().equals(reqId)) {
        return Optional.ofNullable(data.getFields());
      }
    }
    return Optional.empty();
  }

  /**
   * get all jobs.
   *
   * @param driver Instance of driver
   * @param platformFiltersDto Instance of platformFilterDto
   * @param clientId clientId as a string
   * @param page page number on Mojo
   * @param limit limit within the page
   * @param startDate start date in local date format
   * @param endDate end date in local date format
   * @return list of jobs
   * @throws MojoException throws custom mojo exception
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public List<Job> getJobs(
      Driver driver,
      PlatformFiltersDto platformFiltersDto,
      String clientId,
      int page,
      int limit,
      LocalDate startDate,
      LocalDate endDate)
      throws MojoException, ApiRequestException, UnexpectedResponseException {
    if (isInValidPageParameteres(page, limit)) {
      logger.error(
          "Invalid page or limit, page should be positive number and limit in range of 0-2000! ");
      throw new ApiRequestException("Invalid page or limit in request");
    }

    platformFiltersDto.addRule(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL);
    platformFiltersDto.addRule(clientId, PFfields.clientId, PfOperators.IN);
    platformFiltersDto.setLimit(limit);
    platformFiltersDto.setPage(page);

    String url = driver.conf.getString("MojoBaseUrl") + "/api/jobsv2";

    RestResponse response = executor.post(driver.session, url, platformFiltersDto);
    if (!response.isSuccess()) {
      String errorMessage =
          "failed to get jobs data "
              + response.getJoveoErrorMessage()
              + " status code: "
              + response.getResponseCode();
      logger.error(errorMessage);
      throw new UnexpectedResponseException(errorMessage);
    }
    MojoResponse<JobStats> mojoResponse =
        response.toMojoResponse(new TypeReference<MojoResponse<JobStats>>() {});

    List<Job> jobs = new ArrayList<>();
    for (MojoData<JobStats> data : mojoResponse.getData()) {
      jobs.add(
          new Job(driver, clientId, data.getFields().getId(), data.getFields().getRefNumber()));
    }
    return jobs;
  }

  private boolean isInValidPageParameteres(int page, int limit) {
    return page < 0 || limit < 0 || limit > 2000;
  }

  @Override
  public String getEntity() {
    return JoveoEntity.job.toString();
  }
}
