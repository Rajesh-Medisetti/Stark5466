package validations;

import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import entitycreators.JobCreator;
import java.util.List;

public class EditJobGroupValidations {

  /** . checking jobs in jobGroup */
  public boolean isJobsInJobGroup(
      JobGroup jobGroup, Driver driver, JobGroupDto jobGroupDto, JobCreator jobCreator)
      throws MojoException {

    FeedDto feedDto = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto);

    List<FeedJob> jobs = feedDto.getJob();

    for (FeedJob job : jobs) {
      if (!jobGroup.getJobDetails(Integer.toString(job.getReferenceNumber())).isPresent()) {
        return false;
      }
    }
    return true;
  }
}
