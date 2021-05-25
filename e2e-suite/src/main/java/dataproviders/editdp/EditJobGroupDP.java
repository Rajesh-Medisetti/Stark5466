package dataproviders.editdp;

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
import entitycreators.DtosCreatorForEdit;
import entitycreators.JobCreator;
import enums.BidType;
import helpers.MojoUtils;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class EditJobGroupDP {

  public static Set<Client> clientSet = new HashSet<>();
  public static List<List<Object>> cpcBidDPList = new ArrayList<>();
  public static List<List<Object>> bidDPList = new ArrayList<>();
  public static boolean ifSchedulerRan = true;
  static String publisher = "Naukri";

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "editBids")
  public static Object[][] editBidsDP() {

    Object[][] array = new Object[bidDPList.size()][bidDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : bidDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "editCpcBids")
  public static Object[][] cpcBidsAfterEditDP() {

    Object[][] array = new Object[cpcBidDPList.size()][cpcBidDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : cpcBidDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /** . check bids after edit */
  public static void checkBidsAfterEdit(Driver driver) throws MojoException {

    Double newCpcBid = (double) Utils.getRandomNumber(1, 3);
    Double newCpaBid = (double) Utils.getRandomNumber(1, 5);

    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();
    JobCreator jobCreator = JobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    Client client = driver.createClient(clientDto);
    clientSet.add(client);

    CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
    campaignDto.setClientId(client.id);

    Campaign campaign = driver.createCampaign(campaignDto);

    JobGroupDto jobGroupDto = dtosList.get(0).getJobGroupDto();
    jobGroupDto.setClientId(client.id);
    jobGroupDto.setCampaignId(campaign.id);
    jobGroupDto.setCpcBid(2.0);
    jobGroupDto.setCpaBid(4.0);

    jobGroupDto.setBudgetCap(true, Freq.Monthly, 80.0, 500.0);
    jobGroupDto.addPlacement(publisher);

    JobGroup jobGroup = driver.createJobGroup(jobGroupDto);

    for (BidType bidType : BidType.values()) {

      JobGroupDto editJobGroupDto = new JobGroupDto();

      if (bidType.equals(BidType.CPC)) {
        editJobGroupDto.setCpcBid(newCpcBid);
        jobGroup.edit(editJobGroupDto);
        bidDPList.add(List.of(jobGroup, newCpcBid, bidType));
      }

      if (bidType.equals(BidType.CPA)) {
        editJobGroupDto.setCpaBid(newCpaBid);
        jobGroup.edit(editJobGroupDto);
        bidDPList.add(List.of(jobGroup, newCpaBid, bidType));
      }
    }
  }

  /** . checks CpcBid after edit in OutBoundFeed */
  public static void checkCpcBidInOutBoundFeedAfterEdit(Driver driver) throws MojoException {

    final Double newCpcBid = (double) Utils.getRandomNumber(1, 3);

    List<Dtos> dtosList = new DtosCreatorForEdit().getDtos();

    JobCreator jobCreator = JobCreator.jobProvider(dtosList);

    ClientDto clientDto = dtosList.get(0).getClientDto();

    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    Client client = driver.createClient(clientDto);
    clientSet.add(client);

    CampaignDto campaignDto = dtosList.get(0).getCampaignDto();
    campaignDto.setClientId(client.id);

    Campaign campaign = driver.createCampaign(campaignDto);

    JobGroupDto jobGroupDto = dtosList.get(0).getJobGroupDto();
    jobGroupDto.setClientId(client.id);
    jobGroupDto.setCampaignId(campaign.id);
    jobGroupDto.setCpcBid(2.0);
    jobGroupDto.setCpaBid(4.0);

    jobGroupDto.setBudgetCap(true, Freq.Monthly, 80.0, 500.0);
    jobGroupDto.addPlacement(publisher);

    JobGroup jobGroup = driver.createJobGroup(jobGroupDto);

    JobGroupDto editJobGroupDto = new JobGroupDto();
    editJobGroupDto.setCpcBid(newCpcBid);
    jobGroup.edit(editJobGroupDto);

    cpcBidDPList.add(List.of(jobGroup, newCpcBid, client, publisher, jobGroupDto, jobCreator));
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
