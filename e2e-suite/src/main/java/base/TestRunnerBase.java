package base;

import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import com.joveo.eqrtestsdk.models.RuleOperator;
import enums.RuleOperatorStringNegative;
import enums.RuleOperatorStringPositive;
import enums.RuleOperatorsDate;
import enums.RuleOperatorsNumber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunnerBase {
  public static Driver driver = null;

  public static void createDriver() throws MojoException {
    driver = Driver.start("reliability@joveo.com", "joveo1520", JoveoEnvironment.Staging);
  }

  /**
   * creates a list of all number supported operators.
   *
   * @return list of all operators
   */
  public static ArrayList<String> getNumberList() {
    ArrayList<String> cpcOperatorList = new ArrayList<>();
    for (RuleOperatorsNumber r : RuleOperatorsNumber.values()) {
      cpcOperatorList.add(r.toString());
    }
    return cpcOperatorList;
  }

  /**
   * creates a list of all string supported operators.
   *
   * @return list of all operators
   */
  public static ArrayList<String> getStringPositiveList() {
    ArrayList<String> stringOperatorList = new ArrayList<String>();
    for (RuleOperatorStringPositive r : RuleOperatorStringPositive.values()) {
      stringOperatorList.add(r.toString());
    }
    return stringOperatorList;
  }

  /**
   * creates a list of all string supported operators.
   *
   * @return list of all operators
   */
  public static ArrayList<String> getStringNegativeList() {
    ArrayList<String> stringOperatorList = new ArrayList<>();
    for (RuleOperatorStringNegative r : RuleOperatorStringNegative.values()) {
      stringOperatorList.add(r.toString());
    }
    return stringOperatorList;
  }

  /**
   * creates a list of all date supported operators.
   *
   * @return list of all operators
   */
  public static ArrayList<String> getDateList() {
    ArrayList<String> dateOperatorList = new ArrayList<>();
    for (RuleOperatorsDate r : RuleOperatorsDate.values()) {
      dateOperatorList.add(r.toString());
    }
    return dateOperatorList;
  }

  /**
   * return date operators as list of groups.
   *
   * @return list of list of date operators
   */
  public static List<List<RuleOperator>> getDateGroups() {
    List<List<RuleOperator>> dateGroups =
        Arrays.asList(
            Arrays.asList(RuleOperator.GREATER_THAN, RuleOperator.LESS_THAN, RuleOperator.BETWEEN),
            Arrays.asList(RuleOperator.ON, RuleOperator.BEFORE, RuleOperator.AFTER));

    return dateGroups;
  }

  /**
   * creates a list of all date supported filters for string.
   *
   * @return list of all filters
   */
  public static List<JobFilterFields> getJobFilterStringList() {
    List<JobFilterFields> stringJobFilter = new ArrayList<>();
    stringJobFilter.add(JobFilterFields.state);
    stringJobFilter.add(JobFilterFields.city);
    stringJobFilter.add(JobFilterFields.title);
    stringJobFilter.add(JobFilterFields.type);
    stringJobFilter.add(JobFilterFields.category);
    stringJobFilter.add(JobFilterFields.company);
    stringJobFilter.add(JobFilterFields.zip);
    stringJobFilter.add(JobFilterFields.country);
    return stringJobFilter;
  }

  /**
   * creates a list of all date supported filters for Date.
   *
   * @return list of all filters
   */
  public static List<JobFilterFields> getJobFilterDateList() {
    List<JobFilterFields> stringJobFilter = new ArrayList<>();
    stringJobFilter.add(JobFilterFields.postedDate);
    return stringJobFilter;
  }

  /**
   * creates a list of all date supported filters for Numeric.
   *
   * @return list of all filters
   */
  public static List<JobFilterFields> getJobFilterNumberList() {
    List<JobFilterFields> stringJobFilter = new ArrayList<>();
    stringJobFilter.add(JobFilterFields.cpcBid);
    stringJobFilter.add(JobFilterFields.refNumber);
    return stringJobFilter;
  }
}
