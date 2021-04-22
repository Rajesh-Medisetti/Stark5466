package com.joveo.eqrtestsdk.api;

import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wait {
  private static Logger logger = LoggerFactory.getLogger(Wait.class);

  /**
   * .
   *
   * @param waiter waiter
   * @return status status
   * @throws TimeoutException on TimeOut
   * @throws InterruptWaitException on Interrupt
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException On unexpected Response
   * @throws MojoException On unexpected behaviour
   */
  public static Boolean until(Waitable waiter)
      throws TimeoutException, InterruptWaitException, ApiRequestException,
          UnexpectedResponseException, MojoException {
    LocalDateTime end = LocalDateTime.now().plus(waiter.getTimeout());
    while (true) {
      Boolean status = waiter.isComplete();
      if (Boolean.TRUE.equals(status)) {
        return true;
      }
      if (end.isBefore(LocalDateTime.now())) {
        logger.error(
            "Timeout encountered after "
                + waiter.getTimeout().getSeconds()
                + " seconds: "
                + waiter.getWaiterMessage());
        throw new TimeoutException(
            "Timeout encountered after "
                + waiter.getTimeout().getSeconds()
                + " seconds: "
                + waiter.getWaiterMessage());
      }
      try {
        Thread.sleep(waiter.getRefreshInterval().toMillis());
      } catch (InterruptedException e) {
        logger.error("Wait for " + waiter.getWaiterMessage() + " failed due to an interrupt");
        throw new InterruptWaitException(
            "Wait for " + waiter.getWaiterMessage() + " failed due to an interrupt", e);
      }
    }
  }

  private Wait() {}
}
