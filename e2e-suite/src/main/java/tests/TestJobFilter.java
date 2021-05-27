package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.JobFilterDP;
import entitycreators.JobCreator;
import enums.BidLevel;
import helpers.MojoUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import validations.EditJobGroupValidations;
import validations.JobFilterValidations;

public class TestJobFilter extends TestRunnerBase {

  /**
   * craetes the data for the thing. it runs before every class.
   *
   * @throws MojoException exceprion
   */
  @BeforeClass
  public static void beforeClass() throws MojoException {
    if (null == driver) {
      createDriver();
    }
    JobFilterDP jobfilterObj = new JobFilterDP();
    JobFilterDP.data1to1 = jobfilterObj.createJobFilterData(driver, JobFilterDP.dpMethod1(), true);
  }

  @Test(dataProvider = "test", dataProviderClass = JobFilterDP.class)
  public void test1To1JobFilters(
      String testCase,
      ClientDto clientDto,
      Client clientObj,
      JobGroupDto jobGroupDto,
      JobGroup jobGroupObj,
      JobCreator jobCreator,
      String pubId,
      BidLevel bidLevel)
      throws MojoException, InterruptedException {
    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertTrue(JobFilterDP.ifSchedulerRan, "Scheduler run failed");
    softAssertion.assertEquals(
        jobGroupObj.getStats().getJobCount(),
        jobCreator.jobGroupDtoFeedDtoMap.get(jobGroupDto).getJob().size(),
        "The job count is not correct for client "
            + clientObj.id
            + " and job group "
            + jobGroupObj.getStats().getName());
    if (bidLevel.equals(BidLevel.NO_BID)) {
      softAssertion.assertFalse(
          JobFilterValidations.checkJobWithRefNo(
              clientDto, clientObj, jobGroupDto, jobGroupObj, pubId, jobCreator),
          "jobRefId is present in OutBoundFeed for bid zero");
      // need to check job is not live too
    } else {
      softAssertion.assertTrue(
          JobFilterValidations.checkJobWithRefNo(
              clientDto, clientObj, jobGroupDto, jobGroupObj, pubId, jobCreator),
          "jobRefId is not present in OutBoundFeed");
      softAssertion.assertTrue(
          JobFilterValidations.checkJobWithFields(
              clientDto, clientObj, jobGroupDto, jobGroupObj, pubId, jobCreator),
          "Job values is not equal in outboundJob");
      JobFilterValidations.checkBid(
          clientObj, pubId, bidLevel, jobGroupObj, jobGroupDto, jobCreator, softAssertion);

      softAssertion.assertTrue(
          new EditJobGroupValidations().isJobsInJobGroup(jobGroupObj, driver, jobGroupDto, jobCreator),
          "required jobs are not in jobGroup for JobGroupId " + jobGroupObj.id);
      softAssertion.assertTrue(
          JobFilterValidations.isJobLive(
              clientDto, clientObj, jobGroupDto, jobGroupObj, pubId, jobCreator, driver),
          "jobs are not live for JobGroupId" + " " + jobGroupObj.id);
    }
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
    MojoUtils.removeClientSet(JobFilterDP.clientSet);
    for (Client client : JobFilterDP.clientSet) {
      MojoUtils.removeInboundFeed(client.getInboundFeeds(), driver);
    }
  }
}
