package entitycreators;

import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.Filter;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dtos.Dtos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class JobCreator {

  public Map<JobGroupDto, Integer> jobsInJobGroup;
  public Map<ClientDto, List<Map<JobFilterFields, List<String>>>> clientFeedMap;
  public Map<ClientDto, String> clientUrlMap;

  /** Constructor for JobCreator. */
  public JobCreator(
      Map<JobGroupDto, Integer> jobsInJobGroup,
      Map<ClientDto, List<Map<JobFilterFields, List<String>>>> clientFeedMap,
      Map<ClientDto, String> clientUrlDMap) {
    this.jobsInJobGroup = jobsInJobGroup;
    this.clientFeedMap = clientFeedMap;
    this.clientUrlMap = clientUrlDMap;
  }

  /** . JobProvider. */
  public static JobCreator jobProvider(List<Dtos> dtos) throws MojoException {

    Map<ClientDto, List<Map<JobFilterFields, List<String>>>> clientFeeds = new HashMap<>();

    Set<ClientDto> clientDtoSet = new HashSet<>();
    Map<JobGroupDto, Integer> jobsInJobGroupDto = new HashMap<>();

    for (Dtos dto : dtos) {

      JobGroupDto jobGroupDto = dto.getJobGroupDto();
      ClientDto clientDto = dto.getClientDto();

      Filter filter = jobGroupDto.getFilter().getRules().get(0);

      JobFilter jobFilter = (JobFilter) filter;

      List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

      int size = getRandomNumber(2, 5);
      String prefix = getRandomString(getRandomNumber(5, 10));
      String suffix = getRandomString(getRandomNumber(5, 10));
      List<String> data = new ArrayList<>();

      switch (jobFilter.getOperator().toString()) {
        case "EQUAL":
          Map<JobFilterFields, List<String>> job = new HashMap<>();
          data.add((String) jobFilter.getData());
          job.put(jobFilter.getField(), data);
          feed.add(job);
          break;

        case "IN":
          feed = getAllJobsForIn(jobFilter);
          break;

        case "BEGINS_WITH":
          feed = getAllJobs("", suffix, jobFilter, size);
          break;

        case "ENDS_WITH":
          feed = getAllJobs(prefix, "", jobFilter, size);
          break;

        default:
          feed = getAllJobs(prefix, suffix, jobFilter, size);
      }

      jobsInJobGroupDto.put(jobGroupDto, feed.size());

      if (clientDtoSet.contains(clientDto)) {
        List<Map<JobFilterFields, List<String>>> oldFeed = clientFeeds.get(clientDto);
        oldFeed.addAll(feed);
        clientFeeds.put(clientDto, oldFeed);
      } else {
        clientDtoSet.add(clientDto);
        clientFeeds.put(clientDto, feed);
      }

      System.out.println(feed.size());
    }

    Map<ClientDto, String> feedurl = InBoundFeedCreator.feedCreator(clientFeeds);
    return new JobCreator(jobsInJobGroupDto, clientFeeds, feedurl);
  }

  private static List<Map<JobFilterFields, List<String>>> getAllJobs(
      String prefix, String suffix, JobFilter jobFilter, int size) {

    List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

    String value = (String) jobFilter.getData();
    for (int i = 0; i < size; i++) {

      Map<JobFilterFields, List<String>> job = new HashMap<>();
      List<String> data = new ArrayList<>();

      data.add(prefix + value + suffix);
      job.put(jobFilter.getField(), data);
      feed.add(job);
    }
    return feed;
  }

  private static List<Map<JobFilterFields, List<String>>> getAllJobsForIn(JobFilter jobFilter) {

    List<Map<JobFilterFields, List<String>>> feed = new ArrayList<>();

    List<String> values = (List<String>) jobFilter.getData();

    for (String value : values) {

      Map<JobFilterFields, List<String>> job = new HashMap<>();
      List<String> data = new ArrayList<>();

      data.add(value);
      job.put(jobFilter.getField(), data);
      feed.add(job);
    }
    return feed;
  }

  /** . returning a random string of length size. */
  public static String getRandomString(int size) {
    char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    StringBuilder str = new StringBuilder(size);
    Random random = new Random();
    for (int i = 0; i < size; i++) {
      char c = chars[random.nextInt(chars.length)];
      str.append(c);
    }
    return str.toString();
  }

  public static int getRandomNumber(int minimum, int maximum) {
    Random random = new Random();
    return minimum + random.nextInt(maximum - minimum + 1);
  }
}
