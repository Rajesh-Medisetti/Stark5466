package base;

import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import enums.RuleOperatorString;
import enums.RuleOperatorsDate;
import enums.RuleOperatorsNumber;
import java.util.ArrayList;

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
    ArrayList<String> cpcOperatorList = new ArrayList<String>();
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
  public static ArrayList<String> getStringList() {
    ArrayList<String> stringOperatorList = new ArrayList<String>();
    for (RuleOperatorString r : RuleOperatorString.values()) {
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
    ArrayList<String> dateOperatorList = new ArrayList<String>();
    for (RuleOperatorsDate r : RuleOperatorsDate.values()) {
      dateOperatorList.add(r.toString());
    }
    return dateOperatorList;
  }
}
