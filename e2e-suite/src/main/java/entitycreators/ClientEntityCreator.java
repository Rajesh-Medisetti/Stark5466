package entitycreators;

import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.Frequency;
import com.joveo.eqrtestsdk.models.TimeZone;
import helpers.Utils;

public class ClientEntityCreator {
  // will use some builder like design pattern here. so that this can be created
  // on the run.

  // . Just
  // feed need to be passed on.

  /**
   * This will create the clientDTO with all random fields in specific range.
   *
   * @return the randomly created clientDto.
   */
  public static ClientDto randomClientCreator(boolean isMarkDown, double markDown) {

    ClientDto clientDto = new ClientDto();
    clientDto.setName("TEST_CLIENT1_client_name_automation" + Utils.generateRandomString());
    clientDto.setBudget((double) Utils.getRandomNumber(1000));
    clientDto.setBudget(1000.00); // this has to be random need somesdk modifications
    clientDto.setFrequency(Frequency._3_Hours);
    clientDto.setApplyConvWindow(4);
    clientDto.setTimezone(TimeZone.UTC_plus_05_30);
    clientDto.setAts("App Vault");
    clientDto.setIndustry("47");
    if (isMarkDown) {
      clientDto.setMarkDown(markDown, false);
    }
    return clientDto;
  }
}
