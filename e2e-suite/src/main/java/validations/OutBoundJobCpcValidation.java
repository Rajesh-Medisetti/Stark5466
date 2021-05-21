package validations;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.OutboundJob;
import entitycreators.JobCreator;
import entitycreators.OutBoundJobCreator;
import java.util.List;
import java.util.Map;

public class OutBoundJobCpcValidation {

  /** . Checking cpc of jobs of a JobGroup in OutboundFeed */
  public Boolean getJobLevelCpc(
      Client client,
      String pubId,
      JobCreator jobCreator,
      JobGroupDto jobGroupDto,
      JobGroup jobGroup,
      Double cpc)
      throws MojoException, InterruptedException {

    List<FeedJob> jobs = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob();

    Map<String, OutboundJob> refIdOutBoundFeedJob =
        OutBoundJobCreator.outBoundFeedJob(client, pubId);

    for (FeedJob job : jobs) {

      Double jobCpc =
          Double.parseDouble(
              refIdOutBoundFeedJob
                  .get(Integer.toString(job.getReferenceNumber()))
                  .getCpc()
                  .substring(4));
      if (!cpc.equals(jobCpc)) {
        return false;
      }
    }
    return true;
  }
}
