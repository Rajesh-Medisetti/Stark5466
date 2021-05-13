package entitycreators;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.models.Filter;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.RuleOperator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobGroupFilterCreator extends TestRunnerBase {

  /**
   * The list of all the combinations of job filters(just one filter) possible as per the values of
   * rule operator and operand. The value is a combination of rule operator and operand used.
   *
   * @return list of list of filter objects. where every nested list has just one rule.
   */
  public static List<List<Filter>> createSingleFilterList() {
    Map<String, List<String>> extendedMap = createExcptionMap();
    List<List<Filter>> jfList = new ArrayList();
    for (JobFilterFields jf : JobFilterFields.values()) {
      List<String> rulesList = extendedMap.get(jf.toString());
      for (RuleOperator ro : RuleOperator.values()) {
        if (rulesList.contains(ro.toString())) {
          ArrayList<Filter> indvList = new ArrayList();
          putRules(jf, ro, indvList, jfList);
        }
      }
    }
    return jfList;
  }

  /**
   * creating date filters.
   *
   * @param ruleOperators list of operators.
   * @return list of list of filter
   */
  public static List<List<Filter>> createDateFilterList(List<RuleOperator> ruleOperators) {
    Map<String, List<String>> extendedMap = createExcptionMap();
    List<List<Filter>> jfList = new ArrayList();
    for (JobFilterFields jf : JobFilterFields.values()) {
      List<String> rulesList = extendedMap.get(jf.toString());
      for (RuleOperator ro : ruleOperators) {
        if (rulesList.contains(ro.toString())) {
          ArrayList<Filter> indvList = new ArrayList();
          putDateRules(jf, ro, indvList, jfList);
        }
      }
    }
    return jfList;
  }

  /**
   * The list of all the combinations of job filters(just one filter) possible as per the values of
   * rule operator and operand.The value is a combination of rule operator and operand used.
   *
   * @return list of list of filter objects.where every nested list has just one rule.
   */
  public static List<List<Filter>> createFilterList(
      List<JobFilterFields> filterList, List<String> rulesList) {
    List<List<Filter>> jfList = new ArrayList();
    for (JobFilterFields jf : JobFilterFields.values()) {
      for (RuleOperator ro : RuleOperator.values()) {
        if (filterList.contains(jf)) {
          if (rulesList.contains(ro.toString())) {
            ArrayList<Filter> indvList = new ArrayList();
            putRules(jf, ro, indvList, jfList);
          }
        }
      }
    }
    return jfList;
  }

  /**
   * assign date rules.
   *
   * @param jf job filter filed
   * @param ro rule operator
   * @param indvList list
   * @param jfList job filter list
   */
  public static void putDateRules(
      JobFilterFields jf, RuleOperator ro, ArrayList<Filter> indvList, List<List<Filter>> jfList) {

    switch (ro) {
      case GREATER_THAN:
      case AFTER:
        indvList.add(new JobFilter<>(ro, jf, LocalDate.now().minusDays(30)));
        jfList.add(indvList);
        break;

      case LESS_THAN:
      case BEFORE:
        indvList.add(new JobFilter<>(ro, jf, LocalDate.now().plusDays(30)));
        jfList.add(indvList);
        break;

      case ON:
        indvList.add(new JobFilter<>(ro, jf, LocalDate.now()));
        jfList.add(indvList);
        break;

      case BETWEEN:
        ArrayList<LocalDate> datesRange = new ArrayList<>();
        datesRange.add(LocalDate.now().minusDays(25));
        datesRange.add(LocalDate.now().plusDays(25));
        indvList.add(new JobFilter<>(ro, jf, datesRange));
        break;

      default:
        break;
    }
  }

  /**
   * creating another method for job filter and rule operator mapping.
   *
   * @param jf job filer
   * @param ro rule operator
   * @param indvList list of filters
   * @param jfList returnee list of filters
   */
  public static void putRules(
      JobFilterFields jf, RuleOperator ro, ArrayList<Filter> indvList, List<List<Filter>> jfList) {
    if ((ro.toString().equals(RuleOperator.BETWEEN.toString())
        || ro.toString().equals(RuleOperator.IN.toString())
        || ro.toString().equals(RuleOperator.NOT_IN.toString()))) {
      List<String> tlist = new ArrayList<>();
      tlist.add(ro.toString() + "_" + jf.toString());
      indvList.add(new JobFilter<>(ro, jf, tlist));
    } else {
      indvList.add(new JobFilter<>(ro, jf, ro.toString() + "_" + jf.toString()));
    }
    jfList.add(indvList);
  }

  /**
   * CREATES one case rule of city equals hyderabad.
   *
   * @return list of list of filter objects.
   */
  public static List<List<Filter>> createOnlyOneCaseList() {
    List<List<Filter>> jfList = new ArrayList();
    ArrayList<Filter> indvList = new ArrayList();
    indvList.add(new JobFilter<>(RuleOperator.EQUAL, JobFilterFields.city, "Hyderabad"));
    jfList.add(indvList);

    return jfList;
  }

  /**
   * Creates a map of relationships between job filter and operators.
   *
   * @return a Map with the job filter field and their supported operators in String.
   */
  public static Map<String, List<String>> createExcptionMap() {

    Map<String, List<String>> extendedMap = new HashMap<>();
    for (JobFilterFields jf : JobFilterFields.values()) {
      if (jf.toString().equals(JobFilterFields.cpcBid.toString())) {
        extendedMap.put(jf.toString(), getNumberList());
      } else if (jf.toString().equals(JobFilterFields.postedDate.toString())) {
        extendedMap.put(jf.toString(), getDateList());
      } else {
        extendedMap.put(jf.toString(), getStringPositiveList());
      }
    }
    return extendedMap;
  }
}
