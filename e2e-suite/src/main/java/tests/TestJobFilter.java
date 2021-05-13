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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
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
    JobFilterDP.createJobFilterData(driver);
  }

  @Test(dataProvider = "test", dataProviderClass = JobFilterDP.class)
  public void test1To1JobFilters(
      String testCase,
      ClientDto clientDto,
      Client clientObj,
      JobGroupDto jobGroupDto,
      JobGroup jobGroupObj,
      JobCreator jobCreator,
      String pubId)
      throws MojoException {
    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertTrue(JobFilterDP.ifSchedulerRan, "Scheduler run failed");
    softAssertion.assertEquals(
        jobGroupObj.getStats().getJobCount(),
        jobCreator.jobsInJobGroup.get(jobGroupDto).size(),
        "The job count is not correct for client "
            + clientObj.id
            + " and job group "
            + jobGroupObj.getStats().getName());
    softAssertion.assertTrue(
        JobFilterValidations.checkJobWithRefNo(
            clientDto, clientObj, jobGroupDto, jobGroupObj, pubId, jobCreator),
        "jobRefId is not present in OutBoundFeed");
    softAssertion.assertTrue(
        JobFilterValidations.checkJobWithFields(
            clientDto, clientObj, jobGroupDto, jobGroupObj, pubId, jobCreator),
        "Job values is not equal in outboundJob");

    //// if sent at cpc -  priority
    //    //then
    // if(jobGroupDto.getPlacements().get(0).bid > 0 ) {}
    //   //this is final bid
    // else
    // {
    //   jobGroupObj.getStats().getCpcBid();
    // }

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
    JobFilterDP.removeClientSet();
  }
}
