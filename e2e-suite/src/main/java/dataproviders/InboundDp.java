package dataproviders;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dtos.AllEntities;
import entitycreators.InBoundFeedCreator;
import entitycreators.JobCreator;
import enums.BidLevel;
import helpers.MojoUtils;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class InboundDp {

  public static List<List<Object>> titleAndRefList;
  public static AllEntities newJobsToInboundEntity;
  public static AllEntities removedJobsToInboundEntity;
  public static Set<Client> clientsUsedForInboundTest = new HashSet<>();
  public static List<String> inboundFeedUrlsUsedForTest = new ArrayList<>();

  /**
   * This method is used to convert 2d list to 2d array.
   *
   * @return two dimensional array
   */
  @DataProvider(name = "test", parallel = false)
  public static Object[][] dpMethod() {
    List<List<Object>> dpList = titleAndRefList;
    Object[][] array = new Object[dpList.size()][dpList.get(0).size()];
    int counter = 0;
    for (List<Object> list : dpList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * This method is used for running schedulers.
   *
   * @param driver Driver
   * @throws MojoException Mojo Exception
   * @throws InterruptedException Interrupted Exception
   */
  public static void runSchedulers(Driver driver) throws MojoException, InterruptedException {
    Set<Client> clients = new HashSet<>();
    for (List<Object> obj : titleAndRefList) {
      clients.add((Client) obj.get(0));
    }
    clients.add(newJobsToInboundEntity.getClient());
    clients.add(removedJobsToInboundEntity.getClient());

    MojoUtils.runSchedulerAndRefreshCache(clients, driver);
  }

  /**
   * .This method returns 2d list of objects holding parameters needed for testing Reference number
   * and title.
   *
   * @param driver Driver
   * @return 2d list
   * @throws MojoException Mojo exception
   * @throws InterruptedException Interrupt exception
   */
  public static List<List<Object>> setDiffTitleAndRef(Driver driver) throws MojoException {
    List<Boolean> booleanList = new ArrayList<>();
    List<List<Object>> dpList = new ArrayList<>();
    booleanList.add(true);
    booleanList.add(false);
    for (Boolean isTitleSame : booleanList) {
      for (Boolean isReqSame : booleanList) {
        AllEntities allEntities =
            MojoUtils.createClientAlongWithCampaignAndJobGroup(
                driver, JobFilterDP.placements, BidLevel.PLACEMENT);

        inboundFeedUrlsUsedForTest.add(
            allEntities.getJobCreator().clientUrlMap.get(allEntities.getClientDto()));

        allEntities.getClient().runScheduler();
        driver.refreshJobCount();
        driver.refreshEntityCache();

        Integer initialJobs =
            allEntities
                .getJobCreator()
                .clientFeedMap
                .get(allEntities.getClientDto())
                .getJob()
                .size();

        Integer jobsAdded = Utils.getRandomNumber(initialJobs) + 1;

        MojoUtils.addNewJobToInboundFeed(
            allEntities.getJobCreator(), isTitleSame, isReqSame, jobsAdded, allEntities);

        inboundFeedUrlsUsedForTest.add(
            allEntities.getJobCreator().clientUrlMap.get(allEntities.getClientDto()));

        dpList.add(
            List.of(
                allEntities.getClient(),
                isTitleSame,
                isReqSame,
                allEntities,
                JobFilterDP.placements,
                initialJobs,
                jobsAdded));

        clientsUsedForInboundTest.add(allEntities.getClient());
      }
    }
    return dpList;
  }

  /**
   * This method adds random number of new jobs to client by creating a new job group.
   *
   * @param driver Driver
   * @return 2d list
   * @throws MojoException Mojo Exception
   * @throws InterruptedException Interrupted Exception
   */
  public static AllEntities addNewJobsScenario(Driver driver)
      throws MojoException, InterruptedException {
    // Add new jobs
    AllEntities entitiesOfClientForJobGroup =
        MojoUtils.addNewJobGroupToExistingClient(
            driver,
            JobFilterDP.placements,
            BidLevel.PLACEMENT,
            newJobsToInboundEntity.getJobCreator(),
            newJobsToInboundEntity);

    inboundFeedUrlsUsedForTest.add(
        entitiesOfClientForJobGroup
            .getJobCreator()
            .clientUrlMap
            .get(entitiesOfClientForJobGroup.getClientDto()));

    Set<Client> clients = new HashSet<>();
    clients.add(entitiesOfClientForJobGroup.getClient());

    MojoUtils.runSchedulerAndRefreshCache(clients, driver);

    return entitiesOfClientForJobGroup;
  }

  /**
   * This method removes random number of jobs.
   *
   * @param driver driver
   * @return 2d list
   * @throws MojoException MojoException
   * @throws InterruptedException InterruptedException
   */
  public static AllEntities removeJobsScenario(Driver driver)
      throws MojoException, InterruptedException {

    ClientDto clientDto = removedJobsToInboundEntity.getClientDto();
    ClientDto editClientDto = new ClientDto();

    JobGroupDto jobGroupDto = removedJobsToInboundEntity.getJobGroupDto();
    JobCreator jobCreator = removedJobsToInboundEntity.getJobCreator();

    int totalJobs = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob().size();
    int jobNo = Utils.getRandomNumber(totalJobs);

    // remove feed
    editClientDto.deleteFeed(jobCreator.clientUrlMap.get(clientDto));

    // update maps
    FeedDto inboundFeedDto = jobCreator.clientFeedMap.get(clientDto);
    inboundFeedDto.getJob().remove(jobNo);

    FeedDto feedDtoOfJobGroup1 = jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto);
    feedDtoOfJobGroup1.getJob().remove(jobNo);

    Map<ClientDto, FeedDto> clientFeeds = new HashMap<>();
    clientFeeds.put(clientDto, inboundFeedDto);

    String feedUrl = InBoundFeedCreator.feedCreator(clientFeeds).get(clientDto);

    // update jobcreation fields
    jobCreator.clientFeedMap.put(clientDto, inboundFeedDto);
    jobCreator.clientUrlMap.put(clientDto, feedUrl);
    jobCreator.jobGroupDtoFeedDtoMap.put(jobGroupDto, feedDtoOfJobGroup1);

    editClientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));
    inboundFeedUrlsUsedForTest.add(jobCreator.clientUrlMap.get(clientDto));

    Client client = removedJobsToInboundEntity.getClient();
    client.edit(editClientDto);

    Set<Client> clients = new HashSet<>();
    clients.add(removedJobsToInboundEntity.getClient());

    MojoUtils.runSchedulerAndRefreshCache(clients, driver);

    return removedJobsToInboundEntity;
  }
}
