package entitycreators;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.mojo.OutboundFeed;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.OutboundJob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutBoundJobCreator {

  /** . Create Map of OutBoundJob and ReNo */
  public static Map<String, OutboundJob> outBoundFeedJob(Client client, String pubId)
      throws MojoException, InterruptedException {

    Map<String, OutboundJob> map = new HashMap<>();

    OutboundFeed outboundFeed = client.getOutboundFeed(pubId);

    int cnt = 5;
    while (outboundFeed == null && cnt > 0) {
      Thread.sleep(5000);
      client.runScheduler();
      outboundFeed = client.getOutboundFeed(pubId);
      cnt--;
    }

    List<OutboundJob> outboundJobs = outboundFeed.getFeed().getJobs();

    for (OutboundJob outboundJob : outboundJobs) {

      map.put(outboundJob.referencenumber, outboundJob);
    }

    return map;
  }
}
