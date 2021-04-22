package com.joveo.eqrtestsdk.models;

public class MojoData<T> {
  private String id;

  public String getId() {
    return id;
  }

  public T getFields() {
    return fields;
  }

  private T fields;
}
