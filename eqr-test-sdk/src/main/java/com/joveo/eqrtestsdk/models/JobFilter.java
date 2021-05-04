package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joveo.eqrtestsdk.models.validationgroups.EditJobGroup;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

public class JobFilter<T> implements Filter {

  private RuleOperator operator;

  @NotNull(message = "field in a rule can't be null")
  private JobFilterFields field;

  @NotNull(message = "data in rule can't be null")
  private T data;

  /** *Create a Rule. */
  public JobFilter(RuleOperator operator, JobFilterFields field, T data) {
    this.operator = operator;
    this.field = field;
    this.data = data;
  }

  public JobFilterFields getField() {
    return field;
  }

  public void setField(JobFilterFields field) {
    this.field = field;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setOperator(RuleOperator operator) {
    this.operator = operator;
  }

  public RuleOperator getOperator() {
    return operator;
  }

  public static Filter eq(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.EQUAL, fieldName, value);
  }

  public static Filter notEq(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.NOT_EQUAL, fieldName, value);
  }

  public static GroupingJobFilter and(Filter... jobFilters) {
    return new GroupingJobFilter(GroupOperator.AND, Arrays.asList(jobFilters));
  }

  public static GroupingJobFilter or(Filter... jobFilters) {
    return new GroupingJobFilter(GroupOperator.OR, Arrays.asList(jobFilters));
  }

  public static Filter in(JobFilterFields fieldName, List<String> value) {
    return new JobFilter<>(RuleOperator.IN, fieldName, value);
  }

  public static Filter notIn(JobFilterFields fieldName, List<String> value) {
    return new JobFilter<>(RuleOperator.NOT_IN, fieldName, value);
  }

  public static Filter beginWith(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.BEGINS_WITH, fieldName, value);
  }

  public static Filter notBeginWith(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.NOT_BEGINS_WITH, fieldName, value);
  }

  public static Filter contains(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.CONTAINS, fieldName, value);
  }

  public static Filter notContains(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.NOT_CONTAINS, fieldName, value);
  }

  public static Filter endWith(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.ENDS_WITH, fieldName, value);
  }

  public static Filter notEndWith(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.NOT_ENDS_WITH, fieldName, value);
  }

  public static Filter greaterThan(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.GREATER_THAN, fieldName, value);
  }

  public static Filter greaterThanEqual(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.GREATER_THAN_EQUAL, fieldName, value);
  }

  public static Filter lessThan(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.LESS_THAN, fieldName, value);
  }

  public static Filter lessThanEqual(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.LESS_THAN_EQUAL, fieldName, value);
  }

  public static Filter on(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.ON, fieldName, value);
  }

  public static Filter before(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.BEFORE, fieldName, value);
  }

  public static Filter after(JobFilterFields fieldName, String value) {
    return new JobFilter<>(RuleOperator.AFTER, fieldName, value);
  }

  public static Filter between(JobFilterFields fieldName, List<String> value) {
    return new JobFilter<>(RuleOperator.BETWEEN, fieldName, value);
  }

  /**
   * .
   *
   * @return checkingRules
   */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  @JsonIgnore
  @AssertTrue(
      message =
          "Invalid Rule- check Rules(Check compatibility of fields with operators),"
              + "(dataType of values"
              + " can only be list(string) or string,(in,between,notIn will take a list of string),"
              + "between operator will take take list(2 strings)",
      groups = {EditJobGroup.class, Default.class})
  public boolean isValidRule() {

    if (isInvalidFieldData()) {
      return false;
    }

    List<JobFilterFields> jobFilterFields =
        Arrays.asList(
            JobFilterFields.category,
            JobFilterFields.country,
            JobFilterFields.state,
            JobFilterFields.city,
            JobFilterFields.zip,
            JobFilterFields.type,
            JobFilterFields.title,
            JobFilterFields.company,
            JobFilterFields.refNumber);

    List<RuleOperator> ruleOperatorsForJobFilterFields =
        Arrays.asList(
            RuleOperator.EQUAL,
            RuleOperator.NOT_EQUAL,
            RuleOperator.IN,
            RuleOperator.NOT_IN,
            RuleOperator.BEGINS_WITH,
            RuleOperator.NOT_BEGINS_WITH,
            RuleOperator.CONTAINS,
            RuleOperator.NOT_CONTAINS,
            RuleOperator.ENDS_WITH,
            RuleOperator.NOT_ENDS_WITH);

    if (isInvalidFieldOperator(jobFilterFields, ruleOperatorsForJobFilterFields)) {
      return false;
    }

    List<RuleOperator> ruleOperatorsForDate =
        Arrays.asList(
            RuleOperator.GREATER_THAN,
            RuleOperator.LESS_THAN,
            RuleOperator.ON,
            RuleOperator.BEFORE,
            RuleOperator.AFTER,
            RuleOperator.BETWEEN);

    if (isInvalidPostedDate(ruleOperatorsForDate)) {
      return false;
    }

    List<RuleOperator> ruleOperatorsForCpc =
        Arrays.asList(
            RuleOperator.GREATER_THAN,
            RuleOperator.LESS_THAN,
            RuleOperator.BETWEEN,
            RuleOperator.EQUAL,
            RuleOperator.GREATER_THAN_EQUAL,
            RuleOperator.LESS_THAN_EQUAL);

    if (isInvalidCpc(ruleOperatorsForCpc)) {
      return false;
    }

    if (operator.equals(RuleOperator.BETWEEN)) {
      return (((List<String>) data).size() == 2);
    }
    return true;
  }

  @JsonIgnore
  private boolean isInvalidCpc(List<RuleOperator> ruleOperatorsForCpc) {
    return field.equals(JobFilterFields.cpcBid) && !ruleOperatorsForCpc.contains(operator);
  }

  @JsonIgnore
  private boolean isInvalidPostedDate(List<RuleOperator> ruleOperatorsForDate) {

    return field.equals(JobFilterFields.postedDate) && !ruleOperatorsForDate.contains(operator);
  }

  @JsonIgnore
  private boolean isInvalidFieldOperator(
      List<JobFilterFields> jobFilterFields, List<RuleOperator> ruleOperatorsForJobFilterFields) {

    return jobFilterFields.contains(field) && !ruleOperatorsForJobFilterFields.contains(operator);
  }

  @JsonIgnore
  private boolean isInvalidFieldData() {
    return field == null || data == null;
  }

  /** . Validating Date Format. */
  @AssertTrue(
      message =
          "Invalid Date format or startDate>EndDate for between Operator,"
              + " date format must be MM/dd/yyyy in JobFilter, for MoreThan,"
              + "LessThan Input will be Numeric")
  @JsonIgnore
  public boolean isValidDate() {

    if (field != JobFilterFields.postedDate) {
      return true;
    }

    if (isOperatorOnBeforeAfter()) {
      return isValidDateFormat((String) data);
    } else if (operator == RuleOperator.BETWEEN) {
      List<String> list = (ArrayList) data;
      return isValidDates(list);
    } else {
      return isNumberValid((String) data);
    }
  }

  @JsonIgnore
  private boolean isValidDates(List<String> list) {

    return list.size() == 2
        && isValidDateFormat(list.get(0))
        && isValidDateFormat(list.get(1))
        && compareDates(list.get(0), list.get(1));
  }

  @JsonIgnore
  private boolean isOperatorOnBeforeAfter() {

    return operator == RuleOperator.ON
        || operator == RuleOperator.BEFORE
        || operator == RuleOperator.AFTER;
  }

  @JsonIgnore
  private boolean isValidDateFormat(String dateStr) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    boolean status;
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(dateStr);
      status = true;
    } catch (Exception e) {
      status = false;
    }
    return status;
  }

  @JsonIgnore
  private boolean compareDates(String start, String end) {

    LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    return startDate.compareTo(endDate) <= 0 && endDate.compareTo(LocalDate.now()) <= 0;
  }

  /** . Vlidating cpcBid in JobFilter */
  @AssertTrue(message = "InvalidCpc in JobFilter")
  @JsonIgnore
  private boolean isCpcValid() {

    if (field != JobFilterFields.cpcBid) {
      return true;
    }

    if (operator != RuleOperator.BETWEEN) {
      String number = (String) data;
      return isNumberValid(number);
    } else {
      List<String> list = (ArrayList) data;
      return list.size() == 2 && isNumberValid(list.get(0)) && isNumberValid(list.get(1));
    }
  }

  @JsonIgnore
  private boolean isNumberValid(String number) {
    String regex = "\\d+(\\.\\d+)?";
    return number.matches(regex);
  }
}
