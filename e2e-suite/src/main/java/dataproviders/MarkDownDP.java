package dataproviders;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.*;
import dtos.Dtos;
import dtos.JobFilterData;
import entitycreators.*;
import enums.BidLevel;
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
  public static JobFilterData markDowndata;

  /**
   * the actual data provider for the test.
   *
   * @return 2d array
   */
  @DataProvider(name = "MarkDown")
  public static Object[][] markDownDP() {
    List<List<Object>>  markDownDPList = markDowndata.getDpList();
    Object[][] array = new Object[markDownDPList.size()][markDownDPList.get(0).size()];
    int counter = 0;
    for (List<Object> list : markDownDPList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }


  public List<Dtos> getMarkDownList()
  {
    List<Dtos> dtosList =  new ArrayList<>();
    for(BidLevel level:BidLevel.values()) {
      dtosList.addAll(getMarkDownList(level));
    }
return dtosList;
  }

public List<Dtos> getMarkDownList(BidLevel level)
{
  List<Dtos> dtosList =  new ArrayList<>();
  ClientDto clientDto = ClientEntityCreator.randomClientCreator("", true, markDown);
  JobGroupDto jobGroup =
          JobGroupCreator.markDownDtoWithEqual(JobFilterFields.country, "India",300, 1);
    dtosList.add(new Dtos(clientDto, null, jobGroup, level));
  return dtosList;
}



}
