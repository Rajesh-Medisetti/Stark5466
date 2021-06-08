package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonSerialize
@Valid
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PauseDto {

  @Size(min = 1, message = "pause at least one automation")
  @NotNull
  List<String> ids;

  @NotNull(message = "enter status to change for automation")
  Status status;

  public List<String> getIds() {
    return ids;
  }

  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
