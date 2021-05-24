package entitycreators;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Inbound Feed is being created. */
public class InBoundFeedCreator extends TestRunnerBase {

  /**
   * .
   *
   * @param map map
   * @throws MojoException on MojoException.
   */
  public static Map<ClientDto, String> feedCreator(Map<ClientDto, FeedDto> map)
      throws MojoException {

    if (driver == null) {
      createDriver();
    }

    Map<ClientDto, String> clientFeedUrlMap = new HashMap<>();
    for (Map.Entry<ClientDto, FeedDto> entry : map.entrySet()) {

      final ClientDto clientDto = entry.getKey();

      FeedDto feedDto = entry.getValue();

      String url = driver.generateInboundFeed(feedDto);
      clientFeedUrlMap.put(clientDto, url);
    }
    return clientFeedUrlMap;
  }

  /** . Get a job with Fields and values. */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public static FeedJob getJob(Map<JobFilterFields, List<String>> job, boolean notFeed) {

    FeedJob feedJob = new FeedJob();

    String defaultValue = null;
    for (Map.Entry<JobFilterFields, List<String>> entry : job.entrySet()) {

      switch (entry.getKey().toString()) {
        case "title":
          feedJob.setTitle(entry.getValue().get(0));
          break;
        case "city":
          feedJob.setCity(entry.getValue().get(0));
          break;
        case "state":
          feedJob.setState(entry.getValue().get(0));
          break;
        case "country":
          feedJob.setCountry(entry.getValue().get(0));
          break;
        case "category":
          feedJob.setCategory(entry.getValue().get(0));
          break;
        case "refNumber":
          feedJob.setReferenceNumber(Integer.parseInt(entry.getValue().get(0)));
          break;
        case "postedDate":
          feedJob.setDate(
              LocalDate.parse(entry.getValue().get(0), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
          break;
        case "cpcBid":
          feedJob.setCpc(Integer.parseInt(entry.getValue().get(0)));
          break;
        default:
          feedJob.addAdditionalFeedNode(entry.getKey().toString(), entry.getValue().get(0));
      }
      if (job.size() == 1 || !entry.getKey().equals(JobFilterFields.refNumber)) {
        defaultValue = entry.getValue().get(0);
      }
    }
    if (notFeed) {
      setDefaultValues("NoFeedValues", feedJob);
    } else {
      setDefaultValues(defaultValue, feedJob);
    }
    return feedJob;
  }

  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  private static void setDefaultValues(String defaultValue, FeedJob feedJob) {

    if (feedJob.getTitle() == null) {
      feedJob.setTitle(defaultValue);
    }
    if (feedJob.getCity() == null) {
      feedJob.setCity(defaultValue);
    }
    if (feedJob.getState() == null) {
      feedJob.setState(defaultValue);
    }
    if (feedJob.getCountry() == null) {
      feedJob.setCountry(defaultValue);
    }
    if (feedJob.getDescription() == null) {
      feedJob.setDescription(defaultValue);
    }
    if (feedJob.getUrl() == null) {
      feedJob.setUrl(defaultValue);
    }
    if (feedJob.getDate() == null) {
      feedJob.setDate(LocalDate.now().plusWeeks(1));
    }
    if (feedJob.getCategory() == null) {
      feedJob.setCategory(defaultValue);
    }

    if (feedJob.getCareerLevel() == null) {
      feedJob.setCareerLevel(defaultValue);
    }
    if (feedJob.getDepartment() == null) {
      feedJob.setDepartment(defaultValue);
    }
  }
}
