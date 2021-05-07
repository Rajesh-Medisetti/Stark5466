package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.JobFilterDP;
import entitycreators.JobCreator;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
      Client clientObj,
      JobGroupDto jobGroupDto,
      JobGroup jobGroupObj,
      JobCreator jobCreator)
      throws MojoException {
    Assert.assertTrue(JobFilterDP.ifSchedulerRan, "Scheduler run failed");
    Assert.assertEquals(
        jobGroupObj.getStats().getJobCount(),
        jobCreator.jobsInJobGroup.get(jobGroupDto).size(),
        "The job count is not correct for client "
            + clientObj.id
            + " and job group "
            + jobGroupObj.getStats().getName());
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
