package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.DateDP;
import dataproviders.JobFilterDP;
import entitycreators.JobCreator;
import enums.BidLevel;
import helpers.MojoUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestDate extends TestRunnerBase {

  /**
   * crates the data for the thing. it runs before every class.
   *
   * @throws MojoException exception
   */
  @BeforeClass
  public static void beforeClass() throws MojoException {
    if (null == driver) {
      createDriver();
    }
    JobFilterDP jobFilter = new JobFilterDP();
    DateDP.data1to1 =
        jobFilter.createJobFilterData(driver, new DateDP().getDateCombination(), false);
  }

  @Test(dataProvider = "test", dataProviderClass = DateDP.class)
  public void test1To1DateFilters(
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
    softAssertion.assertNull(clientObj.getOutboundFeed(pubId));
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
    MojoUtils.removeClientSet(DateDP.clientSet);
    for (Client client : DateDP.clientSet) {
      MojoUtils.removeInboundFeed(client.getInboundFeeds(), driver);
    }
  }
}
