package tests;

import base.OurWebDriver;
import base.TestRunnerBaseUI;
import enums.ui.BrowserType;
import enums.ui.ClientTabs;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Calendar;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ClientPage;
import pages.Homepage;

public class TestCalendar extends TestRunnerBaseUI {
  public static ArrayList<OurWebDriver> driverList = new ArrayList<>();
  public static String dashboardCalendar =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer"
          + "/jv-entity/div/div[1]/jv-date-range";

  @Test
  public static void testCalendarAtClientHomepage() throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new Homepage(driver).landAtClientHomepage();
    Calendar calendar = new Calendar(driver, dashboardCalendar);

    calendar.checkCustomRanges(LocalDate.now().minusMonths(7), LocalDate.now(), softAssert);

    calendar.checkDateRangePickerInterval(softAssert);

    calendar.isFutureDatesDisabled(softAssert);

    calendar.isFutureMonthDisabled(softAssert);
  }

  @Test
  public static void testCalendarAtPublisherHomepage() throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new Homepage(driver).landAtPublisherHomepage();
    Calendar cal = new Calendar(driver, dashboardCalendar);
    cal.checkDateRangePickerInterval(softAssert);
  }

  @Test
  public static void testCalendarAtCampaignHomepage() throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient("Ned", ClientTabs.CAMPAIGN);
    Calendar cal = new Calendar(driver, dashboardCalendar);
    cal.checkDateRangePickerInterval(softAssert);
  }

  @Test
  public static void testCalendarAtJobGroupHomepage() throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient("Ned", ClientTabs.JOBGROUPS);
    Calendar cal = new Calendar(driver, dashboardCalendar);
    cal.checkDateRangePickerInterval(softAssert);
  }

  @Test
  public static void testCalendarAtJobsHomepage() throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient("Ned", ClientTabs.JOBS);
    Calendar cal = new Calendar(driver, dashboardCalendar);
    cal.checkDateRangePickerInterval(softAssert);
  }

  @Test
  public static void testCalendarAtPlacementsHomepage() throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient("Ned", ClientTabs.PLACEMENTS);
    Calendar cal = new Calendar(driver, dashboardCalendar);
    cal.checkDateRangePickerInterval(softAssert);
  }

  /** removing all browsers. */
  @AfterClass
  public void removeDriver() {
    for (OurWebDriver driver : driverList) {
      System.out.println("innnn");
      driver.close();
      System.out.println("ouuut");
    }
  }
}
