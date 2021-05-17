package com.joveo.eqrtestsdk.exception;

public class RedisIoException extends MojoException {
  public RedisIoException(String message, Throwable cause) {
    super(message, cause);
  }

  public RedisIoException(String message) {
    super(message);
  }
}
