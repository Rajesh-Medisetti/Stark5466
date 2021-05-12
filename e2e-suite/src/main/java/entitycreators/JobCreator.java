package entitycreators;

import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.Filter;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.RuleOperator;
import dtos.Dtos;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobCreator {

  public Map<JobGroupDto, List<Map<JobFilterFields, List<String>>>> jobsInJobGroup;
  public Map<ClientDto, FeedDto> clientFeedMap;
  public Map<ClientDto, String> clientUrlMap;
  public Map<JobGroupDto, FeedDto> jobGroupDtoFeedDtoMap;

  public static Integer refNo = 0;

  /** Constructor for JobCreator. */
  public JobCreator(
      Map<JobGroupDto, List<Map<JobFilterFields, List<String>>>> jobsInJobGroup,
      Map<ClientDto, FeedDto> clientFeedMap,
      Map<ClientDto, String> clientUrlDMap,
      Map<JobGroupDto, FeedDto> jobGroupDtoFeedDtoMap) {
    this.jobsInJobGroup = jobsInJobGroup;
    this.clientFeedMap = clientFeedMap;
    this.clientUrlMap = clientUrlDMap;
    this.jobGroupDtoFeedDtoMap = jobGroupDtoFeedDtoMap;
  }

  /** . JobProvider. */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public static JobCreator jobProvider(List<Dtos> dtos) throws MojoException {

    Map<ClientDto, FeedDto> clientFeeds = new HashMap<>();

    Map<JobGroupDto, FeedDto> jobGroupFeedMap = new HashMap<>();

    Set<ClientDto> clientDtoSet = new HashSet<>();

    boolean noFeed = false;
    Map<JobGroupDto, List<Map<JobFilterFields, List<String>>>> jobsInJobGroupDto = new HashMap<>();

    for (Dtos dto : dtos) {
      noFeed = false;
      JobGroupDto jobGroupDto = dto.getJobGroupDto();
      final ClientDto clientDto = dto.getClientDto();

      Filter filter = jobGroupDto.getFilter().getRules().get(0);

      JobFilter jobFilter = (JobFilter) filter;

      List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

      int size = Utils.getRandomNumber(2, 5);
      List<String> data = new ArrayList<>();

      switch (jobFilter.getOperator().toString()) {
        case "EQUAL":
        case "NOT_EQUAL":
          Map<JobFilterFields, List<String>> job = new HashMap<>();
          if (jobFilter.getOperator().toString().equals("NOT_EQUAL")) {
            String notData = (String) jobFilter.getData();
            data.add(notData + "ERRR");
            noFeed = true;
          } else {
            data.add((String) jobFilter.getData());
          }
          job.put(jobFilter.getField(), new ArrayList<>(data));

          if (!jobFilter.getField().equals(JobFilterFields.refNumber)) {
            data.clear();
            data.add((++refNo).toString());
            job.put(JobFilterFields.refNumber, new ArrayList<>(data));
          }
          data.clear();
          feed.add(job);
          break;

        case "IN":
          feed = getAllJobsForIn(jobFilter);
          break;

        case "NOT_IN":
          feed = getAllJobsForIn(jobFilter);
          noFeed = true;
          break;

        case "BEGINS_WITH":
        case "ENDS_WITH":
          feed = getAllJobs(jobFilter, size);
          break;

        case "NOT_BEGINS_WITH":
        case "NOT_ENDS_WITH":
          feed = getAllJobs(jobFilter, size);
          noFeed = true;
          break;

        default:
          feed = getAllJobs(jobFilter, size);
      }
      FeedDto localFeedDto = getFeed(feed, noFeed);
      jobGroupFeedMap.put(jobGroupDto, localFeedDto);

      jobsInJobGroupDto.put(jobGroupDto, new ArrayList<>(feed));

      List<JobGroupDto> jobGroupDtos = new ArrayList<>();
      jobGroupDtos.add(jobGroupDto);

      if (clientDtoSet.contains(clientDto)) {
        //        List<Map<JobFilterFields, List<String>>> oldFeed = clientFeeds.get(clientDto);
        FeedDto oldFeed1 = clientFeeds.get(clientDto);
        for (FeedJob job : localFeedDto.getJob()) {
          oldFeed1.addJob(job);
        }
        clientFeeds.put(clientDto, oldFeed1);

        //        clientFeeds.put(clientDto, oldFeed);

      } else {
        clientDtoSet.add(clientDto);
        clientFeeds.put(clientDto, localFeedDto);
      }
    }
    Map<ClientDto, String> feedurl = InBoundFeedCreator.feedCreator(clientFeeds);
    return new JobCreator(jobsInJobGroupDto, clientFeeds, feedurl, jobGroupFeedMap);
  }

  private static FeedDto getFeed(List<Map<JobFilterFields, List<String>>> feed, boolean notFeed) {

    FeedDto feedDto = new FeedDto();
    for (Map<JobFilterFields, List<String>> jobDetails : feed) {
      feedDto.addJob(InBoundFeedCreator.getJob(jobDetails, notFeed));
    }
    return feedDto;
  }

  private static List<Map<JobFilterFields, List<String>>> getAllJobs(
      JobFilter jobFilter, int size) {

    List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

    String value = (String) jobFilter.getData();
    for (int i = 0; i < size; i++) {

      Map<JobFilterFields, List<String>> job = new HashMap<>();
      List<String> data = new ArrayList<>();

      if (jobFilter.getOperator().equals(RuleOperator.CONTAINS)) {
        data.add(
            Utils.getRandomString(Utils.getRandomNumber(10, 20))
                + value
                + Utils.getRandomString(Utils.getRandomNumber(10, 20)));
      } else if (jobFilter.getOperator().equals(RuleOperator.NOT_CONTAINS)) {
        data.add(
            Utils.getRandomString(Utils.getRandomNumber(10, 20))
                + "ERR"
                + Utils.getRandomString(Utils.getRandomNumber(10, 20)));
      } else if (jobFilter.getOperator().equals(RuleOperator.BEGINS_WITH)) {
        data.add("" + value + Utils.getRandomString(Utils.getRandomNumber(10, 20)));
      } else if (jobFilter.getOperator().equals(RuleOperator.NOT_BEGINS_WITH)) {
        data.add("ERR" + value + Utils.getRandomString(Utils.getRandomNumber(10, 20)));
      } else if (jobFilter.getOperator().equals(RuleOperator.ENDS_WITH)) {
        data.add(Utils.getRandomString(Utils.getRandomNumber(10, 20)) + value + "");
      } else {
        data.add(Utils.getRandomString(Utils.getRandomNumber(10, 20)) + value + "ERR");
      }

      job.put(jobFilter.getField(), new ArrayList<>(data));

      if (!jobFilter.getField().equals(JobFilterFields.refNumber)) {
        data.clear();
        data.add((++refNo).toString());
        job.put(JobFilterFields.refNumber, new ArrayList<>(data));
      }
      data.clear();
      feed.add(job);
    }
    return feed;
  }

  //
  //  private static List<Map<JobFilterFields, List<String>>> getAllJobs(
  //      JobFilter jobFilter, int size) {
  //
  //    List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();
  //
  //    String value = (String) jobFilter.getData();
  //    for (int i = 0; i < size; i++) {
  //
  //      Map<JobFilterFields, List<String>> job = new HashMap<>();
  //      List<String> data = new ArrayList<>();
  //
  //      if (jobFilter.getOperator().equals(RuleOperator.CONTAINS)) {
  //        data.add(
  //            Utils.getRandomString(Utils.getRandomNumber(10, 20))
  //                + value
  //                + Utils.getRandomString(Utils.getRandomNumber(10, 20)));
  //      } else if (jobFilter.getOperator().equals(RuleOperator.BEGINS_WITH)) {
  //        data.add("" + value + Utils.getRandomString(Utils.getRandomNumber(10, 20)));
  //      } else {
  //        data.add(Utils.getRandomString(Utils.getRandomNumber(10, 20)) + value + "");
  //      }
  //
  //      job.put(jobFilter.getField(), new ArrayList<>(data));
  //
  //      if (!jobFilter.getField().equals(JobFilterFields.refNumber)) {
  //        data.clear();
  //        data.add((++refNo).toString());
  //        job.put(JobFilterFields.refNumber, new ArrayList<>(data));
  //      }
  //      data.clear();
  //      feed.add(job);
  //    }
  //    return feed;
  //  }

  private static List<Map<JobFilterFields, List<String>>> getAllJobsForIn(JobFilter jobFilter) {

    List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

    List<String> values = (List<String>) jobFilter.getData();

    for (String value : values) {

      Map<JobFilterFields, List<String>> job = new HashMap<>();
      List<String> data = new ArrayList<>();

      if (jobFilter.getOperator().equals(RuleOperator.NOT_IN)) {
        value = value + "ERR";
      }

      data.add(value);
      job.put(jobFilter.getField(), new ArrayList<>(data));

      if (!jobFilter.getField().equals(JobFilterFields.refNumber)) {
        data.clear();
        data.add((++refNo).toString());
        job.put(JobFilterFields.refNumber, new ArrayList<>(data));
      }
      data.clear();
      feed.add(job);
    }
    return feed;
  }
}
