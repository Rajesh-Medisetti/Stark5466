package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class PerClientOutboundJob {
  private String title;
  private String city;
  private String state;
  private String country;

  @JsonProperty("job_reference")
  private String jobReference;

  private String body;
  private String company;
  private String advertiser;

  @JsonProperty("mobile_friendly_apply")
  private String mobileFriendlyApply;

  private String category;

  @JsonProperty("html_jobs")
  private String htmlJobs;

  private String campaign;
  private String url;

  @JsonProperty("job_type")
  private String jobType;

  private String cpc;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC", shape = JsonFormat.Shape.STRING)
  @JsonProperty("posted_at")
  private LocalDate postedAt;

  @JsonProperty("tracking_url")
  private String trackingUrl;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getJobReference() {
    return jobReference;
  }

  public void setJobReference(String jobReference) {
    this.jobReference = jobReference;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getAdvertiser() {
    return advertiser;
  }

  public void setAdvertiser(String advertiser) {
    this.advertiser = advertiser;
  }

  public String getMobileFriendlyApply() {
    return mobileFriendlyApply;
  }

  public void setMobileFriendlyApply(String mobileFriendlyApply) {
    this.mobileFriendlyApply = mobileFriendlyApply;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getHtmlJobs() {
    return htmlJobs;
  }

  public void setHtmlJobs(String htmlJobs) {
    this.htmlJobs = htmlJobs;
  }

  public String getCampaign() {
    return campaign;
  }

  public void setCampaign(String campaign) {
    this.campaign = campaign;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getCpc() {
    return cpc;
  }

  public void setCpc(String cpc) {
    this.cpc = cpc;
  }

  public LocalDate getPostedAt() {
    return postedAt;
  }

  public void setPostedAt(LocalDate postedAt) {
    this.postedAt = postedAt;
  }

  public String getTrackingUrl() {
    return trackingUrl;
  }

  public void setTrackingUrl(String trackingUrl) {
    this.trackingUrl = trackingUrl;
  }
}
