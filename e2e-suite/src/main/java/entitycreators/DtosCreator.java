package entitycreators;

import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.Filter;
import com.joveo.eqrtestsdk.models.GroupOperator;
import com.joveo.eqrtestsdk.models.GroupingJobFilter;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.RuleOperator;
import dtos.Dtos;
import enums.BidLevel;
import helpers.Utils;
import java.util.ArrayList;
import java.util.List;

public class DtosCreator {

  /** . Return a pair of DTOs for editEntities tests */
  public List<Dtos> getDtos() {

    List<List<Filter>> listList = getJobFilters(2, RuleOperator.EQUAL, JobFilterFields.country);

    final ClientDto createClientDto = ClientEntityCreator.randomClientCreator("");

    final CampaignDto createCampaignDto = CampaignEntityCreator.randomCampaignCreator(1000.0);
    final CampaignDto editCampaignDto = new CampaignDto();

    final JobGroupDto createJobGroupDto = new JobGroupDto();

    final JobGroupDto editJobGroupDto = new JobGroupDto();

    JobFilter jobFilter = (JobFilter) listList.get(0).get(0);

    createJobGroupDto.setName(
        jobFilter.getField() + "_" + jobFilter.getOperator() + "_" + jobFilter.getData());
    createJobGroupDto.setJobFilter(new GroupingJobFilter(GroupOperator.OR, listList.get(0)));

    jobFilter = (JobFilter) listList.get(1).get(0);

    editJobGroupDto.setName(
        jobFilter.getField() + "_" + jobFilter.getOperator() + "_" + jobFilter.getData());

    editJobGroupDto.setJobFilter(new GroupingJobFilter(GroupOperator.AND, listList.get(1)));

    List<Dtos> dtosList = new ArrayList<>();

    dtosList.add(
        new Dtos(createClientDto, createCampaignDto, createJobGroupDto, BidLevel.JOB_GROUP));
    dtosList.add(new Dtos(createClientDto, editCampaignDto, editJobGroupDto, BidLevel.PLACEMENT));

    return dtosList;
  }

  private static List<List<Filter>> getJobFilters(
      int size, RuleOperator ruleOperator, JobFilterFields jobFilterFields) {

    List<List<Filter>> listList = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      List<Filter> list = new ArrayList<>();
      list.add(
          new JobFilter<>(
              ruleOperator,
              jobFilterFields,
              Utils.getRandomString(5)
                  + ruleOperator
                  + jobFilterFields
                  + Utils.getRandomString(5)));
      listList.add(list);
    }
    return listList;
  }
}
