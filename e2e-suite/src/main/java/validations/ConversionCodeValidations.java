package validations;

import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.models.fetcher.Pixels;
import enums.ConversionCodeTitles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConversionCodeValidations {

  /**
   * This method checks creation of all pixels for client.
   *
   * @param pixels Pixels
   * @return true/false
   */
  public static boolean checkPresenceOfAllPixels(Pixels pixels) {

    Set<String> existingPixelTitles = new HashSet<>();

    for (ConversionCodeTitles titles : ConversionCodeTitles.values()) {
      if (titles.getValue().equals("resume JS Pixel")
          || titles.getValue().equals("resume Image Pixel")) {
        continue;
      }
      existingPixelTitles.add(titles.getValue());
    }

    for (Pixels.Records record : pixels.getRecords()) {
      existingPixelTitles.remove(record.getTitle());
    }
    return existingPixelTitles.isEmpty();
  }

  /**
   * .This method returns presence of JS pixels in S3.
   *
   * @param driver Driver
   * @param s3Links S3Links
   * @return true/false
   */
  public static boolean checkPresenceOfPixelsInS3(Driver driver, List<String> s3Links) {
    for (String link : s3Links) {
      String bucketName = link.substring(0, link.indexOf("/"));
      String filePath = link.substring(link.indexOf('/') + 1);
      if (!driver.doesFileExistsInS3Bucket(bucketName, filePath)) {
        return false;
      }
    }
    return true;
  }
}
