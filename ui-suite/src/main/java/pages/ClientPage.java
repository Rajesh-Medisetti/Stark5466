package pages;

import base.OurWebDriver;
import base.TestRunnerBaseUI;
import enums.ui.ClientTabs;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class ClientPage {

  private String publisherTabXpath =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content"
          + "/jv-content-viewer/div/jv-content-nav/nav"
          + "/div[2]/div/div/a[2]";
  private OurWebDriver driver;
  private String urlIdentifier = "https://mojo2.staging.joveo.com";
  String clientSearchIconXpath =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer"
          + "/jv-entity/div/div[2]/div/div/jv-table-view/m-table/mat-card"
          + "/div[1]/div/div/div[1]/div/mat-icon";
  String clientSearchXpath =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer"
          + "/jv-entity/div/div[2]/div/div/jv-table-view/m-table/mat-card"
          + "/div[1]/div/div/div[1]/div/input";
  String firstRowResult =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer"
          + "/jv-entity/div/div[2]/div/div/jv-table-view/m-table/mat-card/"
          + "div[2]/table/tbody/tr/td[3]/div/div/div/a";
  String tabsXpath =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content"
          + "/jv-content-viewer"
          + "/div/jv-content-nav/nav/div[2]/div/div/a[";

  public ClientPage(OurWebDriver driver) {
    this.driver = driver;
  }

  /** lands at the client page, after searching and then clicking the mentioned clientName. */
  public void landAtClientPage(String clientName) {
    new Homepage(this.driver).landAtClientHomepage();
    driver.findElement(By.xpath(clientSearchIconXpath)).click();
    driver.findElement(By.xpath(clientSearchXpath)).sendKeys(clientName);
    driver.findElement(By.xpath(clientSearchXpath)).sendKeys(Keys.ENTER);
    driver.findElement(By.xpath(firstRowResult)).click();
  }

  /**
   * lands at client page based on the clientname mentioned and then clicking on the specific tab.
   */
  public void goToTabUnderClient(String clientName, ClientTabs tab) throws InterruptedException {
    landAtClientPage(clientName);
    Thread.sleep(5000);
    tabsXpath = tabsXpath + TestRunnerBaseUI.clientMap.get(tab.toString()) + "]";
    System.out.println(tabsXpath);
    driver.findElement(By.xpath(tabsXpath)).click();
  }

  /** returns the title of the page. */
  public String getTitle() {
    return this.driver.getTitle();
  }
}
