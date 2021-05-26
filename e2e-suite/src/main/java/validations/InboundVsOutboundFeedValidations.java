package validations;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.OutboundJob;
import entitycreators.OutBoundJobCreator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.asserts.SoftAssert;

public class InboundVsOutboundFeedValidations {
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static void checkJobWithFields(FeedDto inboundFeedDto, Client client, String pubId, SoftAssert softAssert)
      throws MojoException, InterruptedException {

    Map<String, OutboundJob> refIdOutBoundFeedJob =
        OutBoundJobCreator.outBoundFeedJob(client, pubId);

    List<FeedJob> jobs = inboundFeedDto.getJob();

    softAssert.assertTrue(jobs.size() == refIdOutBoundFeedJob.size(), "Job count diff");


    for (FeedJob job : jobs) {
      softAssert.assertTrue(checkJobFields(
          job, refIdOutBoundFeedJob.get(Integer.toString(job.getReferenceNumber()))), "");
    }
  }

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

  public static FeedDto getUpdatedFeedDto(FeedDto inboundFeedDto) {
    FeedDto feedDto = new FeedDto();
    Set<Integer> refNos = new HashSet<>();
    List<FeedJob> inboundjobs = inboundFeedDto.getJob();
    for (int i = inboundjobs.size() - 1; i >= 0; i--) {
      FeedJob job = inboundjobs.get(i);
      if (!refNos.contains(job.getReferenceNumber())) {
        feedDto.addJob(job);
        refNos.add(job.getReferenceNumber());
      }
    }
    return feedDto;
  }
}
