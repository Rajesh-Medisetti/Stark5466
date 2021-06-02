package helpers;

import static helpers.Utils.getRandomString;
import static helpers.Utils.getRandomStringsOfGivenSize;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.exception.InterruptWaitException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.TimeoutException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.FeedDto;
import com.joveo.eqrtestsdk.models.FeedJob;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.Stats;
import dtos.AllEntities;
import dtos.Dtos;
import dtos.SponsoredStatsInfo;
import entitycreators.CampaignEntityCreator;
import entitycreators.ClientEntityCreator;
import entitycreators.InBoundFeedCreator;
import entitycreators.JobCreator;
import entitycreators.JobGroupCreator;
import enums.BidLevel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    //            for (Client client : clientSet) {
    //              System.out.println(client.getStats().getName() + " " + client.id);
    //              client.runScheduler();
    //            }

    // ForkJoinPool pool = new ForkJoinPool(2);

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

    for (int i = 0; i < 5; i++) {
      Thread.sleep(20000);
      driver.refreshEntityCache();
      driver.refreshJobCount();
    }
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

  /**
   * This method creates a client along with campaign and job group for that client.
   *
   * @param driver driver
   * @param publisher publisher
   * @param bidLevel bidLevel
   * @return AllEntities object
   * @throws MojoException Mojo Exception
   */
  public static AllEntities createClientAlongWithCampaignAndJobGroup(
      Driver driver, String publisher, BidLevel bidLevel) throws MojoException {
    List<String> countriesForJobGroup = getRandomStringsOfGivenSize(Utils.getRandomNumber(3, 5));
    // clientDto
    ClientDto clientDto = ClientEntityCreator.randomClientCreator(false, 0);
    // campaignDto
    CampaignDto campaignDto = CampaignEntityCreator.randomCampaignCreator(1000);
    // JobGroupDto
    JobGroupDto jobGroupDto =
        JobGroupCreator.dtoWithIN(
            JobFilterFields.country, countriesForJobGroup, 100.0, 1.0, BidLevel.PLACEMENT, 1);
    jobGroupDto.addPlacementWithBid(publisher, 2.0);

    List<Dtos> dtos = new ArrayList<>();
    dtos.add(new Dtos(clientDto, campaignDto, jobGroupDto, bidLevel, 0));

    // inbound feed url
    JobCreator jobCreator = new JobCreator();
    jobCreator.jobProvider(dtos);

    // set url to client
    clientDto.addFeed(jobCreator.clientUrlMap.get(clientDto));

    // create client, campaign, job group
    Client client = driver.createClient(clientDto);

    campaignDto.setClientId(client.id);
    Campaign campaign = driver.createCampaign(campaignDto);

    jobGroupDto.setClientId(client.id);
    jobGroupDto.setCampaignId(campaign.id);
    JobGroup jobGroup = driver.createJobGroup(jobGroupDto);

    AllEntities allEntities =
        new AllEntities(
            client,
            clientDto,
            campaign,
            campaignDto,
            jobGroup,
            jobGroupDto,
            bidLevel,
            0,
            jobCreator);
    return allEntities;
  }

  /**
   * This method adds a list of jobs to a given client.
   *
   * @param driver driver
   * @param publisher publisher
   * @param bidLevel bidLevel
   * @param jobCreator jobCreator
   * @param allEntities allEntities
   * @return AllEntities Object
   * @throws MojoException Mojo Exception
   */
  public static AllEntities addNewJobGroupToExistingClient(
      Driver driver,
      String publisher,
      BidLevel bidLevel,
      JobCreator jobCreator,
      AllEntities allEntities)
      throws MojoException {
    List<String> countriesForJobGroup = getRandomStringsOfGivenSize(Utils.getRandomNumber(1, 3));

    ClientDto editClientDto = new ClientDto();
    editClientDto.deleteFeed(jobCreator.clientUrlMap.get(allEntities.getClientDto()));

    JobGroupDto jobGroupDto =
        JobGroupCreator.dtoWithIN(
            JobFilterFields.country, countriesForJobGroup, 100.0, 1.0, BidLevel.PLACEMENT, 1);
    jobGroupDto.addPlacementWithBid(publisher, 2.0);

    List<Dtos> dtos = new ArrayList<>();
    dtos.add(
        new Dtos(
            allEntities.getClientDto(), allEntities.getCampaignDto(), jobGroupDto, bidLevel, 0));

    JobCreator jobCreator1 = allEntities.getJobCreator();
    jobCreator1.jobProvider(dtos);

    editClientDto.addFeed(jobCreator.clientUrlMap.get(allEntities.getClientDto()));

    allEntities.getClient().edit(editClientDto);

    jobGroupDto.setClientId(allEntities.getClient().id);
    jobGroupDto.setCampaignId(allEntities.getCampaign().id);
    JobGroup jobGroup = driver.createJobGroup(jobGroupDto);

    AllEntities allEntities1 =
        new AllEntities(
            allEntities.getClient(),
            allEntities.getClientDto(),
            allEntities.getCampaign(),
            allEntities.getCampaignDto(),
            jobGroup,
            jobGroupDto,
            bidLevel,
            0,
            jobCreator1);
    return allEntities1;
  }

  /**
   * This method is used to add a given job to existing inbound feed based on title and reference id
   * provided.
   *
   * @param jobCreator jobCreator
   * @param isTitleSame isTitleSame
   * @param isReqSame isReqSame
   * @param jobs jobs
   * @param allEntities allEntities
   * @throws MojoException Mojo Exception
   */
  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
  public static void addNewJobToInboundFeed(
      JobCreator jobCreator,
      boolean isTitleSame,
      boolean isReqSame,
      int jobs,
      AllEntities allEntities)
      throws MojoException {
    Client client = allEntities.getClient();
    ClientDto clientDto = allEntities.getClientDto();
    JobGroupDto jobGroupDto = allEntities.getJobGroupDto();
    ClientDto editClientDto = new ClientDto();
    editClientDto.deleteFeed(jobCreator.clientUrlMap.get(clientDto));

    if (jobs > jobCreator.clientFeedMap.get(clientDto).getJob().size()) {
      return;
    }

    FeedDto inboundFeedDto = jobCreator.clientFeedMap.get(clientDto);
    for (int i = 0; i < jobs; i++) {
      FeedJob job = new FeedJob();
      FeedJob inboundJob = inboundFeedDto.getJob().get(i);
      if (isReqSame && isTitleSame) {
        job.setReferenceNumber(inboundJob.getReferenceNumber());
        job.setTitle(inboundJob.getTitle());
      } else if (isReqSame) {
        job.setReferenceNumber(inboundJob.getReferenceNumber());
      } else if (isTitleSame) {
        job.setTitle(inboundJob.getTitle());
        job.setReferenceNumber(++JobCreator.refNo);
      } else {
        job.setReferenceNumber(++JobCreator.refNo);
      }
      job.setCountry(inboundJob.getCountry());
      InBoundFeedCreator.setDefaultValues(getRandomString(10), job);
      inboundFeedDto.addJob(job);
    }

    Map<ClientDto, FeedDto> clientFeeds = new HashMap<>();
    clientFeeds.put(clientDto, inboundFeedDto);
    String feedUrl = InBoundFeedCreator.feedCreator(clientFeeds).get(clientDto);

    editClientDto.addFeed(feedUrl);
    client.edit(editClientDto);

    jobCreator.clientUrlMap.put(clientDto, feedUrl);
    jobCreator.clientFeedMap.put(clientDto, inboundFeedDto);
    jobCreator.jobGroupDtoFeedDtoMap.put(jobGroupDto, inboundFeedDto);
  }

  /**
   * This method creates clients for given client ids.
   *
   * @param clientIds ClientIds
   * @param driver Driver
   * @return List of clients
   * @throws MojoException Mojo Exception
   */
  public Map<Client, SponsoredStatsInfo> getClients(List<String> clientIds, Driver driver)
      throws MojoException {
    Map<Client, SponsoredStatsInfo> clientSponsoredStatsInfoMap = new HashMap<>();
    for (String clientId : clientIds) {
      Client client = driver.getExistingClient(clientId);
      Stats clientLevelStats = client.getStats();
      SponsoredStatsInfo sponsoredStatsInfo =
          new SponsoredStatsInfo(
              clientLevelStats.getClicks(),
              clientLevelStats.getBotClicks(),
              clientLevelStats.getApplyStarts(),
              clientLevelStats.getApplies());
      clientSponsoredStatsInfoMap.put(client, sponsoredStatsInfo);
    }
    return clientSponsoredStatsInfoMap;
  }
}
