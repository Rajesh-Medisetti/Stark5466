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

  /**
   * This method validates inbound feed with outbound feed.
   *
   * @param inboundFeedDto Inbound FeedDto
   * @param client Client
   * @param pubId Publisher
   * @param softAssert SoftAssert
   * @throws MojoException Mojo Exception
   * @throws InterruptedException Interrupted Exception
   */
  public static void checkJobWithFields(
      FeedDto inboundFeedDto, Client client, String pubId, SoftAssert softAssert)
      throws MojoException, InterruptedException {

    Map<String, OutboundJob> refIdOutBoundFeedJob =
        OutBoundJobCreator.outBoundFeedJob(client, pubId);

    List<FeedJob> jobs = inboundFeedDto.getJob();

    softAssert.assertTrue(
        jobs.size() == refIdOutBoundFeedJob.size(),
        "Jobs count in inbound and outbound feed got mismatched");

    for (FeedJob job : jobs) {
      softAssert.assertTrue(
          JobFilterValidations.checkJobFields(
              job, refIdOutBoundFeedJob.get(Integer.toString(job.getReferenceNumber()))),
          "Jobs content in inbound and outbound feed got mismatched");
    }
  }

  /**
   * If we have more than one job with same reference number then it will hold only most recent job
   * added.
   *
   * @param inboundFeedDto Inbound FeedDto
   * @return FeedDto
   */
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
