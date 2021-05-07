package dtos;

import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;

public class Dtos {
  private ClientDto clientDto;
  private CampaignDto campaignDto;
  private JobGroupDto jobGroupDto;

  /** . Storage for Entities */
  public Dtos(ClientDto clientDto, CampaignDto campaignDto, JobGroupDto jobGroupDto) {
    this.clientDto = clientDto;
    this.campaignDto = campaignDto;
    this.jobGroupDto = jobGroupDto;
  }

  public ClientDto getClientDto() {
    return clientDto;
  }

  public void setClientDto(ClientDto clientDto) {
    this.clientDto = clientDto;
  }

  public CampaignDto getCampaignDto() {
    return campaignDto;
  }

  public void setCampaignDto(CampaignDto campaignDto) {
    this.campaignDto = campaignDto;
  }

  public JobGroupDto getJobGroupDto() {
    return jobGroupDto;
  }

  public void setJobGroupDto(JobGroupDto jobGroupDto) {
    this.jobGroupDto = jobGroupDto;
  }
}
