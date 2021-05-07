package entitycreators;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Inbound Feed is being created. */
public class InBoundFeedCreator extends TestRunnerBase {

  static FeedJob[] referenceNumberSlot;

  /*public static void main(String[] args) throws MojoException {

    Map<ClientDto, List<Map<JobFilterFields, List<String>>>> map = new HashMap<>();

    String feedUrl = "https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml";

    Map<JobFilterFields, List<String>> jobFeed1 = new HashMap<>();
    Map<JobFilterFields, List<String>> jobFeed2 = new HashMap<>();
    Map<JobFilterFields, List<String>> jobFeed3 = new HashMap<>();
    Map<JobFilterFields, List<String>> jobFeed4 = new HashMap<>();
    Map<JobFilterFields, List<String>> jobFeed5 = new HashMap<>();

    List<String> values1 = new ArrayList<>();
    List<String> values2 = new ArrayList<>();
    List<String> values3 = new ArrayList<>();
    List<String> values4 = new ArrayList<>();
    List<String> values5 = new ArrayList<>();

    values1.add("5");
    values2.add("India");
    values3.add("1");
    values4.add("2");
    values5.add("3");

    jobFeed1.put(JobFilterFields.refNumber, values1);
    jobFeed2.put(JobFilterFields.country, values2);
    jobFeed3.put(JobFilterFields.refNumber, values3);
    jobFeed4.put(JobFilterFields.refNumber, values4);
    jobFeed5.put(JobFilterFields.refNumber, values5);

    List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

    feed.add(jobFeed1);
    feed.add(jobFeed2);
    feed.add(jobFeed3);
    feed.add(jobFeed4);
    feed.add(jobFeed5);

    map.put(ClientEntityCreator.randomClientCreator(feedUrl), feed);
    map.put(ClientEntityCreator.randomClientCreator(feedUrl), feed);

    feedCreator(map);
  }*/

  /**
   * .
   *
   * @param map map
   * @throws MojoException on MojoException.
   */
  public static Map<ClientDto, String> feedCreator(
      Map<ClientDto, List<Map<JobFilterFields, List<String>>>> map) throws MojoException {

    if (driver == null) {
      createDriver();
    }

    Map<ClientDto, String> clientFeedUrlMap = new HashMap<>();
    for (Map.Entry<ClientDto, List<Map<JobFilterFields, List<String>>>> entry : map.entrySet()) {

      final ClientDto clientDto = entry.getKey();

      List<Map<JobFilterFields, List<String>>> jobFeed = entry.getValue();

      FeedDto feedDto = new FeedDto();

      referenceNumberSlot = new FeedJob[jobFeed.size() + 1];
      Arrays.fill(referenceNumberSlot, null);

      for (Map<JobFilterFields, List<String>> job : jobFeed) {

        FeedJob feedJob = getJob(job);
        feedDto.addJob(feedJob);
      }

      String url = driver.generateInboundFeed(feedDto);
      clientFeedUrlMap.put(clientDto, url);
    }
    return clientFeedUrlMap;
  }

  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  private static FeedJob getJob(Map<JobFilterFields, List<String>> job) {

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
          if (feedJob.getReferenceNumber() < referenceNumberSlot.length) {
            if (referenceNumberSlot[feedJob.getReferenceNumber()] != null) {
              shiftJobFeed(feedJob.getReferenceNumber());
              referenceNumberSlot[feedJob.getReferenceNumber()] = feedJob;
            }
          }
          break;
        case "postedDate":
          feedJob.setDate(LocalDate.parse(entry.getValue().get(0)));
          break;
        case "cpcBid":
          feedJob.setCpc(Integer.parseInt(entry.getValue().get(0)));
          break;
        default:
          feedJob.addAdditionalFeedNode(entry.getKey().toString(), entry.getValue().get(0));
      }
      defaultValue = entry.getValue().get(0);
    }

    setDefaultValues(defaultValue, feedJob);

    return feedJob;
  }

  private static void shiftJobFeed(int referenceNumber) {

    for (int i = referenceNumber; i < referenceNumberSlot.length; i++) {
      if (referenceNumberSlot[i] == null) {
        referenceNumberSlot[i] = referenceNumberSlot[referenceNumber];
        referenceNumberSlot[i].setReferenceNumber(i);
        return;
      }
    }

    for (int i = referenceNumber; i >= 1; i--) {
      if (referenceNumberSlot[i] == null) {
        referenceNumberSlot[i] = referenceNumberSlot[referenceNumber];
        if (referenceNumberSlot[i] != null) {
          referenceNumberSlot[i].setReferenceNumber(i);
        }
        return;
      }
    }
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
    if (feedJob.getReferenceNumber() == 0) {
      feedJob.setReferenceNumber(getNextDefaultRefNumber(feedJob));
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

  private static int getNextDefaultRefNumber(FeedJob feedJob) {

    for (int i = 1; i < referenceNumberSlot.length; i++) {
      if (referenceNumberSlot[i] == null) {
        referenceNumberSlot[i] = feedJob;
        return i;
      }
    }
    return referenceNumberSlot.length - 1;
  }
}
