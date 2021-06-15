package model;

import base.OurWebDriver;
import enums.DataType;
import helpers.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class Form {
  private OurWebDriver driver;
  private Map<String, String> mandatoryFields;
  private Map<String, String> optionalFields;
  private Map<String, String> mandatoryFieldsErrorMessage;
  private Map<String, DataType> formDataTypes;
  private Set<String> defaultFields;
  private String submitXpath;
  private String resetXPath;

  /** Constructor for Form utility. */
  public Form(
      OurWebDriver driver,
      Map<String, String> mandatoryFields,
      Map<String, String> optionalFields,
      Map<String, String> mandatoryFieldsErrorMessage,
      Map<String, DataType> formDataTypes,
      Set<String> defaultFields,
      String submitXpath,
      String resetXPath) {
    this.driver = driver;
    this.mandatoryFields = mandatoryFields;
    this.optionalFields = optionalFields;
    this.mandatoryFieldsErrorMessage = mandatoryFieldsErrorMessage;
    this.formDataTypes = formDataTypes;
    this.defaultFields = defaultFields;
    this.submitXpath = submitXpath;
    this.resetXPath = resetXPath;
  }

  /** To check field mandatory pop ups. */
  public void areMandatoryFieldsHighlighted(SoftAssert softAssert, boolean isMandatory)
      throws InterruptedException {
    Map<String, String> fields = isMandatory ? mandatoryFields : optionalFields;
    for (String key : fields.keySet()) {
      String path = fields.get(key);
      WebElement webElement = driver.findElement(By.xpath(path));
      webElement.click();

      driver.findElement(By.xpath("/html")).click();
      // check colour based on data type
      String newColor = webElement.getCssValue("caret-color");
      boolean actual = newColor.equals(Constants.mandatoryFieldColorCode);
      System.out.println("Key : " + key + " color : " + newColor);
      String errorMessage =
          (isMandatory
                  ? "\nMandatory message recommendation not shown for : "
                  : "\nMandatory message recommendation shown for : ")
              + key;
      softAssert.assertEquals(actual, isMandatory, errorMessage);
      // check mandatory message
      if (isMandatory && !defaultFields.contains(key)) {
        boolean actual1 =
            driver.findElement(By.xpath(mandatoryFieldsErrorMessage.get(key))).isDisplayed();
        softAssert.assertEquals(actual1, isMandatory, errorMessage);
      }
    }
  }

  private void checkAllMandatoryPopUpsColor(SoftAssert softAssert, boolean isMandatory) {
    Map<String, String> fields = isMandatory ? mandatoryFields : optionalFields;
    for (String key : fields.keySet()) {
      String path = fields.get(key);
      WebElement webElement = driver.findElement(By.xpath(path));
      // check colour
      String newColor = webElement.getCssValue("caret-color");
      boolean actual = newColor.equals(Constants.mandatoryFieldColorCode);
      String errorMessage =
          (isMandatory
                  ? "\nMandatory message recommendation not shown for : "
                  : "\nMandatory message recommendation shown for : ")
              + key;
      softAssert.assertEquals(actual, isMandatory, errorMessage);
    }
  }

  /** To check functionality of submit button . */
  public void checkSubmitButton(SoftAssert softAssert) {
    driver.findElement(By.xpath(submitXpath)).click();
    checkAllMandatoryPopUpsColor(softAssert, true);
    checkAllMandatoryPopUpsColor(softAssert, false);
  }

  private void addContentToFormFields(boolean isMandatory) {
    Map<String, String> fields = isMandatory ? mandatoryFields : optionalFields;
    for (String key : fields.keySet()) {
      String path = fields.get(key);
      WebElement webElement = driver.findElement(By.xpath(path));
      if (formDataTypes.get(key).equals(DataType.STRING)) {
        webElement.sendKeys("xyz");
      } else if (formDataTypes.get(key).equals(DataType.NUMBER)) {
        webElement.sendKeys("1000");
      } else if (formDataTypes.get(key).equals(DataType.DROPDOWN)) {
        // select any item randomly from dropdown
        DropDown dropDown = new DropDown(path);
        List<String> elementsInDropDown = dropDown.getData(driver);
        Random random = new Random();
        int index = random.nextInt(elementsInDropDown.size());
        dropDown.selectValue(elementsInDropDown.get(index), driver);
      } else if (formDataTypes.get(key).equals(DataType.CALENDAR)) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String mojoSpecificDate = localDate.format(formatter);
        webElement.sendKeys(Keys.COMMAND + "A");
        webElement.sendKeys(Keys.BACK_SPACE, mojoSpecificDate);
      } else if (formDataTypes.get(key).equals(DataType.CHECKBOX)) {
        webElement.click();
      }
    }
  }

  private void isFieldContentEmpty(SoftAssert softAssert, boolean isMandatory) {
    Map<String, String> fields = isMandatory ? mandatoryFields : optionalFields;
    for (String key : fields.keySet()) {
      String path = fields.get(key);
      WebElement webElement = driver.findElement(By.xpath(path));
      String valueStores;
      boolean actual;
      if (formDataTypes.get(key).equals(DataType.DROPDOWN)) {
        valueStores = webElement.getText();
        actual = valueStores.contains(key);
      } else if (formDataTypes.get(key).equals(DataType.CHECKBOX)) {
        actual = webElement.isEnabled();
      } else {
        valueStores = webElement.getAttribute("value");
        actual = valueStores.isEmpty();
      }
      softAssert.assertEquals(actual, true, key + " is not reset to empty.");
    }
  }

  /** To check functionality of reset button. */
  public void checkResetButton(SoftAssert softAssert) throws InterruptedException {
    // Add content to each field
    addContentToFormFields(true);
    addContentToFormFields(false);
    Thread.sleep(3000);
    driver.findElement(By.xpath(resetXPath)).click();

    // content should be empty
    isFieldContentEmpty(softAssert, true);
    isFieldContentEmpty(softAssert, false);
  }

  /** To check whether button is active. */
  public void isButtonEnabled(SoftAssert softAssert, String buttonXpath, boolean isEnabled) {
    String errorMessage = isEnabled ? "\nButton is not enabled" : "\nButton is enabled";
    softAssert.assertEquals(
        driver.findElement(By.xpath(buttonXpath)).isEnabled(), isEnabled, errorMessage);
  }
}
