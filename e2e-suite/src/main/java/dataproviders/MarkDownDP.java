package dataproviders;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.Freq;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dtos.Dtos;
import entitycreators.DtosCreator;
import entitycreators.JobCreator;
import helpers.MojoUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class MarkDownDP {

  public static String publisher = "Naukri";
  public static Set<Client> clientSet = new HashSet<Client>();
  public static boolean ifSchedulerRan = true;
  public static Double markDown = 50.0;
  public static List<List<Object>> markDownDPList = new ArrayList<>();

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "MarkDown")
  public static Object[][] markDownDP() {

    Object[][] array = new Object[markDownDPList.size()][markDownDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : markDownDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /** . Data Provider for MarkDown TestCases */
  public static void dataProvider(Driver driver) throws MojoException {

    List<Dtos> dtosList = new DtosCreator().getDtos();
    JobCreator jobCreator = JobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    clientDto.setMarkDown(markDown, false);
    Client client = driver.createClient(clientDto);
    clientSet.add(client);

    CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
    campaignDto.setClientId(client.id);

    Campaign campaign = driver.createCampaign(campaignDto);

    JobGroupDto jobGroupDto1 = dtosList.get(0).getJobGroupDto();
    jobGroupDto1.setClientId(client.id);
    jobGroupDto1.setCampaignId(campaign.id);
    jobGroupDto1.setCpcBid(4.0);

    jobGroupDto1.setBudgetCap(true, Freq.Monthly, 80.0, 500.0);
    jobGroupDto1.addPlacement(publisher);

    JobGroup jobGroup1 = driver.createJobGroup(jobGroupDto1);

    markDownDPList.add(
        List.of(
            markDown, client, jobGroupDto1, jobGroup1, jobCreator, dtosList.get(0).getBidLevel()));

    JobGroupDto jobGroupDto2 = dtosList.get(1).getJobGroupDto();
    jobGroupDto2.setClientId(client.id);
    jobGroupDto2.setCampaignId(campaign.id);
    jobGroupDto2.setCpcBid(4.0);

    jobGroupDto2.setBudgetCap(true, Freq.Monthly, 80.0, 500.0);
    jobGroupDto2.addPlacementWithBid(publisher, 3.0);
    JobGroup jobGroup2 = driver.createJobGroup(jobGroupDto2);

    markDownDPList.add(
        List.of(
            markDown, client, jobGroupDto2, jobGroup2, jobCreator, dtosList.get(1).getBidLevel()));

    try {
      MojoUtils.runSchedulerAndRefreshCache(clientSet, driver);
    } catch (Exception e) {
      ifSchedulerRan = false;
    }
  }
}
