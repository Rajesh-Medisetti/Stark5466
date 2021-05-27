package entitycreators;

import com.joveo.eqrtestsdk.models.CampaignDto;
import java.time.LocalDate;

public class CampaignEntityCreator {

  /**
   * Campaign DTO randomly is created.
   *
   * @param maxBudget maximum possible budget, preferably budget of client.
   * @return a campaign randomly by making sure given budget in campaign is less than the budget
   *     passed.
   */
  public static CampaignDto randomCampaignCreator(double maxBudget) {
    CampaignDto campaignDto = new CampaignDto();
    campaignDto.setName("campaign_name_test"); // this has to be random
    campaignDto.setBudget(maxBudget / 1.5);
    campaignDto.setStartDate(LocalDate.now());
    campaignDto.setEndDate(LocalDate.now().plusDays(2));
    return campaignDto;
  }

  /**
   * creating campaign dto.
   *
   * @param startDate start Date
   * @param endDate end date
   * @return campaign dto
   */
  public static CampaignDto dateCampaignCreator(LocalDate startDate, LocalDate endDate) {
    CampaignDto campaignDto = new CampaignDto();
    campaignDto.setName("campaign_name_test"); // this has to be random
    campaignDto.setStartDate(startDate);
    campaignDto.setEndDate(endDate);
    return campaignDto;
  }
}
