package com.joveo.eqrtestsdk.exception;

public class SqsEventFailedException extends MojoException {
  public SqsEventFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public SqsEventFailedException(String message) {
    super(message);
  }
}
