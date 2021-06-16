package model;

import base.OurWebDriver;
import enums.Language;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LanguageSelector {
  private String languageDropDownXpath = "/html/body/jv-root/jv-home/mat-toolbar/div/div[1]";

  public LanguageSelector() {
  }

  private WebElement getBtnByText(OurWebDriver ourWebDriver, Language text) {
    List<WebElement> buttons = ourWebDriver.findElements(By.tagName("button"));
    for (WebElement btn : buttons) {
      if (btn.getText().equals(text.toString())) {
        return btn;
      }
    }
    return null;
  }

  public boolean selectLanguage(OurWebDriver driver, Language language)
      throws InterruptedException {
    WebElement dropDown = driver.findElement(By.xpath(languageDropDownXpath));
    dropDown.click();
    driver.waitForAngular();
    WebElement btn = getBtnByText(driver, language);
    if (btn != null) {
      btn.click();
      driver.waitForAngular();
      Thread.sleep(2000);
      return true;
    }
    return false;
  }
}
