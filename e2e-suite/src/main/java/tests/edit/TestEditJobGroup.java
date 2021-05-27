package tests.edit;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.editdp.EditJobGroupDP;
import entitycreators.JobCreator;
import helpers.MojoUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import validations.EditJobGroupValidations;
import validations.JobFilterValidations;

public class TestEditJobGroup extends TestRunnerBase {

  /**
   * craetes the data for the thing. it runs before every class.
   *
   * @throws MojoException exception
   */
  @BeforeClass
  public static void beforeClass() throws MojoException {
    if (null == driver) {
      createDriver();
    }
    //  EditJobGroupDP.checkCpcBidInOutBoundFeedAfterEdit(driver);
    // EditJobGroupDP.checkBidsAfterEdit(driver);
    EditJobGroupDP.checkJobFilter(driver);
    EditJobGroupDP.checkJobFilterAfterEdit(driver);

    EditJobGroupDP.runScheduler(driver);
  }

  /* @Test(dataProvider = "editBids", dataProviderClass = EditJobGroupDP.class)
  public void testBidsAfterEdit(JobGroup jobGroup, Double newBid, BidType bidType)
      throws MojoException {

    if (bidType.equals(BidType.CPC)) {
      Assert.assertEquals(newBid, Double.parseDouble(jobGroup.getStats().getCpcBid()));
    }

    if (bidType.equals(BidType.CPA)) {
      Assert.assertEquals(newBid, Double.parseDouble(jobGroup.getStats().getCpaBid()));
    }
  }

  @Test(dataProvider = "editCpcBids", dataProviderClass = EditJobGroupDP.class)
  public void checkOutBoundCpcAfterEdit(
      JobGroup jobGroup,
      Double newCpcBid,
      Client client,
      String publisher,
      JobGroupDto jobGroupDto,
      JobCreator jobCreator)
      throws MojoException, InterruptedException {

    Assert.assertTrue(
        new OutBoundJobCpcValidation()
            .getJobLevelCpc(client, publisher, jobCreator, jobGroupDto, jobGroup, newCpcBid));
  }*/

  @Test(dataProvider = "editJobFilter", dataProviderClass = EditJobGroupDP.class)
  public void checkJobsAfterJobFilterEdit(
      JobGroup jobGroup,
      Driver driver,
      JobGroupDto jobGroupDto,
      JobCreator jobCreator,
      Client client,
      ClientDto clientDto,
      String pubId)
      throws MojoException, InterruptedException {

    Assert.assertEquals(
        jobGroup.getJobs(1, 100).size(),
        jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob().size());
    Assert.assertTrue(
        new EditJobGroupValidations().isJobsInJobGroup(jobGroup, driver, jobGroupDto, jobCreator));
    Assert.assertTrue(
        JobFilterValidations.isJobLive(
            clientDto, client, jobGroupDto, jobGroup, pubId, jobCreator, driver));
  }

  /**
   * teardown function deletes all the clients that are created.
   *
   * @throws MojoException mojo exception
   */
  @AfterClass
  public void tearDown() throws MojoException {

    MojoUtils.removeClientSet(EditJobGroupDP.clientSet);

    for (Client client : EditJobGroupDP.clientSet) {
      MojoUtils.removeInboundFeed(client.getInboundFeeds(), driver);
    }
  }
}
