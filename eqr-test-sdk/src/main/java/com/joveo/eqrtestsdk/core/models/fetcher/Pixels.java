package com.joveo.eqrtestsdk.core.models.fetcher;

import java.util.List;

public class Pixels {

  List<Records> records;

  public List<Records> getRecords() {
    return records;
  }

  public void setRecords(List<Records> records) {
    this.records = records;
  }

  public static class Records {

    String id;
    String title;
    String code;
    Boolean status;
    String conversionName;
    String conversionType;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public Boolean getStatus() {
      return status;
    }

    public void setStatus(Boolean status) {
      this.status = status;
    }

    public String getConversionName() {
      return conversionName;
    }

    public void setConversionName(String conversionName) {
      this.conversionName = conversionName;
    }

    public String getConversionType() {
      return conversionType;
    }

    public void setConversionType(String conversionType) {
      this.conversionType = conversionType;
    }
  }
}
