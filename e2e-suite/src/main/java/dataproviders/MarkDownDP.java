package dataproviders;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dtos.Dtos;
import dtos.JobFilterData;
import entitycreators.ClientEntityCreator;
import entitycreators.JobGroupCreator;
import enums.BidLevel;
import helpers.Utils;
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
  public static JobFilterData markDowndata;

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "MarkDown")
  public static Object[][] markDownDP() {
    List<List<Object>> markDownDpList = markDowndata.getDpList();
    Object[][] array = new Object[markDownDpList.size()][markDownDpList.get(0).size()];
    int counter = 0;
    for (List<Object> list : markDownDpList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * the method that creates dto list and combines for all bid level.
   *
   * @return list of dtos.
   */
  public List<Dtos> getMarkDownList() {
    List<Dtos> dtosList = new ArrayList<>();
    for (BidLevel level : BidLevel.values()) {
      dtosList.addAll(getMarkDownList(level));
    }
    return dtosList;
  }

  /**
   * the method that creates dto list based on the bid level.
   *
   * @return list of dtos.
   */
  public List<Dtos> getMarkDownList(BidLevel level) {
    List<Dtos> dtosList = new ArrayList<>();
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(true, markDown);
    JobGroupDto jobGroup = JobGroupCreator.dtoWithEqual(JobFilterFields.country, "India", 300, 1);
    dtosList.add(new Dtos(clientDto, null, jobGroup, level, Utils.getRandomNumber(2, 5)));
    return dtosList;
  }
}
