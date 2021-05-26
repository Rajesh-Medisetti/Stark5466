package entitycreators;

import com.joveo.eqrtestsdk.models.Filter;
import com.joveo.eqrtestsdk.models.Freq;
import com.joveo.eqrtestsdk.models.GroupOperator;
import com.joveo.eqrtestsdk.models.GroupingJobFilter;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobGroupCreator {

  /**
   * One on one mapping of job group DTO with job filter. this doess't set the id's for client and
   * campaign.
   *
   * @param jbList list of job filter, it is expected to create one job groupDTO for one filter
   *     list.
   * @param budget max budget that need to be set.
   * @param bidIncrement how much the bid should increment each time
   * @return list of job group DTO's that has been set.
   */
  public static List<JobGroupDto> dtoUsingFilter(
      List<List<Filter>> jbList, GroupOperator groupOper, double budget, double bidIncrement) {
    double counter = 0.0;
    List<JobGroupDto> resultList = new ArrayList();
    for (List<Filter> jf : jbList) {
      JobGroupDto jobGroupDto = new JobGroupDto();
      JobFilter jfEle = (JobFilter) jf.get(0);
      jobGroupDto.setName(
          jfEle.getField().toString() + " is " + jfEle.getOperator() + " to " + jfEle.getData());
      jobGroupDto.setCpcBid(counter + bidIncrement);
      jobGroupDto.setCpaBid(counter + bidIncrement + 0.5);
      jobGroupDto.setBudgetCap(false, Freq.Monthly, 80.00, budget);
      jobGroupDto.setJobFilter(new GroupingJobFilter(groupOper, jf));
      counter++;
      resultList.add(jobGroupDto);
    }
    return resultList;
  }

  /**
   * One on one mapping of job group DTO with job filter operator as equal.
   *
   * @return list of job group DTO's that has been set.
   */
  public static JobGroupDto dtoWithEqual(
      JobFilterFields field, String searchString, double budget, double bidIncrement) {
    double counter = 0.0;
    List<JobGroupDto> resultList = new ArrayList();
    List<Filter> jf = new ArrayList<>();
    JobGroupDto jobGroupDto = new JobGroupDto();
    JobFilter jfEle = (JobFilter) JobFilter.eq(field, searchString);
    jf.add(jfEle);
    jobGroupDto.setName(
        jfEle.getField().toString() + " is " + jfEle.getOperator() + " to " + jfEle.getData());
    jobGroupDto.setCpcBid(counter + bidIncrement);
    jobGroupDto.setCpaBid(counter + bidIncrement + 0.5);
    jobGroupDto.setBudgetCap(false, Freq.Monthly, 80.00, budget);
    jobGroupDto.setJobFilter(new GroupingJobFilter(GroupOperator.OR, jf));

    counter++;
    return jobGroupDto;
  }

  /**
   * One on one mapping of job group DTO with job filter operator as In.
   *
   * @return list of job group DTO's that has been set.
   */
  public static JobGroupDto dtoWithIN(
      JobFilterFields field, List<String> searchString, double budget, double bidIncrement) {
    double counter = 0.0;
    List<JobGroupDto> resultList = new ArrayList();
    List<Filter> jf = new ArrayList<>();
    JobGroupDto jobGroupDto = new JobGroupDto();
    JobFilter jfEle = (JobFilter) JobFilter.in(field, searchString);
    jf.add(jfEle);
    jobGroupDto.setName(
        jfEle.getField().toString() + " is " + jfEle.getOperator() + " to " + jfEle.getData());
    jobGroupDto.setCpcBid(counter + bidIncrement);
    jobGroupDto.setCpaBid(counter + bidIncrement + 0.5);
    jobGroupDto.setBudgetCap(false, Freq.Monthly, 80.00, budget);
    jobGroupDto.setJobFilter(new GroupingJobFilter(GroupOperator.OR, jf));

    counter++;
    return jobGroupDto;
  }

  /**
   * create a job group dto with custom start and end date.
   *
   * @param startDate start date
   * @param endDate end date
   * @return job group dto
   */
  public static JobGroupDto dtoWithDate(
      JobFilterFields field, String searchString, LocalDate startDate, LocalDate endDate) {
    JobGroupDto jobGroupDto = new JobGroupDto();
    JobFilter jfEle = (JobFilter) JobFilter.eq(field, searchString);
    jobGroupDto.setName(
        jfEle.getField().toString() + " is " + jfEle.getOperator() + " to " + jfEle.getData());
    jobGroupDto.setJobFilter(
        new GroupingJobFilter(GroupOperator.OR, Collections.singletonList(jfEle)));
    jobGroupDto.setBudgetCap(false, Freq.Monthly, 80.00, 300.0);
    jobGroupDto.setStartDate(startDate);
    jobGroupDto.setEndDate(endDate);
    return jobGroupDto;
  }
}
