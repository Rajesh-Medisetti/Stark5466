package dataproviders.editdp;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.*;
import dtos.Dtos;
import entitycreators.DtosCreatorForEdit;
import entitycreators.JobCreator;
import enums.Scheduler;
import helpers.MojoUtils;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class EditClientDP {

  public static Set<Client> clientSet = new HashSet<>();
  public static List<List<Object>> addFeedDPList = new ArrayList<>();
  public static List<List<Object>> removeFeedDPList = new ArrayList<>();
  public static List<List<Object>> editMarkDownDPList = new ArrayList<>();
  public static List<List<Object>> editClientBudgetDPList = new ArrayList<>();

  public static boolean ifSchedulerRan = true;
  public static String campaignName = "TestCampaign";
  static String publisher = "Naukri";

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "addFeed")
  public static Object[][] addFeedDP() {

    Object[][] array = new Object[addFeedDPList.size()][addFeedDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : addFeedDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "removeFeed")
  public static Object[][] removeFeedDP() {

    Object[][] array = new Object[removeFeedDPList.size()][removeFeedDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : removeFeedDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "editMarkDown")
  public static Object[][] editMarkDownDP() {

    Object[][] array = new Object[editMarkDownDPList.size()][editMarkDownDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : editMarkDownDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "editClientBudget")
  public static Object[][] editClientBudgetDP() {

    Object[][] array =
        new Object[editClientBudgetDPList.size()][editClientBudgetDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : editClientBudgetDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /** . checks OutBoundFeed after adding feed */
  public static void addFeedDataProvider(Driver driver, Scheduler scheduler) throws MojoException, InterruptedException {


    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();

    JobCreator jobCreator = new JobCreator();
    jobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    Client client = driver.createClient(clientDto);
    clientSet.add(client);


      ClientDto editClientDto = dtosList.get(1).getClientDto();

      editClientDto.addFeed(jobCreator.clientUrlMap.get(editClientDto));


      client.edit(editClientDto);

      if (scheduler.equals(Scheduler.Run)) {
        MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
      }

      CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
      campaignDto.setClientId(client.id);
      campaignDto.setName(campaignName);
      campaignDto.setBudget(1000.0);

      Campaign campaign = driver.createCampaign(campaignDto);

      JobGroupDto jobGroupDto1 = dtosList.get(0).getJobGroupDto();
      jobGroupDto1.setClientId(client.id);
      jobGroupDto1.setCampaignId(campaign.id);

      jobGroupDto1.setBudgetCap(true, Freq.Monthly, 80.0, 300.0);
      jobGroupDto1.addPlacementWithBid(publisher, 3.0);

      driver.createJobGroup(jobGroupDto1);

      JobGroupDto jobGroupDto2 = dtosList.get(1).getJobGroupDto();
      jobGroupDto2.setClientId(client.id);
      jobGroupDto2.setCampaignId(campaign.id);

      jobGroupDto2.setCpcBid(3.0);
      jobGroupDto2.setBudgetCap(true, Freq.Monthly, 80.0, 300.0);
      jobGroupDto2.addPlacement(publisher);
      driver.createJobGroup(jobGroupDto2);

      addFeedDPList.add(
          List.of(
              jobCreator.clientFeedMap.get(clientDto),
              jobCreator.clientFeedMap.get(editClientDto),
              client,
              jobGroupDto1.getPlacements().get(0).publisher));
  }

  /** . checks OutBoundFeed after removing feed */
  public static void removeFeedDataProvider(Driver driver, Scheduler scheduler) throws MojoException, InterruptedException {


    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();
    JobCreator jobCreator = new JobCreator();
    jobCreator.jobProvider(dtosList);


    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    Client client = driver.createClient(clientDto);
    clientSet.add(client);

      ClientDto editClientDto = dtosList.get(1).getClientDto();

      editClientDto.addFeed(jobCreator.clientUrlMap.get(editClientDto));

      editClientDto.deleteFeed(jobCreator.clientUrlMap.get(clientDto));


      client.edit(editClientDto);

      if (scheduler.equals(Scheduler.Run)) {
        MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
      }
      CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
      campaignDto.setClientId(client.id);
      campaignDto.setName(campaignName);
      campaignDto.setBudget(1000.0);

      Campaign campaign = driver.createCampaign(campaignDto);

      JobGroupDto jobGroupDto1 = dtosList.get(0).getJobGroupDto();
      jobGroupDto1.setClientId(client.id);
      jobGroupDto1.setCampaignId(campaign.id);

      jobGroupDto1.setBudgetCap(true, Freq.Monthly, 80.0, 300.0);
      jobGroupDto1.addPlacementWithBid("Naukri", 3.0);

      driver.createJobGroup(jobGroupDto1);

      JobGroupDto jobGroupDto2 = dtosList.get(1).getJobGroupDto();
      jobGroupDto2.setClientId(client.id);
      jobGroupDto2.setCampaignId(campaign.id);

      jobGroupDto2.setBudgetCap(true, Freq.Monthly, 80.0, 300.0);
      jobGroupDto2.addPlacementWithBid("Naukri", 3.0);
      driver.createJobGroup(jobGroupDto2);

      removeFeedDPList.add(
          List.of(
              jobCreator.clientFeedMap.get(editClientDto),
              client,
              jobGroupDto1.getPlacements().get(0).publisher));
  }

  /** . checks OutBoundJob cpc after editing markDown */
  public static void editMarkDownDataProvider(Driver driver, Scheduler scheduler) throws MojoException, InterruptedException {


    final Double editedMarkDown = 50.0;

    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();

    JobCreator jobCreator = new JobCreator();
    jobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    clientDto.setMarkDown(60.0, false);
    Client client = driver.createClient(clientDto);
    clientSet.add(client);

      ClientDto editClientDto = dtosList.get(1).getClientDto();

      editClientDto.addFeed(jobCreator.clientUrlMap.get(editClientDto));

      editClientDto.setMarkDown(editedMarkDown, false);
      client.edit(editClientDto);

      if (scheduler.equals(Scheduler.Run)) {
        MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
      }

      CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
      campaignDto.setBudget(1000.0);
      campaignDto.setClientId(client.id);
      campaignDto.setName(campaignName);

      Campaign campaign = driver.createCampaign(campaignDto);

      JobGroupDto jobGroupDto1 = dtosList.get(0).getJobGroupDto();
      jobGroupDto1.setClientId(client.id);
      jobGroupDto1.setCampaignId(campaign.id);
      jobGroupDto1.setCpcBid(4.0);

      jobGroupDto1.setBudgetCap(true, Freq.Monthly, 80.0, 300.0);
      jobGroupDto1.addPlacement(publisher);

      JobGroup jobGroup1 = driver.createJobGroup(jobGroupDto1);

      editMarkDownDPList.add(
          List.of(
              editedMarkDown,
              client,
              jobGroupDto1,
              jobGroup1,
              jobCreator,
              dtosList.get(0).getBidLevel()));

      JobGroupDto jobGroupDto2 = dtosList.get(1).getJobGroupDto();
      jobGroupDto2.setClientId(client.id);
      jobGroupDto2.setCampaignId(campaign.id);
      jobGroupDto2.setCpcBid((double) Utils.getRandomNumber(1, 5));

      jobGroupDto2.setBudgetCap(true, Freq.Monthly, 80.0, 300.0);
      jobGroupDto2.addPlacementWithBid(publisher, (double) Utils.getRandomNumber(1, 5));
      JobGroup jobGroup2 = driver.createJobGroup(jobGroupDto2);

      editMarkDownDPList.add(
          List.of(
              editedMarkDown,
              client,
              jobGroupDto2,
              jobGroup2,
              jobCreator,
              dtosList.get(1).getBidLevel()));
  }

  /** . checks clientBudget after editing budget */
  public static void editClientBudgetDataProvider(Driver driver) throws MojoException, InterruptedException {


    Double newBudget = (double) Utils.getRandomNumber(1000, 2000);
    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();

    JobCreator jobCreator = new JobCreator();
    jobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    Client client = driver.createClient(clientDto);
    clientSet.add(client);

      ClientDto editClientDto = dtosList.get(1).getClientDto();

      editClientDto.setBudget(newBudget);
      client.edit(editClientDto);


      editClientBudgetDPList.add(List.of(client, newBudget));
  }

  /** . run Scheduler */
  public static void runScheduler(Driver driver) {

    try {
      MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
    } catch (Exception e) {
      ifSchedulerRan = false;
    }
  }
}
