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
import enums.BidType;
import helpers.MojoUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import validations.EditJobGroupValidations;
import validations.JobFilterValidations;
import validations.OutBoundJobCpcValidation;

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
    EditJobGroupDP.checkCpcBidInOutBoundFeedAfterEdit(driver);
    EditJobGroupDP.checkBidsAfterEdit(driver);
    EditJobGroupDP.checkJobFilterAfterEdit(driver);

    EditJobGroupDP.runScheduler(driver);
  }

  @Test(dataProvider = "editBids", dataProviderClass = EditJobGroupDP.class)
  public void testBidsAfterEdit(JobGroup jobGroup, Double newBid, BidType bidType)
      throws MojoException {

    SoftAssert softAssert = new SoftAssert();

    if (bidType.equals(BidType.CPC)) {
      softAssert.assertEquals(newBid, Double.parseDouble(jobGroup.getStats().getCpcBid()));
    }

    if (bidType.equals(BidType.CPA)) {
      softAssert.assertEquals(newBid, Double.parseDouble(jobGroup.getStats().getCpaBid()));
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

    SoftAssert softAssert = new SoftAssert();
    softAssert.assertTrue(
        new OutBoundJobCpcValidation()
            .getJobLevelCpc(client, publisher, jobCreator, jobGroupDto, jobGroup, newCpcBid),
        "cpc is not valid in Jobgroup after edit" + jobGroup.id);
  }

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

    SoftAssert softAssert = new SoftAssert();

    softAssert.assertEquals(
        jobGroup.getStats().getJobCount(),
        jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob().size());

    softAssert.assertTrue(
        new EditJobGroupValidations().isJobsInJobGroup(jobGroup, driver, jobGroupDto, jobCreator),
        "required jobs are not in jobGroup for JobGroupId " + jobGroup.id);
    softAssert.assertTrue(
        JobFilterValidations.isJobLive(
            clientDto, client, jobGroupDto, jobGroup, pubId, jobCreator, driver),
        "jobs are not live for JobGroupId" + " " + jobGroup.id);
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
