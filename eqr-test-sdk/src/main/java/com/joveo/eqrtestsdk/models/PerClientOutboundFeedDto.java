package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

public class PerClientOutboundFeedDto extends OutboundFeedDto{
  @JsonProperty("generation_time")
  private String generationTime;

  @JsonProperty("jobs_count")
  private Integer jobsCount;

  @JacksonXmlProperty(localName = "job")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<PerClientOutboundJob> job;

  public String getGenerationTime() {
    return generationTime;
  }

  public void setGenerationTime(String generationTime) {
    this.generationTime = generationTime;
  }

  public Integer getJobsCount() {
    return jobsCount;
  }

  public void setJobs_Count(Integer jobsCount) {
    this.jobsCount = jobsCount;
  }

  public List<OutboundJob> getJobs() {
    List<? extends OutboundJob> outboundJobs = job;
    return (List<OutboundJob>) outboundJobs;
  }

  public void setJobs(List<PerClientOutboundJob> job) {
    this.job = job;
  }
}