package dataproviders;

import org.testng.annotations.DataProvider;

import java.time.ZoneId;

/** class for calendar data provider. */
public class CalendarDP {

    @DataProvider(name="myDataProvider", parallel = true)
    public static Object[][] myDataProvider() {
        return new Object[][]
                {
                        {"Ned", ZoneId.of("Asia/Kolkata")},
//                        {"Ned",ZoneId.of("IST")},
//                        {"Ned",ZoneId.of("IST")},
                        {"UIAutomationData_PrevDay",ZoneId.of("America/Chicago")}

                };

    }

}
