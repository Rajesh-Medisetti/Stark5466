package tests;

import base.OurWebDriver;
import base.TestRunnerBaseUI;
import dataproviders.CalendarDP;
import enums.ui.BrowserType;
import enums.ui.ClientTabs;
import java.time.LocalDate;
import java.time.ZoneId;
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
    ZoneId zoneId = ZoneId.of("Asia/Kolkata");
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new Homepage(driver).landAtClientHomepage();
    Calendar calendar = new Calendar(driver, dashboardCalendar);
    LocalDate localDate = LocalDate.now(zoneId);
    calendar.checkCustomRanges(localDate.minusMonths(7), localDate, softAssert, zoneId);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    calendar.isFutureDatesDisabled(softAssert, zoneId);
    calendar.isFutureMonthDisabled(softAssert);
    softAssert.assertAll();
  }

  @Test
  public static void testCalendarAtPublisherHomepage() throws InterruptedException {
    ZoneId zoneId = ZoneId.of("Asia/Kolkata");
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new Homepage(driver).landAtPublisherHomepage();
    Calendar calendar = new Calendar(driver, dashboardCalendar);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    LocalDate localDate = LocalDate.now(zoneId);
    calendar.checkCustomRanges(localDate.minusMonths(7), localDate, softAssert, zoneId);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    calendar.isFutureDatesDisabled(softAssert, zoneId);
    calendar.isFutureMonthDisabled(softAssert);
    softAssert.assertAll();
  }

  @Test(dataProvider = "myDataProvider", dataProviderClass = CalendarDP.class)
  public static void testCalendarAtCampaignHomepage(String clientName, ZoneId zoneId)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient(clientName, ClientTabs.CAMPAIGN);
    Calendar calendar = new Calendar(driver, dashboardCalendar);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    LocalDate localDate = LocalDate.now(zoneId);
    calendar.checkCustomRanges(localDate.minusMonths(7), localDate, softAssert, zoneId);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    calendar.isFutureDatesDisabled(softAssert, zoneId);
    calendar.isFutureMonthDisabled(softAssert);
    softAssert.assertAll();
  }

  @Test(dataProvider = "myDataProvider", dataProviderClass = CalendarDP.class)
  public static void testCalendarAtJobGroupHomepage(String clientName, ZoneId zoneId)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient(clientName, ClientTabs.JOBGROUPS);
    Calendar calendar = new Calendar(driver, dashboardCalendar);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    LocalDate localDate = LocalDate.now(zoneId);
    calendar.checkCustomRanges(localDate.minusMonths(7), localDate, softAssert, zoneId);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    calendar.isFutureDatesDisabled(softAssert, zoneId);
    calendar.isFutureMonthDisabled(softAssert);
    softAssert.assertAll();
  }

  @Test(dataProvider = "myDataProvider", dataProviderClass = CalendarDP.class)
  public static void testCalendarAtJobsHomepage(String clientName, ZoneId zoneId)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient(clientName, ClientTabs.JOBS);
    Calendar calendar = new Calendar(driver, dashboardCalendar);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    LocalDate localDate = LocalDate.now(zoneId);
    calendar.checkCustomRanges(localDate.minusMonths(7), localDate, softAssert, zoneId);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    calendar.isFutureDatesDisabled(softAssert, zoneId);
    calendar.isFutureMonthDisabled(softAssert);
    softAssert.assertAll();
  }

  @Test(dataProvider = "myDataProvider", dataProviderClass = CalendarDP.class)
  public static void testCalendarAtPlacementsHomepage(String clientName, ZoneId zoneId)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(BrowserType.CHROME);
    driverList.add(driver);
    new ClientPage(driver).goToTabUnderClient(clientName, ClientTabs.PLACEMENTS);
    Calendar calendar = new Calendar(driver, dashboardCalendar);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    LocalDate localDate = LocalDate.now(zoneId);
    calendar.checkCustomRanges(localDate.minusMonths(7), localDate, softAssert, zoneId);
    calendar.checkDateRangePickerInterval(softAssert, zoneId);
    calendar.isFutureDatesDisabled(softAssert, zoneId);
    calendar.isFutureMonthDisabled(softAssert);
    softAssert.assertAll();
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
