package com.joveo.eqrtestsdk.models.clickmeterevents;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApplyEvent {
  private String event;
  private String timestamp;
  private String id;
  private Data data;

  public ApplyEvent() {
    this.data = new Data();
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp.toInstant(ZoneOffset.UTC).toString();
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * This method is used to set data information.
   *
   * @param clickEvent Click Event
   * @param conversionId Conversion Id
   * @param conversionRef Conversion Ref
   * @param requestQuery Request Query
   */
  public void setData(
      ClickEvent clickEvent, long conversionId, String conversionRef, String requestQuery) {
    this.data.click = clickEvent;
    this.data.conversion.id = conversionId;
    this.data.conversion.ref = conversionRef;
    this.data.request.query = requestQuery;
  }

  public static class Data {
    public ClickEvent click;
    public Conversion conversion;
    public Request request;

    public Data() {
      this.conversion = new Conversion();
      this.request = new Request();
    }

    public static class Conversion {
      public long id;
      public String ref;
    }

    public static class Request {
      public String query;
    }
  }
}
