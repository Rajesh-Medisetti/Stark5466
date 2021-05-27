package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.FeedDto;
import dataproviders.InboundDp;
import dataproviders.JobFilterDP;
import dtos.AllEntities;
import enums.BidLevel;
import helpers.MojoUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import validations.InboundVsOutboundFeedValidations;

public class TestInboundFeed extends TestRunnerBase {
  /**
   * Intial set up for Inbound feed testing.
   *
   * @throws MojoException Mojo Exception
   * @throws InterruptedException Interrupted Exception
   */
  @BeforeClass
  public void setUp() throws MojoException, InterruptedException {
    if (driver == null) {
      TestRunnerBase.createDriver();
    }
    InboundDp.titleAndRefList = InboundDp.setDiffTitleAndRef(driver);

    InboundDp.newJobsToInboundEntity =
        MojoUtils.createClientAlongWithCampaignAndJobGroup(
            driver, JobFilterDP.placements, BidLevel.PLACEMENT);

    InboundDp.clientsUsedForInboundTest.add(InboundDp.newJobsToInboundEntity.getClient());
    InboundDp.inboundFeedUrlsUsedForTest.add(
        InboundDp.newJobsToInboundEntity
            .getJobCreator()
            .clientUrlMap
            .get(InboundDp.newJobsToInboundEntity.getClientDto()));

    InboundDp.removedJobsToInboundEntity =
        MojoUtils.createClientAlongWithCampaignAndJobGroup(
            driver, JobFilterDP.placements, BidLevel.PLACEMENT);
    InboundDp.clientsUsedForInboundTest.add(InboundDp.removedJobsToInboundEntity.getClient());
    InboundDp.inboundFeedUrlsUsedForTest.add(
        InboundDp.removedJobsToInboundEntity
            .getJobCreator()
            .clientUrlMap
            .get(InboundDp.removedJobsToInboundEntity.getClientDto()));

    InboundDp.runSchedulers(driver);
  }

  @Test(dataProvider = "test", dataProviderClass = InboundDp.class)
  public void testRefAndTitle(
      Client client,
      Boolean isTitleSame,
      Boolean isReqSame,
      AllEntities allEntities,
      String publisher,
      int initialJobs,
      int jobsAdded)
      throws MojoException, InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    if (!isReqSame) {
      InboundVsOutboundFeedValidations.checkJobWithFields(
          allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()),
          allEntities.getClient(),
          JobFilterDP.placements,
          softAssert);
    } else {
      FeedDto feedDto =
          InboundVsOutboundFeedValidations.getUpdatedFeedDto(
              allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()));
      InboundVsOutboundFeedValidations.checkJobWithFields(
          feedDto, allEntities.getClient(), JobFilterDP.placements, softAssert);
    }
  }

  @Test
  public void testAddJobsToInboundFeed() throws MojoException, InterruptedException {

    AllEntities allEntities = InboundDp.addNewJobsScenario(driver);

    SoftAssert softAssert = new SoftAssert();

    InboundVsOutboundFeedValidations.checkJobWithFields(
        allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()),
        allEntities.getClient(),
        JobFilterDP.placements,
        softAssert);
  }

  @Test
  public void testRemoveJobsFromInboundFeed() throws MojoException, InterruptedException {

    AllEntities allEntities = InboundDp.removeJobsScenario(driver);

    SoftAssert softAssert = new SoftAssert();

    InboundVsOutboundFeedValidations.checkJobWithFields(
        allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()),
        allEntities.getClient(),
        JobFilterDP.placements,
        softAssert);
  }

  @AfterClass
  public void tearDown() throws MojoException {
    MojoUtils.removeClientSet(InboundDp.clientsUsedForInboundTest);
    MojoUtils.removeInboundFeed(InboundDp.inboundFeedUrlsUsedForTest, driver);
  }
}
