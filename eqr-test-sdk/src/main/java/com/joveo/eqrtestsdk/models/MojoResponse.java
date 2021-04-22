package com.joveo.eqrtestsdk.models;

import java.util.ArrayList;
import java.util.List;

public class MojoResponse<T> {
  public List<MojoData<T>> getData() {
    return data;
  }

  public MojoSummary getSummary() {
    return summary;
  }

  public MojoData<T> getFirstData() {
    return data.get(0);
  }

  public MojoResponse() {
    this.data = new ArrayList<>();
  }

  List<MojoData<T>> data;
  MojoSummary summary;
}
