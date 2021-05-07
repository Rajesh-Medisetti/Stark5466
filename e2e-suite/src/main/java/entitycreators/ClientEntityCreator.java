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
   * @param feed the feed that need to be set in dto.
   * @return the randomly created clientDto.
   */
  public static ClientDto randomClientCreator(String feed) {

    ClientDto clientDto = new ClientDto();
    clientDto.setName("JOVEO_client_name_automation" + Utils.generateRandomString());
    clientDto.setBudget((double) Utils.getRandomNumber(1000));
    clientDto.setBudget(1000.00); // this has to be random need somesdk modifications
    clientDto.setFrequency(Frequency._3_Hours);
    clientDto.setApplyConvWindow(4);
    clientDto.setTimezone(TimeZone.UTC_plus_05_30);
    clientDto.setAts("App Vault");
    clientDto.setIndustry("47");
    return clientDto;
  }
}
