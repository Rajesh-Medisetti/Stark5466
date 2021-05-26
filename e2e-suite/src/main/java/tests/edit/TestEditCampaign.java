package tests.edit;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.exception.MojoException;
import dataproviders.editdp.EditCampaignDP;
import helpers.MojoUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestEditCampaign extends TestRunnerBase {

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

    EditCampaignDP.editCampaignBudgetDataProvider(driver);
    EditCampaignDP.runScheduler(driver);
  }

  @Test(dataProvider = "editCampaignBudget", dataProviderClass = EditCampaignDP.class)
  public void campaignBudgetTestCases(Campaign campaign, Double budget)
      throws MojoException, InterruptedException {

    Assert.assertEquals(campaign.getStats().getBudgetCap().value, budget);
  }

  /**
   * teardown function deletes all the clients that are created.
   *
   * @throws MojoException mojo exception
   */
  @AfterClass
  public void tearDown() throws MojoException {

    MojoUtils.removeClientSet(EditCampaignDP.clientSet);

    for (Client client : EditCampaignDP.clientSet) {
      MojoUtils.removeInboundFeed(client.getInboundFeeds(), driver);
    }
  }
}
