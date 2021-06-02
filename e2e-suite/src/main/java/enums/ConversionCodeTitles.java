package enums;

public enum ConversionCodeTitles {
  View_JS_Pixel("View JS Pixel"),
  View_Image_Pixel("View Image Pixel"),
  Apply_Start_JS_Pixel("Apply Start JS Pixel"),
  Apply_Start_Image_Pixel("Apply Start Image Pixel"),
  Apply_Finish_JS_Pixel("Apply Finish JS Pixel"),
  Apply_Finish_Image_Pixel("Apply Finish Image Pixel"),
  resume_JS_Pixel("resume JS Pixel"),
  resume_Image_Pixel("resume Image Pixel");

  private String value;

  public String getValue() {
    return this.value;
  }

  ConversionCodeTitles(String value) {
    this.value = value;
  }
}
