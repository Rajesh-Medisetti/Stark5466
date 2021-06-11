package model;

import base.OurWebDriver;
import helpers.Month;
import helpers.UiUtils;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;
import validations.UiCalendarValidation;

public class Calendar {

  OurWebDriver driver;
  String xpath;
  String dateRangePicker;
  String prevMonth;
  String thisMonth;
  String prevMonthButton;
  String nextMonthButton;
  String prevMonthYear;
  String thisMonthYear;
  String dataRange;
  String cancel;
  String apply;

  /** Consructor. */
  public Calendar(OurWebDriver driver, String xpath) throws InterruptedException {
    this.driver = driver;
    this.xpath = xpath;
    this.dateRangePicker =
        xpath + "/div/jv-ngx-daterangepicker-material/div/div[1]/mat-list/mat-list-item";
    this.prevMonth = xpath + "/div/jv-ngx-daterangepicker-material/div/div[2]";
    this.thisMonth = xpath + "/div/jv-ngx-daterangepicker-material/div/div[3]";
    this.prevMonthButton =
        xpath + "/div/jv-ngx-daterangepicker-material/div/" + "div[2]/div/table/thead/tr[1]/th[1]";
    this.nextMonthButton =
        xpath + "/div/jv-ngx-daterangepicker-material/div/" + "div[3]/div/table/thead/tr[1]/th[3]";
    this.prevMonthYear =
        xpath + "/div/jv-ngx-daterangepicker-material/div/div[2]" + "/div/table/thead/tr[1]/th[2]";
    this.thisMonthYear =
        xpath + "/div/jv-ngx-daterangepicker-material/div/div[3]/" + "div/table/thead/tr[1]/th[2]";
    this.dataRange = "date-range__control";
    this.cancel = xpath + "/div/jv-ngx-daterangepicker-material/div/div[4]/div/button[1]";
    this.apply = xpath + "/div/jv-ngx-daterangepicker-material/div/div[4]/div/button[2]";
    UiUtils.createDataForCalendar();
    driver.waitForAngular();
    driver.findElement(By.xpath(xpath)).click();
  }

  /** checks if the no. of future dates are disabled. */
  public void isFutureDatesDisabled(SoftAssert softAssert, ZoneId zoneId) {

    List<WebElement> dates =
        driver
            .findElement(By.xpath(thisMonth))
            .findElement(By.tagName("tbody"))
            .findElements(By.tagName("td"));

    boolean result = true;

    LocalDate localDate = LocalDate.now(zoneId);

    for (WebElement element : dates) {

      if ((element.getAttribute("class").contains("off")
              || Integer.parseInt(element.getText()) <= localDate.getDayOfMonth())
          && element.getAttribute("class").contains("available")) {
        continue;
      } else {
        if (!element.getAttribute("class").contains("disabled")) {
          result = false;
          break;
        }
      }
    }
    softAssert.assertTrue(result, "isFutureDatesDisabled Test Failed");
  }

  /** checks if future month is disabled. */
  public void isFutureMonthDisabled(SoftAssert softAssert) {

    softAssert.assertTrue(
        !driver.findElement(By.xpath(nextMonthButton)).getAttribute("class").contains("available"),
        "isFutureMonthDisabled Test Failed");
  }

  /** checks the custom range test scenario. */
  public void checkCustomRanges(
      LocalDate start, LocalDate end, SoftAssert softAssert, ZoneId zoneId)
      throws InterruptedException {
    goToStartMonth(start, zoneId);

    selectDate(start, thisMonth);

    goToCurrentMonth(true, end);
    driver.click(By.xpath(apply));

    driver.click(By.xpath(xpath));

    driver.click(By.xpath(prevMonthButton));

    List<Integer> dateList = new ArrayList<>();
    while (driver
        .findElement(By.xpath(nextMonthButton))
        .getAttribute("class")
        .contains("available")) {

      dateList.addAll(new ArrayList<>(checkDates(thisMonth)));
      driver.click(By.xpath(nextMonthButton));
    }

    dateList.addAll(new ArrayList<>(checkDates(thisMonth)));

    String datePickerText = driver.findElement(By.xpath(dateRangePicker + "[8]")).getText();

    List<LocalDate> localDates = new ArrayList<>();

    localDates.add(start);
    localDates.add(end);

    softAssert.assertEquals("Custom Range", datePickerText, "test1 failed for Custom Range");

    softAssert.assertTrue(
        driver
            .findElement(By.xpath(dateRangePicker + "[8]"))
            .getAttribute("class")
            .contains("selected"),
        "test2 failed for Custom Range");

    softAssert.assertEquals(
        UiUtils.getNoOfDays(localDates), dateList.size(), "test3 failed for Custom Range");

    softAssert.assertTrue(
        UiCalendarValidation.isDatesValid(dateList, start, end), "test4 failed for Custom Range");
  }

  /** to go to the current month. */
  private void goToCurrentMonth(boolean selectEndDate, LocalDate date) throws InterruptedException {

    while (driver
        .findElement(By.xpath(nextMonthButton))
        .getAttribute("class")
        .contains("available")) {

      if (selectEndDate && selectDate(date, this.thisMonth)) {
        return;
      }
      driver.click(By.xpath(nextMonthButton));
    }

    if (selectEndDate) {
      selectDate(date, this.thisMonth);
    }
  }

  /** to go to the starting month. */
  private void goToStartMonth(LocalDate start, ZoneId zoneId) {
    LocalDate localDate = LocalDate.now(zoneId);
    int startMonthValue = start.getMonthValue();
    int prevMonthValue = localDate.minusMonths(1).getMonthValue();
    int startYear = start.getYear();
    int currentYear = localDate.getYear();

    prevMonthValue += (currentYear - startYear) * 12;

    while (prevMonthValue - startMonthValue >= 0) {

      driver.findElement(By.xpath(prevMonthButton)).click();

      prevMonthValue--;
    }
  }

  /** to go to the selected date. */
  private boolean selectDate(LocalDate date, String month) throws InterruptedException {

    String monthYear = driver.findElement(By.xpath(thisMonthYear)).getText();

    int monthValue = new Month().map.get(monthYear.substring(0, 3));

    int yearValue = Integer.parseInt(monthYear.substring(4));

    if (date.getMonthValue() != monthValue || date.getYear() != yearValue) {
      return false;
    }

    List<WebElement> dates =
        driver
            .findElement(By.xpath(month))
            .findElement(By.tagName("tbody"))
            .findElements(By.tagName("td"));

    for (WebElement element : dates) {
      if (!element.getAttribute("class").contains("off")
          && element.getText().equals(Integer.toString(date.getDayOfMonth()))) {

        driver.wait(element);
        element.click();
        return true;
      }
    }
    return true;
  }

  /** to verify all the dates. */
  private List<Integer> checkDates(String month) {

    List<WebElement> dates =
        driver
            .findElement(By.xpath(month))
            .findElement(By.tagName("tbody"))
            .findElements(By.tagName("td"));

    List<Integer> list = new ArrayList<>();

    for (WebElement element : dates) {
      if (!element.getAttribute("class").contains("off")
          && (element.getAttribute("class").contains("in-range")
              || element.getAttribute("class").contains("start-date")
              || element.getAttribute("class").contains("end-date"))) {
        list.add(Integer.parseInt(element.getText()));
      }
    }
    return list;
  }

  /** check the date ranger. */
  public void checkDateRangePickerInterval(SoftAssert softAssert, ZoneId zoneId)
      throws InterruptedException {
    LocalDate localDate = LocalDate.now(zoneId);
    String dateRangeText;
    boolean isSelected = false;
    for (int i = 1; i <= 7; i++) {
      String xpath = dateRangePicker + "[" + i + "]";

      driver.click(By.xpath(xpath));

      String text = driver.findElement(By.className(dataRange)).getText();

      System.out.print(
          text
              + ", "
              + UiUtils.calendarText.get(i - 1)
              + ","
              + text.equals(UiUtils.calendarText.get(i - 1)));

      //  softAssert.assertEquals(UiUtils.calendarText.get(i - 1), text,
      //        "test1 failed for " + UiUtils.dateRangeText.get(i - 1));

      driver.click(By.xpath(this.xpath));

      if (localDate.getDayOfWeek().toString().equals("SUNDAY")
              && driver.findElement(By.xpath(xpath)).getText().equals("This Week")
          || localDate.getDayOfMonth() == 1) {

        dateRangeText = driver.findElement(By.xpath(xpath)).getText();

        xpath = dateRangePicker + "[1]";

        isSelected = driver.findElement(By.xpath(xpath)).getAttribute("class").contains("selected");

      } else {
        dateRangeText = driver.findElement(By.xpath(xpath)).getText();

        isSelected = driver.findElement(By.xpath(xpath)).getAttribute("class").contains("selected");
      }

      List<Integer> dates = new ArrayList<>();

      dates.addAll(new ArrayList<>(checkDates(prevMonth)));
      dates.addAll(new ArrayList<>(checkDates(thisMonth)));

      softAssert.assertEquals(
          UiUtils.dateRangeText.get(i - 1),
          dateRangeText,
          "test2 failed for " + UiUtils.dateRangeText.get(i - 1));

      softAssert.assertEquals(
          UiUtils.getNoOfDays(UiUtils.dateRange.get(UiUtils.dateRangeText.get(i - 1))),
          dates.size(),
          "test3 failed for " + UiUtils.dateRangeText.get(i - 1));

      softAssert.assertTrue(isSelected, "test4 failed for " + UiUtils.dateRangeText.get(i - 1));

      softAssert.assertTrue(
          UiCalendarValidation.isDatesValid(
              dates,
              UiUtils.dateRange.get(UiUtils.dateRangeText.get(i - 1)).get(0),
              UiUtils.dateRange.get(UiUtils.dateRangeText.get(i - 1)).get(1)),
          "test5 failed for" + UiUtils.dateRangeText.get(i - 1));
    }
  }
}
