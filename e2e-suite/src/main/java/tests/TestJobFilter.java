package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.Freq;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.JobFilterDP;
import entitycreators.CampaignEntityCreator;
import entitycreators.ClientEntityCreator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestJobFilter extends TestRunnerBase {
  Client globalClient;
  Campaign globalCampaign;
  String feed = "https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml";
  String placements = "Test1";
  boolean ifSchedulerRan = true;
  Set<Client> clientSet = new HashSet<Client>();
  static Object[][] arr;
  static List<List<Object>> dpList = new ArrayList<>();

  /**
   * craetes the data for the thing. it runs before every class.
   *
   * @throws MojoException exceprion
   */
  @BeforeClass
  public void createData() throws MojoException {
    if (null == driver) {
      createDriver();
    }
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(feed);
    globalClient = driver.createClient(clientDto);
    CampaignDto campaignDto = CampaignEntityCreator.randomCampaignCreator(1000);
    campaignDto.setClientId(globalClient.id);
    globalCampaign = driver.createCampaign(campaignDto);
    arr = JobFilterDP.dpMethod();
    for (int row = 0; row < arr.length; row++) {
      JobGroupDto jobGroupDto = new JobGroupDto();
      jobGroupDto = (JobGroupDto) arr[row][2];
      Client myClient = null;
      Campaign myCampaign = null;
      myClient = globalClient;
      myCampaign = globalCampaign;
      ClientDto clientDto1 = (ClientDto) arr[row][0];
      CampaignDto campaignDto1 = (CampaignDto) arr[row][1];
      if (null != clientDto1) {
        myClient = driver.createClient(clientDto);
      }
      if (null != campaignDto1) {
        campaignDto.setClientId(globalClient.id);
        myCampaign = driver.createCampaign(campaignDto);
      }
      jobGroupDto.setPriority(1);
      jobGroupDto.setClientId(myClient.id);
      jobGroupDto.setCampaignId(myCampaign.id);
      jobGroupDto.addPlacementWithBudgetAndBid(
          placements, 1.80, 200.00, Freq.Monthly, false, 80.00, false);
      try {
        JobGroup myJobGroup = driver.createJobGroup(jobGroupDto);
        dpList.add(List.of(myClient, jobGroupDto, myJobGroup));

        clientSet.add(myClient);

      } catch (MojoException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    try {
      runScheduler(clientSet);
    } catch (Exception e) {
      ifSchedulerRan = false;
    }
  }

  /**
   * Runs scheduler on all the clients passed to it.
   *
   * @param clientSet Set of all client objects
   * @throws TimeoutException exception timeout
   * @throws InterruptWaitException exception wait
   * @throws MojoException mojo exception
   */
  public static void runScheduler(Set<Client> clientSet)
      throws TimeoutException, InterruptWaitException, MojoException {
    System.out.println("client size is " + clientSet.size());
    for (Client client : clientSet) {
      System.out.println(client.getStats().getId());
      //      client.runScheduler();
    }
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
    System.out.println("out of dp too");
    return array;
  }

  @Test(dataProvider = "test")
  public void test1To1JobFilters(Client clientObj, JobGroupDto jobGroupDto, JobGroup jobGroupObj)
      throws UnexpectedResponseException, InvalidInputException, ApiRequestException, MojoException,
          InterruptedException, IOException {
    // refreshJobFeed();
    System.out.println("refreshh");
    // Thread.sleep(30000);
    driver.refreshEntityCache();
    driver.refreshJobCount();
    Thread.sleep(3000);
    Assert.assertTrue(ifSchedulerRan, "Scheduler run failed");
    Assert.assertEquals(
        jobGroupObj.getStats().getJobCount(),
        6,
        "The job count is not correct for client"
            + clientObj.id
            + "and job group "
            + jobGroupObj.getStats().getName());
  }

  /**
   * used to refresh the job feed.
   *
   * @throws IOException exception IO
   * @throws InterruptedException itneruupted exception
   */
  public void refreshJobFeed() throws IOException, InterruptedException {
    String command = "curl --location --request POST 'mojo2.staging.joveo.com/api/cache/refresh'";
    Process p = Runtime.getRuntime().exec(command);
    System.out.println("refresh job feed");
    Thread.sleep(1000);
    System.out.println(p.getOutputStream());
  }

  /**
   * teardown function deletes all the clients that are created.
   *
   * @throws APIRequestException exception api one
   * @throws UnexpectedResponseException unexpected response exceptions
   * @throws MojoException mojo exception
   */
  @AfterClass
  public void tearDown() throws ApiRequestException, UnexpectedResponseException, MojoException {
    for (Client client : clientSet) {
      client.removeClient();
    }
  }
}
