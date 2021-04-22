package com.joveo.eqrtestsdk.core.models;

import java.time.LocalDateTime;

public class SchedulerRunMetadata {
  private String executionId;
  private LocalDateTime time;

  public SchedulerRunMetadata(String executionId, LocalDateTime time) {
    this.executionId = executionId;
    this.time = time;
  }

  public String getExecutionId() {
    return executionId;
  }

  /**
   * .
   *
   * @return time
   */
  public LocalDateTime getTime() {
    if (this.executionId.equals("")) {
      return LocalDateTime.now().minusYears(1);
    } else {
      return time;
    }
  }
}
