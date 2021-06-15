package tests;

import base.OurWebDriver;
import base.TestRunnerBaseUI;
import helpers.Constants;
import model.Form;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.Homepage;

public class TestClientAdd extends TestRunnerBaseUI {

  @Test
  public void checkClientPage() throws InterruptedException {
    OurWebDriver driver = createDriver(enums.ui.BrowserType.CHROME);
    new Homepage(driver).landAtClientHomepage();

    driver.findElement(By.xpath(Constants.clientAddButton)).click();
    SoftAssert softAssert = new SoftAssert();
    Thread.sleep(1000);

    Form form =
        new Form(
            driver,
            Constants.clientFormMandatoryFields,
            Constants.clientFormOptionalFields,
            Constants.mandatoryFieldsErrorMessage,
            Constants.clientFormDataTypes,
            Constants.clientDefaultFields,
            Constants.clientSubmitButton,
            Constants.clientResetButton);

    form.areMandatoryFieldsHighlighted(softAssert, true);

    form.areMandatoryFieldsHighlighted(softAssert, false);

    driver.uiDriver.navigate().refresh();

    form.checkResetButton(softAssert);
    form.checkSubmitButton(softAssert);

    form.isButtonEnabled(softAssert, Constants.clientSubmitButton, true);
    form.isButtonEnabled(softAssert, Constants.clientResetButton, true);
    // form.isButtonEnabled(softAssert, Constants.mapFeedButton, true);

    softAssert.assertAll();
  }
}
