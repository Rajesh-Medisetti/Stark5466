package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

public class ComprehensiveOutboundFeedDto {
  private String publisher;
  private String publisherurl;

  @JacksonXmlProperty(localName = "job")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<ComprehensiveOutboundJob> job;

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getPublisherurl() {
    return publisherurl;
  }

  public void setPublisherurl(String publisherurl) {
    this.publisherurl = publisherurl;
  }

  public List<ComprehensiveOutboundJob> getJobs() {
    return job;
  }

  public void setJob(List<ComprehensiveOutboundJob> job) {
    this.job = job;
  }
}
