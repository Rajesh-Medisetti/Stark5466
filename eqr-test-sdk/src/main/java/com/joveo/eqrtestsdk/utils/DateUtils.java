package com.joveo.eqrtestsdk.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  private static DateTimeFormatter mojoFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public static String formatAsMojoDate(LocalDate date) {
    return date.format(mojoFormat);
  }

  public static LocalDate startOfMonth(LocalDate date) {
    return date.withDayOfMonth(1);
  }
}
