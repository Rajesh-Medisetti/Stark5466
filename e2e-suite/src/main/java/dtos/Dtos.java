package dtos;

import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import enums.BidLevel;

public class Dtos {
  private ClientDto clientDto;
  private CampaignDto campaignDto;
  private JobGroupDto jobGroupDto;

  public int getNumberOfjobs() {
    return numberOfJobs;
  }

  public void setNumberOfjobs(int numberOfJobs) {
    this.numberOfJobs = numberOfJobs;
  }

  private int numberOfJobs;
  private BidLevel bidLevel;

  /** . Storage for Entities */
  public Dtos(
      ClientDto clientDto,
      CampaignDto campaignDto,
      JobGroupDto jobGroupDto,
      BidLevel bidLevel,
      int numberOfJobs) {
    this.clientDto = clientDto;
    this.campaignDto = campaignDto;
    this.jobGroupDto = jobGroupDto;
    this.bidLevel = bidLevel;
    this.numberOfJobs = numberOfJobs;
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

  public BidLevel getBidLevel() {
    return bidLevel;
  }

  public void setBidLevel(BidLevel bidLevel) {
    this.bidLevel = bidLevel;
  }

  public void setJobGroupDto(JobGroupDto jobGroupDto) {
    this.jobGroupDto = jobGroupDto;
  }
}
