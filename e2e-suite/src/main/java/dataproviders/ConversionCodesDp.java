package dataproviders;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.models.fetcher.Pixels;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.ClientDto;
import entitycreators.ClientEntityCreator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.DataProvider;

public class ConversionCodesDp {

  public static List<List<Object>> pixelsInfo;
  public static Set<Client> clients = new HashSet<>();

  /**
   * This method is used to convert 2d list to 2d array.
   *
   * @return two dimensional array
   */
  @DataProvider(name = "test", parallel = false)
  public static Object[][] dpMethod() {
    List<List<Object>> dpList = pixelsInfo;
    Object[][] array = new Object[dpList.size()][dpList.get(0).size()];
    int counter = 0;
    for (List<Object> list : dpList) {
      array[counter] = list.toArray();
      counter++;
    }
    return array;
  }

  /**
   * .This method returns pixels and s3 links of a client.
   *
   * @param driver Driver
   * @return 2d list
   * @throws MojoException Mojo Exception
   */
  public static List<List<Object>> getPixelsOfClient(Driver driver) throws MojoException {
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(false, 10);
    clientDto.addFeed("https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml");

    Client client = driver.createClient(clientDto);
    clients.add(client);

    Pixels pixels = client.getPixels();
    List<String> s3Links = getS3Links(pixels);

    List<List<Object>> dpList = new ArrayList<>();
    dpList.add(List.of(pixels, s3Links));
    return dpList;
  }

  private static List<String> getS3Links(Pixels pixels) {
    List<String> s3Links = new ArrayList<>();
    for (Pixels.Records record : pixels.getRecords()) {
      if (record.getTitle().contains("JS Pixel")) {
        s3Links.add(
            record.getCode().split("src='//s3.amazonaws.com/")[1].split("'></script>")[0].trim());
      }
    }
    return s3Links;
  }
}
