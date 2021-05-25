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

public class DtosCreatorForEdit {

  /** . return a pair of Dtos */
  public List<Dtos> getDtos() {

    int sz = 4;
    List<List<Filter>> listList = getJobFilters(sz, RuleOperator.EQUAL, JobFilterFields.country);

    List<ClientDto> clientDtoList = new ArrayList<>();
    final ClientDto createClientDto = ClientEntityCreator.randomClientCreator(false, 0);

    clientDtoList.add(createClientDto);
    for (int i = 0;i < sz - 1; i++) {
      clientDtoList.add(new ClientDto());
    }

    List<CampaignDto> campaignDtoList = new ArrayList<>();

    for (int i = 0;i < sz ; i++) {
      campaignDtoList.add(new CampaignDto());
    }

    List<JobGroupDto> jobGroupDtoList = new ArrayList<>();

    for (int i = 0;i < sz; i++) {
      JobGroupDto jobGroupDto = new JobGroupDto();

      JobFilter jobFilter = (JobFilter) listList.get(i).get(0);

      jobGroupDto.setName(
          jobFilter.getField() + "_" + jobFilter.getOperator() + "_" + jobFilter.getData());
      jobGroupDto.setJobFilter(new GroupingJobFilter(GroupOperator.OR, listList.get(i)));

      jobGroupDtoList.add(jobGroupDto);
    }


    List<Dtos> dtosList = new ArrayList<>();

    List<BidLevel> bidLevelList = new ArrayList<>();
    bidLevelList.add(BidLevel.JOB_GROUP);
    bidLevelList.add(BidLevel.PLACEMENT);
    bidLevelList.add(BidLevel.JOB_GROUP);
    bidLevelList.add(BidLevel.PLACEMENT);

    for (int i = 0;i < sz; i++) {
      dtosList.add(new Dtos(clientDtoList.get(i), campaignDtoList.get(i),
          jobGroupDtoList.get(i), bidLevelList.get(i),0));
    }

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
