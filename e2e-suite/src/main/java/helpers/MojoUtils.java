package helpers;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import java.util.Set;

public class MojoUtils {

  /**
   * Runs scheduler on all the clients passed to it.
   *
   * @param clientSet Set of all client objects
   * @throws TimeoutException exception timeout
   * @throws InterruptWaitException exception wait
   * @throws MojoException mojo exception
   */
  public static void runSchedulerAndRefreshCache(Set<Client> clientSet, Driver driver)
      throws TimeoutException, InterruptWaitException, MojoException, InterruptedException {
    System.out.println("client size is " + clientSet.size());
    for (Client client : clientSet) {
      System.out.println(client.getStats().getName() + " " + client.id);
      client.runScheduler();
    }
    Thread.sleep(1000);
    driver.refreshEntityCache();
    driver.refreshJobCount();
  }
}
