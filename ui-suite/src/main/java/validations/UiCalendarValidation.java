package validations;

import java.time.LocalDate;
import java.util.List;

public class UiCalendarValidation {
  public static boolean isDatesValid(List<Integer> dates, LocalDate start, LocalDate end) {
    int index = 0;
    while (start.compareTo(end) <= 0) {
      if (index == dates.size() || dates.get(index) != start.getDayOfMonth()) {
        return false;
      }
      start = start.plusDays(1);
      index++;
    }
    return dates.size() == index;
  }
}
