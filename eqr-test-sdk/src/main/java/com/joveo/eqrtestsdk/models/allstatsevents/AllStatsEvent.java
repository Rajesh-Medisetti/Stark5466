package com.joveo.eqrtestsdk.models.allstatsevents;

public enum AllStatsEvent {
  BOT_CLICK(0),
  CLICK(1),
  APPLY_START(2),
  APPLY_FINISH(3);

  private int value;

  public int getValue() {
    return this.value;
  }

  AllStatsEvent(int value) {
    this.value = value;
  }
}
