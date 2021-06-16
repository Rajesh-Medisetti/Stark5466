package pages;

import base.OurWebDriver;
import org.openqa.selenium.By;

public class Homepage {
  private String publisherTabXpath =
      "/html/body/jv-root/jv-home/mat-sidenav-container"
          + "/mat-sidenav-content/jv-content-viewer/"
          + "div/jv-content-nav/nav/div[2]/div/div/a[2]";

  private String urlIdentifier = "https://mojo2.staging.joveo.com";
  private String  clientTabXpath = "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/div/jv-content-nav/nav/div[2]/div/div/a[1]";

  public Homepage(OurWebDriver driver) {
    this.driver = driver;
  }

  private OurWebDriver driver;

  /** lands at the dashboard after logging into the mojo app based on basic credentials. */
  public void landAtClientHomepage() {
    Login login = new Login("reliability@joveo.com", "joveo1520", false);
    login.loginMojo(this.driver, urlIdentifier);
  }

  /** Lands at the publisher basic dashboard after logging into the mojo. */
  public void landAtPublisherHomepage() {
    this.landAtClientHomepage();
    this.driver.findElement(By.xpath(publisherTabXpath)).click();
  }

  /** returns the title of the page. */
  public String getTitle() {
    return this.driver.getTitle();
  }

  public String getPublisherTabText(){
    return this.driver.findElement(By.xpath(publisherTabXpath)).getText();
  }

  public String getClientTabText(){
    return this.driver.findElement(By.xpath(clientTabXpath)).getText();
  }
}
