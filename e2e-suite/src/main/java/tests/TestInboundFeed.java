package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.mojo.OutboundFeed;
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
    //InboundDp.titleAndRefList = InboundDp.setDiffTitleAndRef(driver);
    InboundDp.newJobsToInboundEntity = MojoUtils.createClientAlongWithCampaignAndJobGroup(driver,
        JobFilterDP.placements, BidLevel.PLACEMENT);
//    InboundDp.removedJobsToInboundEntity = MojoUtils.createClientAlongWithCampaignAndJobGroup(driver,
//        JobFilterDP.placements, BidLevel.PLACEMENT);
    InboundDp.runSchedulers(driver);
  }

  //@Test(dataProvider = "test", dataProviderClass = InboundDp.class)
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
   // int jobsInOutBound = allEntities.getClient().getOutboundFeed(publisher).getFeed().getJobs().size();
    if (!isReqSame) {
      //softAssert.assertEquals(jobsInOutBound, initialJobs + jobsAdded);

      softAssert.assertEquals(true, InboundVsOutboundFeedValidations.checkJobWithFields(
          allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()),
          allEntities.getClient(),
          JobFilterDP.placements)
      );

    } else {
      //softAssert.assertEquals(jobsInOutBound, initialJobs);

      FeedDto feedDto = InboundVsOutboundFeedValidations.getUpdatedFeedDto(allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()));
      softAssert.assertEquals(true, InboundVsOutboundFeedValidations.checkJobWithFields(
          feedDto,
          allEntities.getClient(),
          JobFilterDP.placements)
      );
    }
  }

  @SuppressWarnings({"checkstyle:Indentation", "checkstyle:CommentsIndentation"})
  @Test
  public void testAddJobsToInboundFeed()
      throws MojoException, InterruptedException {

    AllEntities entitiesOfClientForJobGroup = InboundDp.addNewJobsScenario(driver);
   // OutboundFeed outboundFeed = entitiesOfClientForJobGroup.getClient().getOutboundFeed(JobFilterDP.placements);
    SoftAssert softAssert = new SoftAssert();

    //check for jobs count between jobs in outbound feed vs Inbound Feed Dto.
//    softAssert.assertEquals(
//        entitiesOfClientForJobGroup.getJobCreator().clientFeedMap
//            .get(entitiesOfClientForJobGroup.getClientDto()).getJob().size(),
//        outboundFeed.getFeed().getJobs().size());
    //Validate each field in every Inbound Feed job with outbound job.
    softAssert.assertEquals(true, InboundVsOutboundFeedValidations.checkJobWithFields(
                          entitiesOfClientForJobGroup.getJobCreator().clientFeedMap
                              .get(entitiesOfClientForJobGroup.getClientDto()),
                          entitiesOfClientForJobGroup.getClient(),
                          JobFilterDP.placements)
        );
  }

  //@Test
  public void testRemoveJobsFromInboundFeed()
      throws MojoException, InterruptedException {

    AllEntities allEntities = InboundDp.removeJobsScenario(driver);
    //OutboundFeed outboundFeed = allEntities.getClient().getOutboundFeed(JobFilterDP.placements);

    SoftAssert softAssert = new SoftAssert();
//    softAssert.assertEquals(allEntities.getJobCreator().clientFeedMap
//            .get(allEntities.getClientDto()).getJob().size(),
//        outboundFeed.getFeed().getJobs().size());

    softAssert.assertEquals(true, InboundVsOutboundFeedValidations.checkJobWithFields(
        allEntities.getJobCreator().clientFeedMap.get(allEntities.getClientDto()),
        allEntities.getClient(),
        JobFilterDP.placements)
    );
  }

  @AfterClass
  public void tearDown() {}
}
