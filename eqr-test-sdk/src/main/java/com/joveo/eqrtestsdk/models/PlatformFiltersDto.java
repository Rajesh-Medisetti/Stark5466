package com.joveo.eqrtestsdk.models;

import static com.joveo.eqrtestsdk.utils.DateUtils.formatAsMojoDate;
import static com.joveo.eqrtestsdk.utils.DateUtils.startOfMonth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.internal.util.classhierarchy.Filters;

@JsonSerialize
public class PlatformFiltersDto {

  public Filters filters;
  public int page;
  public int limit;
  public boolean sortOrderAsc;
  public String sortBy;

  /** Creating the PlatformFilter. */
  public PlatformFiltersDto() {
    this.page = 1;
    this.limit = 2000;
    this.sortOrderAsc = false;
    this.sortBy = "spent";
    this.filters = new Filters();
  }

  public PlatformFiltersDto(String id) {
    this();
    this.filters = new Filters(id, startOfMonth(LocalDate.now()), LocalDate.now());
  }

  public PlatformFiltersDto(String id, LocalDate startDate, LocalDate endDate) {
    this();
    this.filters = new Filters(id, startDate, endDate);
  }

  public void setPage(int page) {
    this.page = page;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  /** Adding Rule. */
  public <T> void addRule(T data, PFfields field, PfOperators operator) {
    if (operator.equals(PfOperators.IN)) {
      this.filters.addRule(Collections.singletonList(data), field, operator);
    } else {
      this.filters.addRule(data, field, operator);
    }
  }

  public static class Filters {
    @NotNull(message = "operator can't be null")
    public PfOperators operator;

    public List<Rules> rules;

    public Filters() {
      this.operator = PfOperators.AND;
      this.rules = new ArrayList<Rules>();
    }

    /** Filter with startDate,EndDate. */
    public Filters(String clientId, LocalDate startDate, LocalDate endDate) {
      this();
      List<String> clientIds = Arrays.asList(clientId);
      List<Rules> newRules =
          Arrays.asList(
              new Rules(formatAsMojoDate(startDate), PFfields.startDate, PfOperators.EQUAL),
              new Rules(formatAsMojoDate(endDate), PFfields.endDate, PfOperators.EQUAL),
              new Rules(clientIds, PFfields.clientId, PfOperators.IN));
      this.rules = newRules;
    }

    public <T> void addRule(T data, PFfields field, PfOperators operator) {
      this.rules.add(new Rules(data, field, operator));
    }

    public static class Rules<T> {
      public PfOperators operator;
      public PFfields field;
      public T data;

      /** Setting The Rules. */
      public Rules(T data, PFfields field, PfOperators operator) {
        this.operator = operator;
        this.field = field;
        this.data = data;
      }
    }
  }
}
