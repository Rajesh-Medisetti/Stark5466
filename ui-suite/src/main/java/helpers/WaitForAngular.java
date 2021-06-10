package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitForAngular {
  private WebDriver driver;
  private WebDriverWait jsWait;
  private JavascriptExecutor jsExec;
  // Get the driver
  public void setDriver(WebDriver driver) {
    jsWait = new WebDriverWait(driver, 10);
    jsExec = (JavascriptExecutor) driver;
  }

  public void waitUntilAngularReady() {
    try {
      Object angular5Check =
          jsExec.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']");
      if (angular5Check != null) {
        Boolean angularPageLoaded =
            (Boolean)
                jsExec.executeScript(
                    "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1");
        if (!angularPageLoaded) {
          sleep(10);
          waitForAngular5Load();
          sleep(10);
        }
      }
    } catch (Exception e) {
    }
  }

  public void waitForAngular5Load() {
    String angularReadynessScript =
        "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";
    angularLoads(angularReadynessScript);
  }

  public void angularLoads(String angularReadynessScript) {
    try {
      ExpectedCondition<Boolean> angularLoad =
          driver ->
              Boolean.valueOf(
                  ((JavascriptExecutor) driver).executeScript(angularReadynessScript).toString());
      boolean angularReady =
          Boolean.valueOf(jsExec.executeScript(angularReadynessScript).toString());
      if (!angularReady) {
        jsWait.until(angularLoad);
      }
    } catch (Exception e) {
    }
  }

  private void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (Exception e) {
    }
  }
}
