package helpers;

import enums.DataType;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("checkstyle:LineLength")
public class Constants {
  public static String clientAddButton =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-entity/div/div[3]/button";

  public static String clientResetButton =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/div/mat-card-actions/button[1]";

  public static String clientSubmitButton =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/div/mat-card-actions/button[2]";

  public static String mapFeedButton =
      "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/jv-feed-mapping/form/div/mat-accordion/div/mat-expansion-panel/mat-expansion-panel-header/span/mat-panel-title/div/div/button";

  public static Map<String, String> clientFormMandatoryFields =
      Map.ofEntries(
          Map.entry(
              "Client Name*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[1]/mat-form-field[1]/div/div[1]/div/input"),
          Map.entry(
              "Exported Name*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[1]/mat-form-field[2]/div/div[1]/div/input"),
          Map.entry(
              "Feed URL",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/jv-feed-mapping/form/div/mat-accordion/div/mat-expansion-panel/mat-expansion-panel-header/span/mat-panel-title/div/mat-form-field/div/div[1]/div/input"),
          Map.entry(
              "Monthly Budget*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[7]/mat-form-field/div/div[1]/div[2]/input"),
          Map.entry(
              "Client Start Date*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[8]/mat-form-field[1]/div/div[1]/div[1]/input"),
          Map.entry(
              "Client End Date*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[8]/mat-form-field[2]/div/div[1]/div[1]/input"),
          Map.entry(
              "Timezone*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[2]/jv-multiselect/mat-form-field/div/div[1]/div/mat-select"),
          Map.entry(
              "ATS Name*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[3]/jv-multiselect/mat-form-field/div/div[1]/div/mat-select"),
          Map.entry(
              "Frequency*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[4]/mat-form-field[1]/div/div[1]/div/mat-select"),
          Map.entry(
              "Apply Conversion Window*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[4]/mat-form-field[2]/div/div[1]/div/mat-select"),
          Map.entry(
              "Country/Region*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[9]/jv-multiselect/mat-form-field/div/div[1]/div/mat-select"));

  public static Map<String, String> mandatoryFieldsErrorMessage =
      Map.ofEntries(
          Map.entry(
              "Client Name*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[1]/mat-form-field[1]/div/div[3]/div/mat-error/p"),
          Map.entry(
              "Exported Name*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[1]/mat-form-field[2]/div/div[3]/div/mat-error/p"),
          Map.entry(
              "Feed URL",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/jv-feed-mapping/form/div/mat-accordion/div/mat-expansion-panel/mat-expansion-panel-header/span/mat-panel-title/div/mat-form-field/div/div[3]/div/mat-error/p"),
          Map.entry(
              "Monthly Budget*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[7]/mat-form-field/div/ div[3]/div/mat-error/p"),
          Map.entry(
              "Client End Date*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[8]/mat-form-field[2]/div/div[3]/div/mat-error/p"),
          Map.entry(
              "Timezone*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[2]/jv-multiselect/mat-form-field/div/div[3]/div/mat-error"),
          Map.entry(
              "ATS Name*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[3]/jv-multiselect/mat-form-field/div/div[3]/div/mat-error"),
          Map.entry(
              "Frequency*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[4]/mat-form-field[1]/div/div[3]/div/mat-error/p"),
          Map.entry(
              "Country/Region*",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[9]/jv-multiselect/mat-form-field/div/div[3]/div/mat-error"));

  public static Set<String> clientDefaultFields =
      Set.of("Apply Conversion Window*", "Client Start Date*");

  public static Map<String, DataType> clientFormDataTypes =
      Map.ofEntries(
          Map.entry("Client Name*", DataType.STRING),
          Map.entry("Exported Name*", DataType.STRING),
          Map.entry("Advertiser Name", DataType.STRING),
          Map.entry("Timezone*", DataType.DROPDOWN),
          Map.entry("ATS Name*", DataType.DROPDOWN),
          Map.entry("Frequency*", DataType.DROPDOWN),
          Map.entry("Apply Conversion Window*", DataType.DROPDOWN),
          Map.entry("ATS URL", DataType.STRING),
          Map.entry("Feed URL", DataType.STRING),
          Map.entry("Industry", DataType.DROPDOWN),
          Map.entry("Monthly Budget*", DataType.NUMBER),
          Map.entry("Client Start Date*", DataType.CALENDAR),
          Map.entry("Client End Date*", DataType.CALENDAR),
          Map.entry("Country/Region*", DataType.DROPDOWN),
          Map.entry("Mark down%", DataType.NUMBER),
          Map.entry("clientMarkDownCheckBox", DataType.CHECKBOX));

  public static String mandatoryFieldColorCode = "rgb(198, 40, 40)";

  public static Map<String, String> clientFormOptionalFields =
      Map.ofEntries(
          Map.entry(
              "Advertiser Name",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[2]/mat-form-field/div/div[1]/div/input"),
          Map.entry(
              "ATS URL",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[3]/mat-form-field/div/div[1]/div/input"),
          Map.entry(
              "Mark down%",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[9]/mat-form-field/div/div[1]/div/input"),
          Map.entry(
              "clientMarkDownCheckBox",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[9]/mat-form-field/div/div[1]/div/mat-checkbox"),
          Map.entry(
              "Industry",
              "/html/body/jv-root/jv-home/mat-sidenav-container/mat-sidenav-content/jv-content-viewer/jv-add-client/mat-card/form/mat-card/div[7]/jv-multiselect/mat-form-field/div/div[1]/div/mat-select"));
}
