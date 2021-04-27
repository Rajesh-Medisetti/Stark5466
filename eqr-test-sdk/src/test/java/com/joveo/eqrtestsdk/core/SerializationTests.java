package com.joveo.eqrtestsdk.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.fixtures.TestServices;
import com.joveo.eqrtestsdk.fixtures.TestSession;
import com.joveo.eqrtestsdk.models.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class SerializationTests {

  private static ObjectMapper objectMapper;

  @BeforeAll
  public static void setUp() {
    objectMapper = TestServices.setup("staging").getObjectMapper();
  }

  @Test
  public void JobGroupFiltersCreation01_JobGroupService_Assertions()
      throws JsonProcessingException, JSONException {
    List<String> countries = new ArrayList<>();
    countries.add("india");
    countries.add("Srilanka");
    countries.add("China");
    countries.add("Japan");
    List<String> dates = new ArrayList<>();
    dates.add("04/01/2021");
    dates.add("04/19/2021");
    GroupingJobFilter groupingJobFilter =
        JobFilter.or(
            JobFilter.eq(JobFilterFields.category, "Recruitment"),
            JobFilter.or(
                JobFilter.and(
                    JobFilter.eq(JobFilterFields.city, "Hyderabad"),
                    JobFilter.beginWith(JobFilterFields.state, "Tel")),
                JobFilter.in(JobFilterFields.country, countries),
                JobFilter.or(
                    JobFilter.eq(JobFilterFields.zip, "534186"),
                    JobFilter.between(JobFilterFields.postedDate, dates))));
    String actualValue = objectMapper.writeValueAsString(groupingJobFilter);
    String expectedValue =
        "{\"operator\":\"OR\",\"rules\":[{\"operator\":\"EQUAL\",\"field\":\"category\",\"data\":\"Recruitment\"},{\"operator\":\"OR\",\"rules\":[{\"operator\":\"AND\",\"rules\":[{\"operator\":\"EQUAL\",\"field\":\"city\",\"data\":\"Hyderabad\"},{\"operator\":\"BEGINS_WITH\",\"field\":\"state\",\"data\":\"Tel\"}]},{\"operator\":\"IN\",\"field\":\"country\",\"data\":[\"india\",\"Srilanka\",\"China\",\"Japan\"]},{\"operator\":\"OR\",\"rules\":[{\"operator\":\"EQUAL\",\"field\":\"zip\",\"data\":\"534186\"},{\"operator\":\"BETWEEN\",\"field\":\"postedDate\",\"data\":[\"04/01/2021\",\"04/19/2021\"]}]}]}]}";
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.STRICT);
  }

  @Test
  public void JobGroupFiltersCreation02_JobGroupService_Assertions()
      throws JsonProcessingException, JSONException {
    List<String> city1 = new ArrayList<String>();
    city1.add("Hyderabad");
    city1.add("Vizag");
    List<String> city2 = new ArrayList<String>();
    city2.add("karanataka");
    List<String> country = new ArrayList<String>();
    country.add("india");
    country.add("srilanka");
    GroupingJobFilter groupingJobFilter =
        JobFilter.or(
            JobFilter.and(
                JobFilter.in(JobFilterFields.country, country),
                JobFilter.lessThanEqual(JobFilterFields.cpcBid, "5")),
            JobFilter.and(
                JobFilter.contains(JobFilterFields.category, "recruitment"),
                JobFilter.notBeginWith(JobFilterFields.zip, "534"),
                JobFilter.in(JobFilterFields.state, city2)),
            JobFilter.and(
                JobFilter.notEndWith(JobFilterFields.refNumber, "234"),
                JobFilter.on(JobFilterFields.postedDate, "04/08/2021"),
                JobFilter.notIn(JobFilterFields.city, city1)),
            JobFilter.and(
                JobFilter.beginWith(JobFilterFields.title, "Software"),
                JobFilter.lessThan(JobFilterFields.postedDate, "10")),
            JobFilter.greaterThanEqual(JobFilterFields.cpcBid, "2"));
    String actualValue = objectMapper.writeValueAsString(groupingJobFilter);
    System.out.println(actualValue);
    String expectedValue =
        "{\"operator\":\"OR\",\"rules\":[{\"operator\":\"AND\",\"rules\":[{\"operator\":\"IN\",\"field\":\"country\",\"data\":[\"india\",\"srilanka\"]},{\"operator\":\"LESS_THAN_EQUAL\",\"field\":\"cpcBid\",\"data\":\"5\"}]},{\"operator\":\"AND\",\"rules\":[{\"operator\":\"CONTAINS\",\"field\":\"category\",\"data\":\"recruitment\"},{\"operator\":\"NOT_BEGINS_WITH\",\"field\":\"zip\",\"data\":\"534\"},{\"operator\":\"IN\",\"field\":\"state\",\"data\":[\"karanataka\"]}]},{\"operator\":\"AND\",\"rules\":[{\"operator\":\"NOT_ENDS_WITH\",\"field\":\"refNumber\",\"data\":\"234\"},{\"operator\":\"ON\",\"field\":\"postedDate\",\"data\":\"04/08/2021\"},{\"operator\":\"NOT_IN\",\"field\":\"city\",\"data\":[\"Hyderabad\",\"Vizag\"]}]},{\"operator\":\"AND\",\"rules\":[{\"operator\":\"BEGINS_WITH\",\"field\":\"title\",\"data\":\"Software\"},{\"operator\":\"LESS_THAN\",\"field\":\"postedDate\",\"data\":\"10\"}]},{\"operator\":\"GREATER_THAN_EQUAL\",\"field\":\"cpcBid\",\"data\":\"2\"}]}";
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.STRICT);
  }

  @Test
  public void ClientCreation_ClientService_AssertEquals()
      throws JsonProcessingException, JSONException {
    String expectedValue =
        "{\"params\":{\"name\":\"Stark\",\"country\":\"IN\",\"exportedName\":\"stark\",\"advertiserName\":\"stark family\",\"ats\":\"Zoho\",\"atsUrl\":\"www.zoho.com\",\"frequency\":\"6 hours\",\"applyConvWindow\":20,\"timezone\":\"c1d3\",\"type\":\"DirectEmployer\",\"industry\":\"47\",\"excludedPublishers\":\"\",\"endDate\":\"06/01/2021\",\"startDate\":\"04/23/2021\",\"markdown\":\"\",\"feeds\":[{\"xmlFeedUrl\":\"https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml\",\"id\":null,\"schemaMappings\":{\"schemaMappingsJobCollection\":\"Jobs\",\"schemaMappingsJob\":\"job\",\"schemaMappingsCompany\":null,\"schemaMappingsTitle\":\"title\",\"schemaMappingsCity\":\"city\",\"schemaMappingsState\":\"state\",\"schemaMappingsCountry\":\"country\",\"schemaMappingsDescription\":\"description\",\"schemaMappingsURL\":\"url\",\"schemaMappingsZip\":null,\"schemaMappingsCategory\":\"category\",\"schemaMappingsDatePosted\":\"date\",\"schemaMappingsRefNumber\":\"referencenumber\",\"schemaMappingsModifiedDate\":null,\"schemaMappingsPublishedDate\":null,\"schemaMappingsCPCBid\":\"cpc\",\"schemaMappingsType\":null,\"schemaMappingAdditional\":{},\"schemaMappingPublisher\":{}},\"mandatoryFields\":[\"source\",\"job\",\"title\",\"description\",\"url\",\"referencenumber\"]}],\"sjCreate\":false,\"budgetCap\":{\"value\":10000,\"pacing\":false,\"freq\":\"Monthly\"},\"industries\":[\"47\"],\"globallyExcludedPublishers\":\"\"}}";
    ClientDto clientDTO = new ClientDto();
    clientDTO.setName("Stark");
    clientDTO.setExportedName("stark");
    clientDTO.setAdvertiserName("stark family");
    clientDTO.setTimezone(TimeZone.UTC_plus_05_30);
    clientDTO.setAts("Zoho");
    clientDTO.setAtsUrl("www.zoho.com");
    clientDTO.setFrequency(Frequency._6_Hours);
    clientDTO.setApplyConvWindow(20);
    clientDTO.addFeed("https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml");
    ClientDto.ClientParams.Feeds feed = clientDTO.getFeeds().get(0);
    feed.schemaMappings.schemaMappingsJobCollection = "Jobs";
    feed.schemaMappings.schemaMappingsJob = "job";
    feed.schemaMappings.schemaMappingsTitle = "title";
    feed.schemaMappings.schemaMappingsCity = "city";
    feed.schemaMappings.schemaMappingsState = "state";
    feed.schemaMappings.schemaMappingsDescription = "description";
    feed.schemaMappings.schemaMappingsCountry = "country";
    feed.schemaMappings.schemaMappingsUrl = "url";
    feed.schemaMappings.schemaMappingsRefNumber = "referencenumber";
    feed.schemaMappings.schemaMappingsCpcBid = "cpc";
    feed.schemaMappings.schemaMappingsDatePosted = "date";
    feed.schemaMappings.schemaMappingsCategory = "category";
    clientDTO.setIndustry("47");
    clientDTO.setBudget(10000.0);
    clientDTO.setStartDate(
        LocalDate.parse("04/23/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    clientDTO.setEndDate(LocalDate.parse("06/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    clientDTO.setCountry("IN");
    clientDTO.setDefaultValues();
    String actualValue = objectMapper.writeValueAsString(clientDTO);
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.LENIENT);
  }

  @Test
  public void CampaignCreation_CampaignService_AssertEquals()
      throws JsonProcessingException, JSONException {
    String expectedValue =
        "{\"params\":{\"clientId\":\"c5e29e5a-477b-48b4-aa26-4ffe345d95fc\",\"clientIds\":[\"c5e29e5a-477b-48b4-aa26-4ffe345d95fc\"],\"name\":\"Camp00\",\"startDate\":\"04/23/2021\",\"endDate\":\"06/01/2021\",\"budgetCap\":{\"value\":17000,\"pacing\":false,\"freq\":\"Monthly\"}}}";
    CampaignDto campaignDTO = new CampaignDto();
    campaignDTO.setClientId("c5e29e5a-477b-48b4-aa26-4ffe345d95fc");
    campaignDTO.setName("Camp00");
    campaignDTO.setBudget(17000.0);
    campaignDTO.setStartDate(
        LocalDate.parse("04/23/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    campaignDTO.setEndDate(
        LocalDate.parse("06/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    String actualValue = objectMapper.writeValueAsString(campaignDTO);
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.STRICT);
  }

  @Test
  public void JobGroupCreation_JobGroupService_AssertEquals()
      throws JsonProcessingException, JSONException {
    String expectedValue =
        "{\"params\":{\"name\":\"JG00\",\"campaignId\":\"10a3755d-73c2-4014-9e07-ca6e49dec67f\",\"startDate\":\"04/23/2021\",\"endDate\":\"06/01/2021\",\"priority\":1,\"category\":\"\",\"cpcBid\":1,\"cpaBid\":2,\"clientId\":\"c5e29e5a-477b-48b4-aa26-4ffe345d95fc\",\"clientIds\":[\"c5e29e5a-477b-48b4-aa26-4ffe345d95fc\"],\"sign\":\"$\",\"filters\":{\"operator\":\"AND\",\"rules\":[{\"operator\":\"EQUAL\",\"field\":\"country\",\"data\":\"India\"},{\"operator\":\"GREATER_THAN_EQUAL\",\"field\":\"cpcBid\",\"data\":\"5\"},{\"operator\":\"OR\",\"rules\":[{\"operator\":\"NOT_EQUAL\",\"field\":\"state\",\"data\":\"Telangana\"},{\"operator\":\"NOT_EQUAL\",\"field\":\"zip\",\"data\":\"13d12\"}]},{\"operator\":\"AND\",\"rules\":[{\"operator\":\"CONTAINS\",\"field\":\"city\",\"data\":\"Hyd\"},{\"operator\":\"NOT_CONTAINS\",\"field\":\"title\",\"data\":\"software\"}]}]},\"budgetCap\":{\"pacing\":false,\"freq\":\"Monthly\",\"threshold\":80,\"value\":1111},\"clicksCap\":{\"pacing\":true,\"freq\":\"Monthly\",\"threshold\":80,\"value\":1111},\"appliesCap\":{\"pacing\":true,\"freq\":\"Monthly\",\"threshold\":80,\"value\":1111},\"overspendCap\":{\"maxJobCount\": null},\"jobBudgetCap\":{\"pacing\":false,\"freq\":\"Lifetime\",\"threshold\":80,\"value\":1111},\"jobClicksCap\":{\"pacing\":false,\"freq\":\"Lifetime\",\"threshold\":80,\"value\":1111},\"jobAppliesCap\":{\"pacing\":false,\"freq\":\"Lifetime\",\"threshold\":80,\"value\":1111},\"tradingGoals\":{\"ioDetails\":[{\"number\":\"123io\",\"value\":170,\"startDate\":\"04/23/2021\",\"endDate\":\"04/30/2021\"}],\"performanceTargets\":[{\"type\":\"cpc\",\"value\":1},{\"type\":\"cpa\",\"value\":1}]},\"placements\":[{\"pValue\":\"Naukri\",\"bid\":1.5,\"budget\":{\"pacing\":false,\"freq\":\"Monthly\",\"threshold\":80,\"value\":100,\"locked\":true}}],\"recommendationAudit\":{\"result\":[],\"acceptedResult\":[]},\"daysToSchedule\":[1,2,3,4,5],\"isPPC\":true}}";
    JobGroupDto jobGroupDto = new JobGroupDto();

    jobGroupDto.setClientId("c5e29e5a-477b-48b4-aa26-4ffe345d95fc");
    jobGroupDto.setName("JG00");
    jobGroupDto.setCampaignId("10a3755d-73c2-4014-9e07-ca6e49dec67f");
    jobGroupDto.setStartDate(
        LocalDate.parse("04/23/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    jobGroupDto.setEndDate(
        LocalDate.parse("06/01/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));

    GroupingJobFilter groupingJobFilter =
        JobFilter.and(
            JobFilter.eq(JobFilterFields.country, "India"),
            JobFilter.greaterThanEqual(JobFilterFields.cpcBid, "5"),
            JobFilter.or(
                JobFilter.notEq(JobFilterFields.state, "Telangana"),
                JobFilter.notEq(JobFilterFields.zip, "13d12")),
            JobFilter.and(
                JobFilter.contains(JobFilterFields.city, "Hyd"),
                JobFilter.notContains(JobFilterFields.title, "software")));
    jobGroupDto.setJobFilter(groupingJobFilter);
    LocalDate ioStartDate =
        LocalDate.parse("04/23/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate ioEndDate = LocalDate.parse("04/30/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    jobGroupDto.addIoDetail("123io", 170, ioStartDate, ioEndDate);
    // jobGroupDto.settrackingUrl("http://www.tracking.com?{url}");
    jobGroupDto.setPriority(1);
    jobGroupDto.setCpcBid(1.0);
    jobGroupDto.setCpaBid(2.0);
    jobGroupDto.addPerformanceTargets("cpc", 1.0);
    jobGroupDto.addPerformanceTargets("cpa", 1.0);
    jobGroupDto.setBudgetCap(false, Freq.Monthly, 80.0, 1111.0);
    jobGroupDto.setClickCap(true, Freq.Monthly, 80.0, 1111);
    jobGroupDto.setApplyCap(true, Freq.Monthly, 80.0, 1111);
    jobGroupDto.setJobBudgetCap(false, Freq.Lifetime, 80.0, 1111);
    jobGroupDto.setJobClickCap(false, Freq.Lifetime, 80.0, 1111);
    jobGroupDto.setJobApplyCap(false, Freq.Lifetime, 80.0, 1111);

    ArrayList<Integer> daysSchedule = new ArrayList<Integer>();
    daysSchedule.add(1);
    daysSchedule.add(2);
    daysSchedule.add(3);
    daysSchedule.add(4);
    daysSchedule.add(5);
    jobGroupDto.setDaysToSchedule(daysSchedule);

    // jobGroupDto.setOverspendCap();
    // jobGroupDto.addPlacementWithBudgetAndBid("Naukri", 1.5, 100.0, Freq.Monthly, false, 80.0,
    // true);
    jobGroupDto.addPlacementWithBudgetAndBid("Naukri", 1.5, 100.0, Freq.Monthly, false, 80.0, true);
    jobGroupDto.setDefaultValues();
    String actualValue = objectMapper.writeValueAsString(jobGroupDto);

    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.STRICT);
  }

  @Test
  public void PublisherCreation_PublisherService_AssertEquals()
      throws JsonProcessingException, JSONException {
    TestSession testSession = new TestSession();
    String agencyId = testSession.getInstanceIdentifier();
    String userName = testSession.getUsername();

    String expectedValue =
        "{\"placement\":{\"name\":\"naukri\",\"bidType\":\"CPC\",\"minBid\":1.5,\"url\":\"https://www.naukri.com/\",\"country\":\"India\",\"industry\":\"InformationTechnology\",\"deliverFeedByFTP\":false,\"perClientPlacements\":false,\"publisherContactDetailsRevamp\":[{\"email\":\"naukri00@gmail.com\"},{\"email\":\"naukri01@gmail.com\"}],\"value\":\"naukri\",\"feedIndexLatency\":null,\"ftpConfig\":{},\"feedFileType\":\"newXml\",\"currency\":\"USD\",\"clickDefinitions\":{\"agencies\":{\""
            + agencyId
            + "\":{\"clients\":{},\"definition\":{\"createdApp\":\"mojo\",\"createdBy\":\""
            + userName
            + "\",\"status\":\"Approved\",\"enable\":true,\"agencyId\":\""
            + agencyId
            + "\",\"newBot\":{\"joveoBotlogic\":true,\"cmBotLogic\":true},\"bot\":true},\"enable\":false}}}},\"agencyId\":\""
            + agencyId
            + "\"}";

    PublisherDto publisherDto = new PublisherDto();
    publisherDto.setName("naukri");
    publisherDto.setPublisherUrl("https://www.naukri.com/");
    publisherDto.setBidType("CPC");
    publisherDto.setIndustry("InformationTechnology");
    publisherDto.setCountry("India");
    publisherDto.setMinBid(1.5);
    publisherDto.addPublisherContactDetails("naukri00@gmail.com");
    publisherDto.addPublisherContactDetails("naukri01@gmail.com");

    publisherDto.setDefaultValue();

    publisherDto.setAgency(agencyId, publisherDto.getAgencyIdClass());
    publisherDto.setCreatedBy(userName);
    publisherDto.setAgencyIdForClickDefinitions(agencyId);

    String actualValue = objectMapper.writeValueAsString(publisherDto);
    System.out.println(expectedValue);
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.STRICT);
  }
}

