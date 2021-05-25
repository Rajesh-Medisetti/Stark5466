package dataproviders.editdp;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import dtos.Dtos;
import entitycreators.DtosCreatorForEdit;
import entitycreators.JobCreator;
import helpers.MojoUtils;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class EditCampaignDP {

  public static Set<Client> clientSet = new HashSet<>();
  public static List<List<Object>> editCampaignBudgetDPList = new ArrayList<>();

  public static boolean ifSchedulerRan = true;

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "editCampaignBudget")
  public static Object[][] editCampaignDP() {

    Object[][] array =
        new Object[editCampaignBudgetDPList.size()][editCampaignBudgetDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : editCampaignBudgetDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /** . checking Budget after edit */
  public static void editCampaignBudgetDataProvider(Driver driver) throws MojoException {

    final Double newBudget = (double) Utils.getRandomNumber(500, 1000);

    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();

    JobCreator jobCreator = JobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    Client client = driver.createClient(clientDto);
    clientSet.add(client);

    CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
    campaignDto.setClientId(client.id);

    Campaign campaign = driver.createCampaign(campaignDto);

    CampaignDto editCampaignDto = dtosList.get(1).getCampaignDto();

    editCampaignDto.setBudget(newBudget);

    campaign.edit(editCampaignDto);

    editCampaignBudgetDPList.add(List.of(campaign, newBudget));
  }

  /** . run Scheduler */
  public static void runScheduler(Driver driver) {

    try {
      MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
    } catch (Exception e) {
      ifSchedulerRan = false;
    }
  }
}
