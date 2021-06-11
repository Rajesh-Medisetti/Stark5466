package pages;

import base.OurWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login {
  private String usernameIdentifier = "mat-input-0";
  private String passwordIdentifier = "mat-input-1";
  private String rememberMeIdentifier = "mat-checkbox-1-input";
  private String loginIdentifier =
      "/html/body/jv-root/jv-login/form/mat-card/mat-card-actions/button/span[1]";
  private String title = "Mojo";

  private String username;
  private String password;
  private boolean rememberMe;

  /** constructor. */
  public Login(String username, String password, boolean rememberMe) {

    this.username = username;
    this.password = password;
    this.rememberMe = rememberMe;
  }

  /** login into the mojo app based on the username and password mentioned in the constructor. */
  public void loginMojo(OurWebDriver ourDriver, String url) {
    ourDriver.uiDriver.get(url);
    ourDriver.findElement(By.id(usernameIdentifier)).sendKeys(this.username);
    ourDriver.findElement(By.id(passwordIdentifier)).sendKeys(this.password);
    if (this.rememberMe) {
      ourDriver.findElement(By.id(rememberMeIdentifier)).click();
    }
    ourDriver.findElement(By.xpath(loginIdentifier)).click();
  }

  /** checks if it is logged in or not. */
  public boolean ifLoggedIn(WebDriver driver) {
    return driver.getTitle().equals(title);
  }
}
