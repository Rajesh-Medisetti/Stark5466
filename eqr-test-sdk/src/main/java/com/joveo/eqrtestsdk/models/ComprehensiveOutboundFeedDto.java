package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

public class ComprehensiveOutboundFeedDto extends OutboundFeedDto {
  private String publisher;

  @JsonProperty("publisherurl")
  private String publisherUrl;

  @JacksonXmlProperty(localName = "job")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<ComprehensiveOutboundJob> job;

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getPublisherUrl() {
    return publisherUrl;
  }

  public void setPublisherUrl(String publisherUrl) {
    this.publisherUrl = publisherUrl;
  }

  public List<OutboundJob> getJobs() {
    List<? extends OutboundJob> bases = job;
    return (List<OutboundJob>) bases;
  }

  public void setJob(List<ComprehensiveOutboundJob> job) {
    this.job = job;
  }
}
