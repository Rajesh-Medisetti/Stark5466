package com.joveo.eqrtestsdk.core.entities;

public class Automation {

  public final String id;
  private final String clientId;
  private final Driver driver;

  /** . Constructor for Automation */
  public Automation(String id, String clientId, Driver driver) {
    this.id = id;
    this.clientId = clientId;
    this.driver = driver;
  }
}
