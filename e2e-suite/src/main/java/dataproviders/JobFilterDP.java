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
import entitycreators.CampaignEntityCreator;
import entitycreators.ClientEntityCreator;
import entitycreators.JobCreator;
import entitycreators.JobGroupCreator;
import entitycreators.JobGroupFilterCreator;
import helpers.MojoUtils;
import helpers.TestUtils;
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
  static String placements = "Naukri";
  public static Set<Client> clientSet = new HashSet<>();
  static Object[][] arr;
  static List<List<Object>> dpList = new ArrayList<>();

  /** . return a list of Dtos. */
  public static List<Dtos> dpMethod1() {
    List<Dtos> dtosList = new ArrayList<>();
    dtosList.addAll(createDtoListStringNegative(GroupOperator.AND));
    dtosList.addAll(createDtoListStringNegative(GroupOperator.OR));
    dtosList.addAll(createDtoListStringPositive(GroupOperator.AND));
    dtosList.addAll(createDtoListStringPositive(GroupOperator.OR));
    return dtosList;
  }

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "test")
  public static Object[][] dpMethod() {
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
  public static void createJobFilterData(Driver driver) throws MojoException {
    final List<Dtos> dtosList = JobFilterDP.dpMethod1();
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(feed);
    clientDto.addFeed(feed);
    globalClient = driver.createClient(clientDto);
    ClientDto globalClientDto = clientDto;
    CampaignDto campaignDto = CampaignEntityCreator.randomCampaignCreator(1000);
    campaignDto.setClientId(globalClient.id);
    globalCampaign = driver.createCampaign(campaignDto);
    jobCreator = JobCreator.jobProvider(dtosList);
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
      jobGroupDto.addPlacementWithBudgetAndBid(
          placements, 1.80, 200.00, Freq.Monthly, false, 80.00, false);
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
                jobGroupDto.getPlacements().get(0).publisher));
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
  }

  /** . creating dtos class object for negative string rule operators */
  public static List<Dtos> createDtoListStringNegative(GroupOperator gp) {
    List<Dtos> dtosList = new ArrayList<>();
    List<String> negativeStringRuleList = TestRunnerBase.getStringNegativeList();
    for (String rule : negativeStringRuleList) {
      ClientDto clientDto = ClientEntityCreator.randomClientCreator("");
      List<String> tempList = new ArrayList<>();
      tempList.add(rule);
      List<JobGroupDto> jobGroupList =
          JobGroupCreator.dtoUsingFilter(
              JobGroupFilterCreator.createFilterList(
                  TestRunnerBase.getJobFilterStringList(), tempList),
              gp,
              300,
              1);
      for (JobGroupDto jobGroupDto : jobGroupList) {
        dtosList.add(new Dtos(clientDto, null, jobGroupDto));
      }
    }
    return dtosList;
  }

  /** . creating dtos class object for positive string rule operators */
  public static List<Dtos> createDtoListStringPositive(GroupOperator gp) {
    List<Dtos> dtosList = new ArrayList<>();
    ClientDto clientDto = ClientEntityCreator.randomClientCreator("");
    List<JobGroupDto> jobGroupList =
        JobGroupCreator.dtoUsingFilter(
            JobGroupFilterCreator.createFilterList(
                TestRunnerBase.getJobFilterStringList(), TestRunnerBase.getStringPositiveList()),
            gp,
            300,
            1);
    for (JobGroupDto jobGroupDto : jobGroupList) {
      dtosList.add(new Dtos(clientDto, null, jobGroupDto));
    }
    return dtosList;
  }
}
