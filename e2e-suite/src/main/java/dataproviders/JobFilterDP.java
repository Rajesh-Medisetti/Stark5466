package dataproviders;

import com.joveo.eqrtestsdk.models.GroupOperator;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import entitycreators.JobGroupCreator;
import entitycreators.JobGroupFilterCreator;
import java.util.List;
import org.testng.annotations.DataProvider;

public class JobFilterDP {

  /**
   * the data provider for 1-1 mapping of job group and job filter. If client DTO is passed as null
   * it will use the existing one.
   *
   * @return 2d object of dp.
   */
  @DataProvider(name = "test-jobFilter-jobgroup")
  public static Object[][] dpMethod() {
    // List<JobGroupDTO> jobGroupList =
    // JobGroupCreator.dTOUsingFilter(JobGroupFilterCreator.createSingleFilterList(),
    // GroupOperator.AND,300,1);
    List<JobGroupDto> jobGroupList =
        JobGroupCreator.dtoUsingFilter(
            JobGroupFilterCreator.createOnlyOneCaseList(), GroupOperator.AND, 300, 1);

    return jobGroupList.stream()
        .map(filter -> new Object[] {null, null, filter})
        .toArray(Object[][]::new);
  }
}
