package base;

import helpers.WaitForAngular;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OurWebDriver {
  public WebDriver uiDriver;

  public OurWebDriver(WebDriver uiDriver) {
    this.uiDriver = uiDriver;
  }

  /** find Element for the waitied condition. */
  public WebElement findElement(By by) {
    new WebDriverWait(this.uiDriver, 75).until(ExpectedConditions.elementToBeClickable(by));
    return this.uiDriver.findElement(by);
  }

  /** find Element for the waitied condition. */
  public void click(By by) {
    new WebDriverWait(this.uiDriver, 60).until(ExpectedConditions.elementToBeClickable(by));
    this.uiDriver.findElement(by).click();
  }

  /** get title for the driver. */
  public String getTitle() {
    return uiDriver.getTitle();
  }

  /** waiting for an element. */
  public void wait(WebElement element) {

    new WebDriverWait(this.uiDriver, 60).until(ExpectedConditions.elementToBeClickable(element));
  }

  /** closing the driver. */
  public void close() {
    uiDriver.close();
  }

  /** Waiting for angular hits. */
  public void waitForAngular() {

    WaitForAngular waiterForAngular = new WaitForAngular();
    waiterForAngular.setDriver(this.uiDriver);
    waiterForAngular.waitUntilAngularReady();
  }
}
