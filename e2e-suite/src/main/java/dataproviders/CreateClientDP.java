package dataproviders;

import org.testng.annotations.DataProvider;

public class CreateClientDP {

  @DataProvider(name = "poc-test-statusCode")
  public static Object[][] dpMethod() {
    return new Object[][] {{"test_automation", 200}, {"test_automation", 200}};
  }
}
