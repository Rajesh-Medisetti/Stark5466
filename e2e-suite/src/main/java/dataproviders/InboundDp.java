package dataproviders;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.MojoException;
import dtos.AllEntities;
import enums.BidLevel;
import helpers.MojoUtils;
import helpers.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class InboundDp {

  public static List<List<Object>> titleAndRefList;

  /**
   * This method is used to convert 2d list to 2d array.
   *
   * @return two dimensional array
   */
  @DataProvider(name = "test", parallel = false)
  public static Object[][] dpMethod() {
    List<List<Object>> dpList = titleAndRefList;
    Object[][] array = new Object[dpList.size()][dpList.get(0).size()];
    int counter = 0;
    for (List<Object> list : dpList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * . This method returns 2d list of objects holding parameters needed for testing Reference number
   * and title.
   *
   * @param driver Driver
   * @return 2d list
   * @throws MojoException Mojo exception
   * @throws InterruptedException Interrupt exception
   */
  public static List<List<Object>> setDiffTitleAndRef(Driver driver)
      throws MojoException, InterruptedException {
    Set<Client> clients = new HashSet<>();
    List<Boolean> booleanList = new ArrayList<>();
    List<List<Object>> dpList = new ArrayList<>();
    booleanList.add(true);
    booleanList.add(false);

    for (Boolean isTitleSame : booleanList) {
      for (Boolean isReqSame : booleanList) {
        AllEntities allEntities =
            MojoUtils.createClientAlongWithCampaignAndJobGroup(
                driver, JobFilterDP.placements, BidLevel.PLACEMENT);
        Integer initialJobs =
            allEntities
                .getJobCreator()
                .clientFeedMap
                .get(allEntities.getClientDto())
                .getJob()
                .size();
        Integer jobsAdded =
            Utils.getRandomNumber(
                allEntities
                    .getJobCreator()
                    .clientFeedMap
                    .get(allEntities.getClientDto())
                    .getJob()
                    .size());
        allEntities.getClient().runScheduler();

        MojoUtils.addNewJobToInboundFeed(
            allEntities.getJobCreator(), isTitleSame, isReqSame, jobsAdded, allEntities);
        dpList.add(
            List.of(
                isTitleSame,
                isReqSame,
                allEntities,
                JobFilterDP.placements,
                initialJobs,
                jobsAdded));
        clients.add(allEntities.getClient());
      }
    }
    MojoUtils.runSchedulerAndRefreshCache(clients, driver);
    return dpList;
  }
}
