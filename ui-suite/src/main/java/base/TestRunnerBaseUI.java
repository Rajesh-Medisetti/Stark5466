package base;

import enums.ui.BrowserType;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestRunnerBaseUI {

  public static Map<String, Integer> clientMap = getClientTab();

  /** creating a driver for web. */
  public static OurWebDriver createDriver(BrowserType browser) {
    WebDriver uiDriver = null;
    if (browser.equals(BrowserType.CHROME)) {
      uiDriver = new ChromeDriver();
      uiDriver.manage().window().maximize();
    }
    return new OurWebDriver(uiDriver);
  }

  /** creates the client tab maps. */
  public static Map<String, Integer> getClientTab() {
    Map<String, Integer> tabMap = new HashMap<>();
    tabMap.put("CAMPAIGN", 1);
    tabMap.put("JOBGROUPS", 2);
    tabMap.put("JOBS", 3);
    tabMap.put("PLACEMENTS", 4);
    tabMap.put("AUTOMATION", 5);
    tabMap.put("POSTINGS", 6);
    tabMap.put("SLOTS", 7);
    tabMap.put("GOOGLEADS", 8);
    tabMap.put("FACEBOOK", 9);
    return tabMap;
  }
}
