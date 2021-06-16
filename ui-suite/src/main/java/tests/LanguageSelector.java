package tests;

import static base.TestRunnerBaseUI.createDriver;

import base.OurWebDriver;
import dataproviders.LanguageDp;
import enums.Language;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ClientPage;
import pages.Homepage;

public class LanguageSelector {

  @Test(dataProvider = "clientDataProvider", dataProviderClass = LanguageDp.class)
  public void testLangForAllClients(String expectedValue, Language language)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(enums.ui.BrowserType.CHROME);
    new Homepage(driver).landAtClientHomepage();
    new model.LanguageSelector().selectLanguage(driver, language);
    String actualValue = new Homepage(driver).getClientTabText();
    softAssert.assertEquals(actualValue, expectedValue, "All clients for " + language.toString() + " is not correctly presented.");
    softAssert.assertAll();
    driver.close();
  }

  @Test(dataProvider = "clientsLevelDataProvider", dataProviderClass = LanguageDp.class)
  public static void testLangOnClient(String clientName, enums.ui.ClientTabs clientTab, String expectedValue, Language language)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(enums.ui.BrowserType.CHROME);
    String actualValue = new ClientPage(driver).getTabText(clientName, clientTab, language);
    softAssert.assertEquals(actualValue, expectedValue, clientTab.toString() +  " for " + language.toString() + " is not correctly presented." + "In UI : " + actualValue + " Expecting : " + expectedValue);
    softAssert.assertAll();
    driver.close();
  }

  @Test(dataProvider = "publisherDataProvider", dataProviderClass = LanguageDp.class)
  public void testLangForAllPublishers(String expectedValue, Language language)
      throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    OurWebDriver driver = createDriver(enums.ui.BrowserType.CHROME);
    new Homepage(driver).landAtPublisherHomepage();
    new model.LanguageSelector().selectLanguage(driver, language);
    String actualValue = new Homepage(driver).getPublisherTabText();
    softAssert.assertEquals(actualValue, expectedValue, "All Publishers for " + language.toString() + " is not correctly presented.");
    softAssert.assertAll();
    driver.close();
  }
}
