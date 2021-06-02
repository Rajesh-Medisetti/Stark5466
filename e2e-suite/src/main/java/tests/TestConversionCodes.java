package tests;

import base.TestRunnerBase;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.models.fetcher.Pixels;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import dataproviders.ConversionCodesDp;
import helpers.MojoUtils;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import validations.ConversionCodeValidations;

public class TestConversionCodes extends TestRunnerBase {

  /**
   * Creates data for validating pixels for a client.
   *
   * @throws MojoException Mojo Exception
   */
  @BeforeClass
  public void setUp() throws MojoException {
    if (driver == null) {
      driver = Driver.start("reliability@joveo.com", "joveo1520", JoveoEnvironment.Staging);
    }
    ConversionCodesDp.pixelsInfo = ConversionCodesDp.getPixelsOfClient(driver);
  }

  @Test(dataProvider = "test", dataProviderClass = ConversionCodesDp.class)
  public void testConversionCodes(Pixels pixels, List<String> s3Links) throws MojoException {
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertTrue(
        ConversionCodeValidations.checkPresenceOfAllPixels(pixels),
        "Few pixels are not generated for client");

    softAssert.assertTrue(
        ConversionCodeValidations.checkPresenceOfPixelsInS3(driver, s3Links),
        "JS Pixels are not present in S3");

    softAssert.assertAll();
  }

  @AfterClass
  public void tearDown() throws MojoException {
    MojoUtils.removeClientSet(ConversionCodesDp.clients);
  }
}
