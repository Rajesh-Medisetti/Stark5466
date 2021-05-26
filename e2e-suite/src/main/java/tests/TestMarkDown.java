package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dataproviders.JobFilterDP;
import dataproviders.MarkDownDP;
import entitycreators.JobCreator;
import enums.BidLevel;
import helpers.MojoUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import validations.OutBoundJobCpcValidation;

public class TestMarkDown extends TestRunnerBase {

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
    MarkDownDP.markDowndata =
        jobfilterObj.createJobFilterData(driver, new MarkDownDP().getMarkDownList(), true);
  }

  @Test(dataProvider = "MarkDown", dataProviderClass = MarkDownDP.class)
  public void markDownTestCases(
      String testCase,
      ClientDto clientDto,
      Client client,
      JobGroupDto jobGroupDto,
      JobGroup jobGroupObj,
      JobCreator jobCreator,
      String publisher,
      BidLevel bidLevel)
      throws MojoException, InterruptedException {

    String cpc = "0";
    if (bidLevel.equals(BidLevel.PLACEMENT)) {
      cpc = jobGroupDto.getPlacements().get(0).bid.toString();
    } else {
      cpc = jobGroupObj.getStats().getCpcBid();
    }
    double markDown = 50.0;
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

  /**
   * teardown function deletes all the clients that are created.
   *
   * @throws MojoException mojo exception
   */
  @AfterClass
  public void tearDown() throws MojoException {

    MojoUtils.removeClientSet(MarkDownDP.clientSet);

    for (Client client : MarkDownDP.clientSet) {
      MojoUtils.removeInboundFeed(client.getInboundFeeds(), driver);
    }
  }
}
