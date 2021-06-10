package model;

import base.OurWebDriver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Table {

  public static List<Map<String, String>> tableData = new ArrayList<>();

  /** returns all the table data. */
  public static List<Map<String, String>> getTableData(String xpath, OurWebDriver driver) {

    List<WebElement> attributes =
        driver
            .findElement(By.xpath(xpath))
            .findElement(By.tagName("table"))
            .findElements(By.tagName("thead"))
            .get(0)
            .findElements(By.tagName("tr"))
            .get(0)
            .findElements(By.tagName("th"));

    List<WebElement> rows =
        driver
            .findElement(By.xpath(xpath))
            .findElement(By.tagName("table"))
            .findElement(By.tagName("tbody"))
            .findElements(By.tagName("tr"));

    System.out.println(rows.size());

    int index = 0;
    for (WebElement row : rows) {
      List<WebElement> tds = row.findElements(By.tagName("td"));
      index = 0;
      Map<String, String> map = new HashMap<>();
      for (WebElement data : tds) {
        map.put(attributes.get(index++).getText(), data.getText());
      }
      tableData.add(map);
    }
    System.out.println(tableData.size());
    return tableData;
  }
}
