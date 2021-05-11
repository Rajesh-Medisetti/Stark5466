package validations;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.OutboundJob;
import entitycreators.JobCreator;
import entitycreators.OutBoundJobCreator;
import java.util.List;
import java.util.Map;

public class JobFilterValidations {
  /** . checking a job Is Live. */
  public static boolean isJobLive(
      ClientDto clientDto,
      Client clientObj,
      JobGroupDto jobGroupDto,
      JobGroup jobGroup,
      String pubId,
      JobCreator jobCreator,
      Driver driver)
      throws MojoException {

    List<FeedJob> jobs = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob();

    driver.refreshEntityCache();

    for (FeedJob job : jobs) {
      if (!jobGroup
          .getJobDetails(Integer.toString(job.getReferenceNumber()))
          .get()
          .isLiveStatus()) {
        return false;
      }
    }
    return true;
  }

  /** . checking a job with refNo in OutBoundFeed. */
  public static boolean checkJobWithRefNo(
      ClientDto clientDto,
      Client clientObj,
      JobGroupDto jobGroupDto,
      JobGroup jobGroup,
      String pubId,
      JobCreator jobCreator)
      throws MojoException {

    Map<String, OutboundJob> refIdOutBoundFeedJob =
        OutBoundJobCreator.outBoundFeedJob(clientObj, pubId);

    List<FeedJob> jobs = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob();

    for (FeedJob job : jobs) {
      if (!refIdOutBoundFeedJob.containsKey(Integer.toString(job.getReferenceNumber()))) {
        return false;
      }
    }
    return true;
  }

  /** . checking a job Is Live. */
  public static boolean checkJobWithFields(
      ClientDto clientDto,
      Client clientObj,
      JobGroupDto jobGroupDto,
      JobGroup jobGroup,
      String pubId,
      JobCreator jobCreator)
      throws MojoException {

    Map<String, OutboundJob> refIdOutBoundFeedJob =
        OutBoundJobCreator.outBoundFeedJob(clientObj, pubId);

    List<FeedJob> jobs = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob();

    for (FeedJob job : jobs) {
      if (!checkJobFields(
          job, refIdOutBoundFeedJob.get(Integer.toString(job.getReferenceNumber())))) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  private static boolean checkJobFields(FeedJob job, OutboundJob outboundJob) {

    if (!job.getTitle().equalsIgnoreCase(outboundJob.title)) {
      return false;
    }

    if (!job.getCity().equalsIgnoreCase(outboundJob.city)) {
      return false;
    }

    if (!job.getState().equalsIgnoreCase(outboundJob.state)) {
      return false;
    }

    if (!job.getCountry().equalsIgnoreCase(outboundJob.country)) {
      return false;
    }

    if (!job.getDescription().equalsIgnoreCase(outboundJob.description)) {
      return false;
    }

    if (!(Integer.toString(job.getReferenceNumber())
        .equalsIgnoreCase(outboundJob.referencenumber))) {
      return false;
    }

    if (!job.getCategory().equalsIgnoreCase(outboundJob.category)) {
      return false;
    }

    return true;
  }
}
