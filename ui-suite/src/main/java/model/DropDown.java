package model;

import base.OurWebDriver;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DropDown {
  public String xpath;
  public List<String> data;

  /** Constructor for DropDown class. */
  public DropDown(String xpath) {
    this.data = new ArrayList<>();
    this.xpath = xpath;
  }

  /** . return all values of the dropDown */
  public List<String> getData(OurWebDriver driver) {
    driver.waitForAngular();
    driver.findElement(By.xpath(xpath)).click();
    driver.waitForAngular();
    List<WebElement> list = driver.findElements(By.tagName("mat-option"));
    for (WebElement e : list) {
      data.add(e.getText());
    }
    driver.findElement(By.xpath("/html")).click();
    return data;
  }

  /** To select given value from dropbox. */
  public boolean selectValue(String text, OurWebDriver driver) {
    driver.waitForAngular();
    driver.findElement(By.xpath(xpath)).click();
    driver.waitForAngular();
    List<WebElement> list = driver.findElements(By.tagName("mat-option"));
    for (WebElement e : list) {
      String value = e.getText();
      if (value.equals(text)) {
        e.click();
        return true;
      }
    }
    return false;
  }
}
