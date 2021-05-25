package dataproviders;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.Freq;
import com.joveo.eqrtestsdk.models.GroupOperator;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dtos.Dtos;
import dtos.JobFilterData;
import entitycreators.CampaignEntityCreator;
import entitycreators.ClientEntityCreator;
import entitycreators.JobCreator;
import entitycreators.JobGroupCreator;
import entitycreators.JobGroupFilterCreator;
import enums.BidLevel;
import helpers.MojoUtils;
import helpers.TestUtils;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class JobFilterDP {
  public static boolean ifSchedulerRan = true;
  static JobCreator jobCreator;
  static Client globalClient;
  static Campaign globalCampaign;
  static String feed = "https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml";
  public static String placements = "Naukri";
  public static Set<Client> clientSet = new HashSet<>();
  static Object[][] arr;
  //  static List<List<Object>> dpList = new ArrayList<>();
  public static JobFilterData data1to1;
  public static JobFilterData dataMultiple;

  /** . return a list of Dtos. */
  public static List<Dtos> dpMethod1() {
    List<Dtos> dtosList = new ArrayList<>();
    dtosList.addAll(createDtoListStringNegative(GroupOperator.AND, BidLevel.PLACEMENT));
    dtosList.addAll(createDtoListStringNegative(GroupOperator.OR, BidLevel.JOB_GROUP));
    dtosList.addAll(createDtoListStringPositive(GroupOperator.AND, BidLevel.JOB_GROUP));
    dtosList.addAll(createDtoListStringPositive(GroupOperator.OR, BidLevel.PLACEMENT));
    dtosList.addAll(createDtoListStringPositive(GroupOperator.OR, BidLevel.NO_BID));
    for (List<String> dateList : TestRunnerBase.getDateGroups()) {
      dtosList.addAll(createDtoListDate(GroupOperator.OR, BidLevel.PLACEMENT, dateList));
    }
    return dtosList;
  }

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "test", parallel = false)
  public static Object[][] dpMethod() {
    List<List<Object>> dpList = data1to1.getDpList();
    Object[][] array = new Object[dpList.size()][dpList.get(0).size()];
    int counter = 0;
    for (List<Object> list : dpList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /** . creating JobFilter */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public JobFilterData createJobFilterData(Driver driver, List<Dtos> dtosList)
      throws MojoException {
    List<List<Object>> dpList = new ArrayList<>();
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(false, 0.0);
    clientDto.addFeed(feed);
    globalClient = driver.createClient(clientDto);
    ClientDto globalClientDto = clientDto;
    CampaignDto campaignDto = CampaignEntityCreator.randomCampaignCreator(1000);
    campaignDto.setClientId(globalClient.id);
    globalCampaign = driver.createCampaign(campaignDto);

    JobCreator jobCreator = new JobCreator();
    jobCreator.jobProvider(dtosList);

    Set<ClientDto> clientDtoSet = new HashSet<>();
    Client myClient = null;
    Campaign myCampaign = null;
    for (Dtos dtos : dtosList) {
      JobGroupDto jobGroupDto = dtos.getJobGroupDto();
      ClientDto clientDto1 = dtos.getClientDto();
      CampaignDto campaignDto1 = dtos.getCampaignDto();
      if (clientDto1 == null) {
        myClient = globalClient;
        myCampaign = globalCampaign;
        clientDto1 = globalClientDto;
      }
      if (clientDto1 != null && !clientDtoSet.contains(clientDto1)) {
        clientDto1.addFeed(jobCreator.clientUrlMap.get(clientDto1));
        myClient = driver.createClient(clientDto1);
        if (campaignDto1 == null) {
          campaignDto1 = campaignDto;
        }
        campaignDto1.setClientId(myClient.id);
        myCampaign = driver.createCampaign(campaignDto1);
        clientDtoSet.add(clientDto1);
      }
      jobGroupDto.setPriority(1);
      jobGroupDto.setClientId(myClient.id);
      jobGroupDto.setCampaignId(myCampaign.id);
      if (dtos.getBidLevel().equals(BidLevel.PLACEMENT)) {
        jobGroupDto.addPlacementWithBudgetAndBid(
            placements, 1.80, 200.00, Freq.Monthly, false, 80.00, false);
      } else {
        jobGroupDto.addPlacementWithBudget(placements, 200.00, Freq.Monthly, false, 80.00, false);
      }
      try {
        JobGroup myJobGroup = driver.createJobGroup(jobGroupDto);
        dpList.add(
            List.of(
                TestUtils.getTestCase(myClient, jobGroupDto),
                clientDto1,
                myClient,
                jobGroupDto,
                myJobGroup,
                jobCreator,
                jobGroupDto.getPlacements().get(0).publisher,
                dtos.getBidLevel()));
        clientSet.add(myClient);
      } catch (MojoException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    try {
      MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
    } catch (Exception e) {
      ifSchedulerRan = false;
    }
    return new JobFilterData(clientSet, dpList, ifSchedulerRan);
  }

  /** . creating dtos class object for negative string rule operators */
  public static List<Dtos> createDtoListStringNegative(GroupOperator gp, BidLevel level) {
    List<Dtos> dtosList = new ArrayList<>();
    List<String> negativeStringRuleList = TestRunnerBase.getStringNegativeList();
    for (String rule : negativeStringRuleList) {
      ClientDto clientDto = ClientEntityCreator.randomClientCreator(false, 0.0);
      List<String> tempList = new ArrayList<>();
      tempList.add(rule);
      List<JobGroupDto> jobGroupList =
          JobGroupCreator.dtoUsingFilter(
              JobGroupFilterCreator.createFilterList(
                  TestRunnerBase.getJobFilterStringList(), tempList),
              gp,
              300,
              1,
              level);
      for (JobGroupDto jobGroupDto : jobGroupList) {
        dtosList.add(new Dtos(clientDto, null, jobGroupDto, level, Utils.getRandomNumber(2, 5)));
      }
    }
    return dtosList;
  }

  /** . creating dtos class object for positive string rule operators */
  public static List<Dtos> createDtoListStringPositive(GroupOperator gp, BidLevel level) {
    List<Dtos> dtosList = new ArrayList<>();
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(false, 0.0);
    List<JobGroupDto> jobGroupList =
        JobGroupCreator.dtoUsingFilter(
            JobGroupFilterCreator.createFilterList(
                TestRunnerBase.getJobFilterStringList(), TestRunnerBase.getStringPositiveList()),
            gp,
            300,
            1,
            level);
    for (JobGroupDto jobGroupDto : jobGroupList) {
      dtosList.add(new Dtos(clientDto, null, jobGroupDto, level, Utils.getRandomNumber(2, 5)));
    }
    return dtosList;
  }

  /** . creating dtos class object for DATE rule operators */
  public static List<Dtos> createDtoListDate(
      GroupOperator gp, BidLevel level, List<String> dateGroupList) {
    List<Dtos> dtosList = new ArrayList<>();
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(false, 0.0);
    List<JobGroupDto> jobGroupList =
        JobGroupCreator.dtoUsingFilter(
            JobGroupFilterCreator.createDateFilterList(
                TestRunnerBase.getJobFilterDateList(), dateGroupList),
            gp,
            300,
            1,
            level);
    for (JobGroupDto jobGroupDto : jobGroupList) {
      dtosList.add(new Dtos(clientDto, null, jobGroupDto, level, Utils.getRandomNumber(2, 5)));
    }
    return dtosList;
  }
}
