package dataproviders;

import java.time.ZoneId;
import org.testng.annotations.DataProvider;

/** class for calendar data provider. */
public class CalendarDP {

  /** Data provider for calendar. */
  @DataProvider(name = "myDataProvider", parallel = true)
  public static Object[][] myDataProvider() {
    return new Object[][] {
      {"Ned", ZoneId.of("Asia/Kolkata")},
      //                        {"Ned",ZoneId.of("IST")},
      //                        {"Ned",ZoneId.of("IST")},
      {"UIAutomationData_PrevDay", ZoneId.of("America/Chicago")}
    };
  }
}
