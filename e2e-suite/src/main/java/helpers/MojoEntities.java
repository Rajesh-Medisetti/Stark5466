package helpers;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.JobGroup;

public class MojoEntities {
  private Client client;
  private Campaign campaign;
  private JobGroup jobGroup1;

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

  public JobGroup getJobGroup1() {
    return jobGroup1;
  }

  public void setJobGroup1(JobGroup jobGroup1) {
    this.jobGroup1 = jobGroup1;
  }
}
