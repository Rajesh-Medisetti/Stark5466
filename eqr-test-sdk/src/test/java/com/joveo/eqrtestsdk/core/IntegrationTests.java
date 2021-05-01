package com.joveo.eqrtestsdk.core;

import com.joveo.eqrtestsdk.core.entities.Campaign;
import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.core.entities.Driver;
import com.joveo.eqrtestsdk.core.entities.JobGroup;
import com.joveo.eqrtestsdk.core.entities.Publisher;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.CampaignStats;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.ClientStats;
import com.joveo.eqrtestsdk.models.Freq;
import com.joveo.eqrtestsdk.models.Frequency;
import com.joveo.eqrtestsdk.models.JobFilter;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import com.joveo.eqrtestsdk.models.JobGroupStats;
import com.joveo.eqrtestsdk.models.JoveoEnvironment;
import com.joveo.eqrtestsdk.models.PublisherDto;
import com.joveo.eqrtestsdk.models.TimeZone;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegrationTests {

  private static Logger logger = LoggerFactory.getLogger(IntegrationTests.class);
  private static Driver driver;
  private static Client client;
  private static ClientDto clientCreationDto;
  private static ClientDto clientEditDto;
  private static Campaign campaign;
  private static CampaignDto campaignCreationDto;
  private static CampaignDto campaignEditDto;
  private static JobGroup jobGroup;
  private static JobGroupDto jobGroupCreationDto;
  private static JobGroupDto jobGroupEditDto;
  private static Publisher publisher;
  private static PublisherDto publisherDto;
  private static Integer oldNumber;
  private static Integer newNumber;
  private static Random random;

  @BeforeAll
  public static void setUp() throws MojoException {
    driver = Driver.start("reliability@joveo.com", "joveo1520", JoveoEnvironment.Staging);
    clientCreationDto = new ClientDto();
    clientEditDto = new ClientDto();
    campaignCreationDto = new CampaignDto();
    campaignEditDto = new CampaignDto();
    jobGroupCreationDto = new JobGroupDto();
    jobGroupEditDto = new JobGroupDto();
    publisherDto = new PublisherDto();
    random = new Random();
    oldNumber = random.nextInt(2000);
    newNumber = oldNumber + 17;
  }

  public void clientEditFlowTest() throws MojoException, ParseException {
    logger.info("Client Execution Started...");

    String oldFeed =
        "https://feeds-aragorn-devlocal-hcl-us-2.s3.amazonaws.com/Indeed-bid/feed-Indeed-bid-499e0593-0f6a-48f9-84fe-fa30317bbd90.xml";
    String newFeed = "https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml";
    LocalDate newEndDate = LocalDate.now().plusDays(4);

    clientCreationDto.setName("stark_" + oldNumber);
    clientCreationDto.setExportedName("stark_exp_" + oldNumber);
    clientCreationDto.setAdvertiserName("stark_adv_" + oldNumber);
    clientCreationDto.setTimezone(TimeZone.UTC_plus_01_00);
    clientCreationDto.setAts("Other");
    clientCreationDto.setAtsUrl("www.ats.com/" + oldNumber);
    clientCreationDto.setFrequency(Frequency._1_Hours);
    clientCreationDto.setApplyConvWindow(30);

    clientCreationDto.addFeed(
        "https://feeds-aragorn-devlocal-hcl-us-2.s3.amazonaws.com/Indeed-bid/feed-Indeed-bid-499e0593-0f6a-48f9-84fe-fa30317bbd90.xml");

    clientCreationDto.setIndustry("47");
    clientCreationDto.setBudget(4000.0);
    clientCreationDto.setStartDate(LocalDate.now().plusDays(1));
    clientCreationDto.setEndDate(LocalDate.now().plusDays(2));
    clientCreationDto.setCountry("PK");

    // Create Client
    client = driver.createClient(clientCreationDto);
    logger.info("Client Created Successfully...");

    // Client Editable Fields
    clientEditDto.setExportedName("stark_exp_" + newNumber);
    clientEditDto.setAdvertiserName("stark_adv_" + newNumber);
    clientEditDto.setAtsUrl("www.ats.com/" + newNumber);
    clientEditDto.setApplyConvWindow(17);

    clientEditDto.deleteFeed(oldFeed);
    clientEditDto.addFeed(newFeed);

    clientEditDto.setBudget(5000.0);
    clientEditDto.setEndDate(newEndDate);
    clientEditDto.setCountry("IN");

    // Edit Client
    client.edit(clientEditDto);

    ClientStats stats = client.getStats();

    LocalDate actualEndDate =
        LocalDate.parse(stats.getEndDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    List<String> feedUrls = client.getInboundFeeds();

    Assertions.assertEquals(newFeed, feedUrls.get(0), "Feeds mismatch in client");
    Assertions.assertEquals(newEndDate, actualEndDate, "End dates mismatch in client");
    Assertions.assertEquals(
        5000.0, stats.getBudgetCap().getValue(), "Monthly budget mismatch in client");
    Assertions.assertEquals("IN", stats.getCountry(), "Country mismatch in client");

    logger.info("Client edit flow succeeded...");
  }

  public void campaignEditFlowTest() throws MojoException {
    logger.info("Campaign Execution Started...");

    campaignCreationDto.setClientId(clientEditDto.getClientId());
    // Below are the fields in UI
    campaignCreationDto.setName("camp" + oldNumber);
    campaignCreationDto.setBudget(2000.0);
    campaignCreationDto.setStartDate(LocalDate.now().plusDays(1));
    campaignCreationDto.setEndDate(LocalDate.now().plusDays(2));

    // Create Campaign
    campaign = driver.createCampaign(campaignCreationDto);
    logger.info("Campaign Created Successfully...");

    // Update Campaign fields
    Double latestBudget = 1000.0;
    LocalDate latestEndDate = LocalDate.now().plusDays(4);

    campaignEditDto.setName("camp" + newNumber);
    campaignEditDto.setBudget(latestBudget);
    campaignEditDto.setEndDate(latestEndDate);

    // Edit Campaign
    campaign.edit(campaignEditDto);

    // Check whether changes are reflected or not
    CampaignStats stats = campaign.getStats();

    LocalDate actualEndDate =
        LocalDate.parse(stats.getEndDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    Assertions.assertEquals("camp" + newNumber, stats.getName(), "Campaign name mismatched");
    Assertions.assertEquals(
        latestBudget, stats.getBudgetCap().getValue(), "Campaign budget mismatched");
    Assertions.assertEquals(latestEndDate, actualEndDate, "Campaign end date mismatched");

    logger.info("Campaign Edit Flow Succeeded");
  }

  public void jobGroupEditFlowTest() throws MojoException {
    logger.info("Job Group Execution Started...");

    jobGroupCreationDto.setClientId(clientEditDto.getClientId());
    jobGroupCreationDto.setName("Job_Group_" + oldNumber);
    jobGroupCreationDto.setCampaignId(campaignEditDto.getCampaignId());
    jobGroupCreationDto.setStartDate(LocalDate.now().plusDays(2));
    jobGroupCreationDto.setEndDate(LocalDate.now().plusDays(4));
    jobGroupCreationDto.setJobFilter(
        JobFilter.and(
            JobFilter.eq(JobFilterFields.country, "Ind"),
            JobFilter.notBeginWith(JobFilterFields.state, "Tel")));
    jobGroupCreationDto.addIoDetail(
        "IO_" + oldNumber, oldNumber, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
    jobGroupCreationDto.setPriority(3);

    jobGroupCreationDto.setCpcBid(1.0);
    jobGroupCreationDto.setCpaBid(2.0);
    jobGroupCreationDto.addPerformanceTargets("cpc", 3.0);
    jobGroupCreationDto.addPerformanceTargets("cpa", 4.0);

    jobGroupCreationDto.setBudgetCap(false, Freq.Weekly, 80.0, 1000.0);
    jobGroupCreationDto.setClickCap(false, Freq.Weekly, 80.0, 1000);
    jobGroupCreationDto.setApplyCap(false, Freq.Weekly, 80.0, 1000);

    jobGroupCreationDto.setJobBudgetCap(false, Freq.Weekly, 80.0, 1000);
    jobGroupCreationDto.setJobClickCap(false, Freq.Weekly, 80.0, 1000);
    jobGroupCreationDto.setJobApplyCap(false, Freq.Weekly, 80.0, 1000);

    List<Integer> scheduledOn = new ArrayList<Integer>();
    scheduledOn.add(0);
    scheduledOn.add(3);
    jobGroupCreationDto.setDaysToSchedule(scheduledOn);
    jobGroupCreationDto.addPlacementWithBudgetAndBid(
        "Naukri", 1.3, 100.0, Freq.Monthly, true, 80.0, true);
    jobGroup = driver.createJobGroup(jobGroupCreationDto);
    logger.info("Job Group Created Successfully...");

    // Edit JobGroup
    jobGroupEditDto.setClientId(clientEditDto.getClientId());
    jobGroupEditDto.setName("Job_Group_" + newNumber);
    LocalDate expectedEndDate = LocalDate.now().plusDays(10);
    jobGroupEditDto.setEndDate(expectedEndDate);
    jobGroupEditDto.setJobFilter(
        JobFilter.and(
            JobFilter.eq(JobFilterFields.country, "india"),
            JobFilter.notBeginWith(JobFilterFields.state, "Andhra")));
    jobGroupEditDto.addIoDetail(
        "IO_" + newNumber, newNumber, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5));
    jobGroupEditDto.setPriority(5);

    jobGroupEditDto.setCpcBid(2.0);
    jobGroupEditDto.setCpaBid(3.0);
    jobGroupEditDto.addPerformanceTargets("cpc", 4.0);
    jobGroupEditDto.addPerformanceTargets("cpa", 5.0);

    jobGroupEditDto.setBudgetCap(true, Freq.Monthly, 50.0, 900.0);
    jobGroupEditDto.setClickCap(true, Freq.Monthly, 50.0, 900);
    jobGroupEditDto.setApplyCap(true, Freq.Monthly, 50.0, 900);

    jobGroupEditDto.setJobBudgetCap(true, Freq.Monthly, 90.0, 800);
    jobGroupEditDto.setJobClickCap(true, Freq.Monthly, 90.0, 800);
    jobGroupEditDto.setJobApplyCap(true, Freq.Monthly, 90.0, 800);

    scheduledOn.add(5);
    jobGroupEditDto.setDaysToSchedule(scheduledOn);
    jobGroupEditDto.addPlacementWithBudgetAndBid(
        "Naukri", 2.0, 110.0, Freq.Weekly, false, 85.0, false);

    jobGroup.edit(jobGroupEditDto);

    JobGroupStats stats = jobGroup.getStats();

    JobGroupDto.JobGroupParams.TradingGoals tradingGoals = stats.getTradingGoals();
    LocalDate actualEndDate =
        LocalDate.parse(stats.getEndDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate tradingStartDate = tradingGoals.getIoDetails().get(0).getStartDate();
    LocalDate tradingEndDate = tradingGoals.getIoDetails().get(0).getEndDate();

    Assertions.assertEquals("Job_Group_" + newNumber, stats.getName(), "JobGroup names mismatch");
    Assertions.assertEquals(expectedEndDate, actualEndDate, "End Dates mismatch in JobGroup");
    Assertions.assertEquals(
        "IO_" + newNumber,
        tradingGoals.getIoDetails().get(0).getNumber(),
        "IO number mismatch in job group");
    Assertions.assertEquals(
        newNumber, tradingGoals.getIoDetails().get(0).getValue(), "IO value mismatch in job group");
    Assertions.assertEquals(
        LocalDate.now().plusDays(3), tradingStartDate, "IO start date mismatch in job group");
    Assertions.assertEquals(
        LocalDate.now().plusDays(5), tradingEndDate, "IO end date mismatch in job group");
    Assertions.assertEquals(5, stats.getPriority(), "Priority mismatch in job group");
    Assertions.assertEquals("2.00", stats.getCpcBid(), "CPC bid mismatch in job group");
    Assertions.assertEquals("3.00", stats.getCpaBid(), "CPA bid mismatch in job group");
    Assertions.assertEquals(
        4, stats.getTradingGoalsCpc(), "Trading goals CPC bid mismatch in job group");
    Assertions.assertEquals(
        5, stats.getTradingGoalsCpa(), "Trading goals CPA bid mismatch in job group");

    Assertions.assertEquals(
        true, stats.getBudgetCap().getPacing(), "Budget cap - pacing mismatch in job group");
    Assertions.assertEquals(
        Freq.Monthly,
        stats.getBudgetCap().getFreq(),
        "Budget cap - frequency mismatch in job group");
    Assertions.assertEquals(
        50, stats.getBudgetCap().getThreshold(), "Budget cap - threshold mismatch in job group");
    Assertions.assertEquals(
        900, stats.getBudgetCap().getValue(), "Budget cap - value mismatch in job group");

    Assertions.assertEquals(
        true, stats.getClicksCap().getPacing(), "Click cap - pacing mismatch in job group");
    Assertions.assertEquals(
        Freq.Monthly,
        stats.getClicksCap().getFreq(),
        "Click cap - frequency mismatch in job group");
    Assertions.assertEquals(
        50, stats.getClicksCap().getThreshold(), "Click cap - threshold mismatch in job group");
    Assertions.assertEquals(
        900, stats.getClicksCap().getValue(), "Click cap - value mismatch in job group");

    Assertions.assertEquals(
        true, stats.getAppliesCap().getPacing(), "Apply cap - pacing mismatch in job group");
    Assertions.assertEquals(
        Freq.Monthly,
        stats.getAppliesCap().getFreq(),
        "Apply cap - frequency mismatch in job group");
    Assertions.assertEquals(
        50, stats.getAppliesCap().getThreshold(), "Apply cap - threshold mismatch in job group");
    Assertions.assertEquals(
        900, stats.getAppliesCap().getValue(), "Apply cap - value mismatch in job group");

    Assertions.assertEquals(
        true, stats.getJobBudgetCap().getPacing(), "Job Budget cap - pacing mismatch in job group");
    Assertions.assertEquals(
        Freq.Monthly,
        stats.getJobBudgetCap().getFreq(),
        "Job Budget cap - frequency mismatch in job group");
    Assertions.assertEquals(
        90,
        stats.getJobBudgetCap().getThreshold(),
        "Job Budget cap - threshold mismatch in job group");
    Assertions.assertEquals(
        800, stats.getJobBudgetCap().getValue(), "Job Budget cap - value mismatch in job group");

    Assertions.assertEquals(
        true, stats.getJobClicksCap().getPacing(), "Job Click cap - pacing mismatch in job group");
    Assertions.assertEquals(
        Freq.Monthly,
        stats.getJobClicksCap().getFreq(),
        "Job Click cap - frequency mismatch in job group");
    Assertions.assertEquals(
        90,
        stats.getJobClicksCap().getThreshold(),
        "Job Click cap - threshold mismatch in job group");
    Assertions.assertEquals(
        800, stats.getJobClicksCap().getValue(), "Job Click cap - value mismatch in job group");

    Assertions.assertEquals(
        true, stats.getJobAppliesCap().getPacing(), "Job Apply cap - pacing mismatch in job group");
    Assertions.assertEquals(
        Freq.Monthly,
        stats.getJobAppliesCap().getFreq(),
        "Job Apply cap - frequency mismatch in job group");
    Assertions.assertEquals(
        90,
        stats.getJobAppliesCap().getThreshold(),
        "Job Apply cap - threshold mismatch in job group");
    Assertions.assertEquals(
        800, stats.getJobAppliesCap().getValue(), "Job Apply cap - value mismatch in job group");

    Assertions.assertEquals(
        Arrays.asList(0, 3, 5), stats.getDaysToSchedule(), "Schedule days mismatch in job group");

    Assertions.assertEquals(
        2, stats.getPlacements().get(0).getBid(), "Placement bid mismatch in job group");
    Assertions.assertEquals(
        110,
        stats.getPlacements().get(0).getBudget().getValue(),
        "Placement budget mismatch in job group");
    Assertions.assertEquals(
        Freq.Weekly,
        stats.getPlacements().get(0).getBudget().getFreq(),
        "Placement frequency mismatch in job group");
    Assertions.assertEquals(
        false,
        stats.getPlacements().get(0).getBudget().getPacing(),
        "Placement pacing mismatch in job group");
    Assertions.assertEquals(
        85,
        stats.getPlacements().get(0).getBudget().getThreshold(),
        "Placement threshold mismatch in job group");
    Assertions.assertEquals(
        false,
        stats.getPlacements().get(0).getBudget().getLocked(),
        "Placement budget locked mismatch in job group");

    logger.info("Job Group edit flow succeeded");
  }

  void publisherEditFlowTest() throws MojoException {
    logger.info("Placement Creation Started...");
    publisherDto.setName("pb_test_" + oldNumber);
    publisherDto.setPublisherUrl("www.publisher.com/");
    publisherDto.setBidType("CPC");
    publisherDto.setMinBid(1.0);

    publisher = driver.createPublisher(publisherDto);
    driver.refreshEntityCache();

    publisher.editMinBid(1.2);

    Assertions.assertEquals(1.2, publisher.getMinBid(), "MinBid mismatch in Placement");
    logger.info("Placement edit flow succeeded..");
  }

  @Test
  public void editFlowTest() throws MojoException, ParseException {
    clientEditFlowTest();
    campaignEditFlowTest();
    publisherEditFlowTest();
    jobGroupEditFlowTest();
  }
}
