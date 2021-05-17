package helpers;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.models.Filter;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import java.util.List;

public class TestUtils {

  /** . TestCases. */
  public static String getTestCase(Client myClient, JobGroupDto jobGroupDto) {
    String testCase =
        " Testing Job Filters client id is "
            + myClient.id
            + " xthe operator field is "
            + jobGroupDto.getFilter().getOperator();
    List<Filter> filterList = jobGroupDto.getFilter().getRules();
    testCase =
        testCase
            + " \n  the number of rules are "
            + filterList.size()
            + ". \n  Following are rules : ";
    for (Filter fil : filterList) {
      JobFilter jfEle = (JobFilter) fil;
      testCase =
          testCase
              + "\n The filter field is "
              + jfEle.getField()
              + " the filter attribute is "
              + jfEle.getOperator()
              + " the filter data is "
              + jfEle.getData();
    }
    return testCase;
  }
}
