package helpers;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import java.util.List;
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
    //        for (Client client : clientSet) {
    //          System.out.println(client.getStats().getName() + " " + client.id);
    //          client.runScheduler();
    //        }

    clientSet.parallelStream()
        .forEach(
            (client) -> {
              try {
                client.runScheduler();
              } catch (MojoException e) {
                e.printStackTrace();
              }
              try {
                System.out.println(client.id + " " + client.getStats().getName());
              } catch (MojoException e) {
                e.printStackTrace();
              }
            });
    Thread.sleep(1000);
    driver.refreshEntityCache();
    driver.refreshJobCount();
  }

  /** . deleting Clients */
  public static void removeClientSet(Set<Client> clientSet) throws MojoException {
    for (Client client : clientSet) {
      client.removeClient();
    }
  }

  /** . deleting Inboundfeed */
  public static void removeInboundFeed(List<String> feeds, Driver driver)
      throws InvalidInputException {
    for (String feed : feeds) {
      driver.deleteInboundFeed(feed);
    }
  }
}
