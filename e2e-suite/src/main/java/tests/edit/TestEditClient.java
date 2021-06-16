package tests.edit;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.JobFilterDP;
import dataproviders.editdp.EditClientDP;
import entitycreators.JobCreator;
import enums.BidLevel;
import enums.Scheduler;
import helpers.MojoUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import validations.EditClientValidations;
import validations.OutBoundJobCpcValidation;

public class TestEditClient extends TestRunnerBase {

  /**
   * craetes the data for the thing. it runs before every class.
   *
   * @throws MojoException exception
   */
  @BeforeClass
  public static void beforeClass() throws MojoException, InterruptedException {
    if (null == driver) {
      createDriver();
    }

    for (Scheduler scheduler : Scheduler.values()) {
      EditClientDP.addFeedDataProvider(driver, scheduler);
      EditClientDP.removeFeedDataProvider(driver, scheduler);
      EditClientDP.editMarkDownDataProvider(driver, scheduler);
    }
    EditClientDP.editClientBudgetDataProvider(driver);

    EditClientDP.runScheduler(driver);
  }

  @Test(dataProvider = "addFeed", dataProviderClass = EditClientDP.class)
  public void addFeedTestCases(FeedDto feedDto1, FeedDto feedDto2, Client client, String pubId)
      throws MojoException, InterruptedException {
    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertTrue(JobFilterDP.ifSchedulerRan, "Scheduler run failed");

    Assert.assertEquals(
        (feedDto1.getJob().size() + feedDto2.getJob().size()),
        client.getOutboundFeed(pubId).size());

    Assert.assertTrue(
        EditClientValidations.checkInboundJobsInOIutBoundWithJobRefNo(feedDto1, client, pubId));
    Assert.assertTrue(
        EditClientValidations.checkInboundJobsInOIutBoundWithJobRefNo(feedDto2, client, pubId),
        "createFeed jobs is not present in OutBoundFeed ");
  }

  @Test(dataProvider = "removeFeed", dataProviderClass = EditClientDP.class)
  public void removeFeedTestCases(FeedDto feed, Client client, String pubId)
      throws MojoException, InterruptedException {
    SoftAssert softAssertion = new SoftAssert();
    System.out.println(" ---- " + client.id);

    Assert.assertTrue(JobFilterDP.ifSchedulerRan, "Scheduler run failed");

    Assert.assertEquals(
        feed.getJob().size(), client.getOutboundFeed(pubId).getFeed().getJobs().size());

    Assert.assertTrue(
        EditClientValidations.checkInboundJobsInOIutBoundWithJobRefNo(feed, client, pubId),
        "Edit jobs is not present in OutBoundFeed ");
  }

  @Test(dataProvider = "editMarkDown", dataProviderClass = EditClientDP.class)
  public void editMarkDownTestCases(
      Double markDown,
      Client client,
      JobGroupDto jobGroupDto,
      JobGroup jobGroupObj,
      JobCreator jobCreator,
      BidLevel bidLevel)
      throws MojoException, InterruptedException {

    String cpc = "0";
    if (bidLevel.equals(BidLevel.PLACEMENT)) {
      cpc = jobGroupDto.getPlacements().get(0).bid.toString();
    } else {
      cpc = jobGroupObj.getStats().getCpcBid();
    }

    Assert.assertTrue(
        new OutBoundJobCpcValidation()
            .getJobLevelCpc(
                client,
                jobGroupDto.getPlacements().get(0).publisher,
                jobCreator,
                jobGroupDto,
                jobGroupObj,
                ((1.0 - (markDown / 100.0)) * Double.parseDouble(cpc))),
        "cpc is not valid");
  }

  @Test(dataProvider = "editClientBudget", dataProviderClass = EditClientDP.class)
  public void editClientBudgetTestCases(Client client, Double budget)
      throws MojoException, InterruptedException {

    System.out.println(client.getStats().getBudgetCap().value + ", " + budget);
    Assert.assertEquals(client.getStats().getBudgetCap().value, budget);
  }

  /**
   * teardown function deletes all the clients that are created.
   *
   * @throws ApiRequestException exception api one
   * @throws UnexpectedResponseException unexpected response exceptions
   * @throws MojoException mojo exception
   */
  @AfterClass
  public void tearDown() throws MojoException {

    MojoUtils.removeClientSet(EditClientDP.clientSet);

    for (Client client : EditClientDP.clientSet) {
      MojoUtils.removeInboundFeed(client.getInboundFeeds(), driver);
    }
  }
}
