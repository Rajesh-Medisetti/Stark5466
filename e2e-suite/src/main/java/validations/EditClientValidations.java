package validations;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.OutboundJob;
import entitycreators.OutBoundJobCreator;
import java.util.List;
import java.util.Map;

public class EditClientValidations {

  /** . check InboundJobs InOIutBound With JobRefNo */
  public static Boolean checkInboundJobsInOIutBoundWithJobRefNo(
      FeedDto feed, Client client, String pubId) throws MojoException, InterruptedException {

    Map<String, OutboundJob> refIdOutBoundFeedJob =
        OutBoundJobCreator.outBoundFeedJob(client, pubId);

    List<FeedJob> jobList = feed.getJob();

    for (FeedJob job : jobList) {
      if (!refIdOutBoundFeedJob.containsKey(Integer.toString(job.getReferenceNumber()))) {
        return false;
      }
    }
    return true;
  }
}
