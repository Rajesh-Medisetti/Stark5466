package com.joveo.eqrtestsdk.api;

import com.joveo.eqrtestsdk.exception.MojoException;
import java.time.Duration;

public interface Waitable {

  public Boolean isComplete() throws MojoException;

  public Duration getTimeout();

  public String getWaiterMessage();

  public Duration getRefreshInterval();
}
