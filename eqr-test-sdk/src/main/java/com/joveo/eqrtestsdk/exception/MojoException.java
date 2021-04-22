package com.joveo.eqrtestsdk.exception;

public class MojoException extends Exception {

  public MojoException(String message) {
    super(message);
  }

  public MojoException(Throwable cause) {
    super(cause);
  }

  public MojoException(String message, Throwable cause) {
    super(message, cause);
  }
}
