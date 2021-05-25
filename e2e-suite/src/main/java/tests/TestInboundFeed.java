package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.exception.MojoException;
import dataproviders.InboundDp;
import dtos.AllEntities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
  }

  @Test(dataProvider = "test", dataProviderClass = InboundDp.class)
  public void testRefAndTitle(
      Boolean isTitleSame,
      Boolean isReqSame,
      AllEntities allEntities,
      String publisher,
      int initialJobs,
      int jobsAdded)
      throws MojoException {
    SoftAssert softAssert = new SoftAssert();
    int jobsInOutBound =
        allEntities.getClient().getOutboundFeed(publisher).getFeed().getJobs().size();
    if (!isReqSame) {
      softAssert.assertEquals(jobsInOutBound, initialJobs + jobsAdded);
    } else {
      softAssert.assertEquals(jobsInOutBound, initialJobs);
    }
  }

  public void testAddJobsToInboundFeed() {}

  public void testRemoveJobsFromInboundFeed() {}

  @AfterClass
  public void tearDown() {}
}
