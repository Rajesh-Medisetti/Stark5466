package dtos;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import entitycreators.JobCreator;
import enums.BidLevel;

public class AllEntities extends Dtos {

  private Client client;
  private Campaign campaign;
  private JobGroup jobGroup;
  private JobCreator jobCreator;

  /**
   * Storage for Entities.
   *
   * @param clientDto clientDto
   * @param campaignDto campaignDto
   * @param jobGroupDto jobGroupDto
   * @param bidLevel bidLevel
   * @param numberOfJobs numberOfJobs
   */
  public AllEntities(
      Client client,
      ClientDto clientDto,
      Campaign campaign,
      CampaignDto campaignDto,
      JobGroup jobGroup,
      JobGroupDto jobGroupDto,
      BidLevel bidLevel,
      int numberOfJobs,
      JobCreator jobCreator) {
    super(clientDto, campaignDto, jobGroupDto, bidLevel, numberOfJobs);
    this.client = client;
    this.campaign = campaign;
    this.jobGroup = jobGroup;
    this.jobCreator = jobCreator;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Campaign getCampaign() {
    return campaign;
  }

  public void setCampaign(Campaign campaign) {
    this.campaign = campaign;
  }

  public JobGroup getJobGroup() {
    return jobGroup;
  }

  public void setJobGroup(JobGroup jobGroup) {
    this.jobGroup = jobGroup;
  }

  public JobCreator getJobCreator() {
    return jobCreator;
  }

  public void setJobCreator(JobCreator jobCreator) {
    this.jobCreator = jobCreator;
  }
}
