package com.joveo.eqrtestsdk.exception;

public class UnexpectedResponseException extends MojoException {

  public UnexpectedResponseException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnexpectedResponseException(String message) {
    super(message);
  }
}
