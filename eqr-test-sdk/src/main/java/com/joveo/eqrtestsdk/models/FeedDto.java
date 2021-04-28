package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;

@JsonSerialize
@JacksonXmlRootElement(localName = "jobs")
public class FeedDto {

  @JacksonXmlElementWrapper(useWrapping = false)
  private List<FeedJob> job;

  public FeedDto() {
    this.job = new ArrayList<>();
  }

  public void addJob(FeedJob jobDetail) {
    this.job.add(jobDetail);
  }

  public List<FeedJob> getJob() {
    return job;
  }

  public void setJob(List<FeedJob> job) {
    this.job = job;
  }
}
