package com.joveo.eqrtestsdk.exception;

public class InterruptWaitException extends MojoException {
  public InterruptWaitException(String message, Throwable cause) {
    super(message, cause);
  }

  public InterruptWaitException(String message) {
    super(message);
  }
}
