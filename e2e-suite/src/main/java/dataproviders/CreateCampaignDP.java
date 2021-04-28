package dataproviders;

import org.testng.annotations.DataProvider;

public class CreateCampaignDP {
  /**
   * DataProvider for campaign creation. Dummy as of now.
   *
   * @return 2d object
   */
  @DataProvider(name = "poc-test-campaign")
  public static Object[][] dpMethod() {
    return new Object[][] {
      {"campaign1", "03/04/2021", "06/04/2021", 1000, "Monthly"},
      {"campaign2", "03/04/2021", "02/04/2021", 900, "Monthly"}
    };
  }
}
