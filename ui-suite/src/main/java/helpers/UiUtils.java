package helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class UiUtils {

  public static LinkedHashMap<String, List<LocalDate>> dateRange = new LinkedHashMap<>();

  public static List<String> dateRangeText = new ArrayList<>();

  public static List<String> calendarText = new ArrayList<>();

  public static String calculatorXPATH =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content"
          + "/jv-content-viewer/jv-entity/div/div[1]/jv-date-range";

  /** creates data for calendar test scenarios. */
  public static void createDataForCalendar() {

    List<LocalDate> dateList = new ArrayList<>();
    LocalDate date = LocalDate.now();
    dateList.add(LocalDate.now());
    dateList.add(LocalDate.now());
    dateRange.put("Today", new ArrayList<>(dateList));
    dateList.clear();

    dateList.add(LocalDate.now().minusDays(1));
    dateList.add(LocalDate.now().minusDays(1));
    dateRange.put("Yesterday", new ArrayList<>(dateList));
    dateList.clear();

    dateList.add(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)));

    dateList.add(LocalDate.now());
    dateRange.put("This Week", new ArrayList<>(dateList));
    dateList.clear();

    dateList.add(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusWeeks(1));

    dateList.add(LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY)));

    dateRange.put("Last Week", new ArrayList<>(dateList));
    dateList.clear();

    dateList.add(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));

    dateList.add(LocalDate.now());

    dateRange.put("This Month", new ArrayList<>(dateList));
    dateList.clear();

    dateList.add(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1));

    dateList.add(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusDays(1));
    dateRange.put("Last Month", new ArrayList<>(dateList));
    dateList.clear();

    dateList.add(LocalDate.now().minusDays(30));
    dateList.add(LocalDate.now().minusDays(1));

    dateRange.put("Last 30 Days", new ArrayList<>(dateList));
    dateList.clear();

    Set<String> keys = dateRange.keySet();

    dateRangeText.addAll(keys);

    calendarText = getCalendarRangeTexts();
  }

  /**
   * returns ranges for the calendar.
   *
   * @return
   */
  private static List<String> getCalendarRangeTexts() {

    List<String> list = new ArrayList<>();

    list.add("Today");
    list.add("Yesterday");

    if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
      list.add("Today");
    } else {
      list.add("This Week");
    }

    list.add("Last Week");

    if (LocalDate.now().getDayOfMonth() == 1) {
      list.add("Today");
    } else {
      list.add("This Month");
    }

    list.add("Last Month");

    list.add("Last 30 Days");

    return list;
  }

  /** gives no of days. */
  public static int getNoOfDays(List<LocalDate> localDates) {

    return (int) ChronoUnit.DAYS.between(localDates.get(0), localDates.get(1)) + 1;
  }
}
