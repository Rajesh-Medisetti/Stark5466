package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ComprehensiveOutboundJob {
  public String advertiser;
  public String country;

  @JsonFormat(
      pattern = "yyyy-MM-dd HH:mm:ss.SSS z",
      timezone = "UTC",
      shape = JsonFormat.Shape.STRING)
  public LocalDateTime date;

  public String city;
  public String referencenumber;
  public String description;
  public String title;
  public String type;
  public String url;
  public Object cpa;
  public String postalcode;
  public String cpc;
  public String category;
  public String state;
  public String company;

  public String getAdvertiser() {
    return advertiser;
  }

  public void setAdvertiser(String advertiser) {
    this.advertiser = advertiser;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getReferencenumber() {
    return referencenumber;
  }

  public void setReferencenumber(String referencenumber) {
    this.referencenumber = referencenumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Object getCpa() {
    return cpa;
  }

  public void setCpa(Object cpa) {
    this.cpa = cpa;
  }

  public String getPostalcode() {
    return postalcode;
  }

  public void setPostalcode(String postalcode) {
    this.postalcode = postalcode;
  }

  public String getCpc() {
    return cpc;
  }

  public void setCpc(String cpc) {
    this.cpc = cpc;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
