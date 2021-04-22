package com.joveo.eqrtestsdk.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joveo.eqrtestsdk.fixtures.TestServices;
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
  public void testJson() {
    //        JSONAssert.assertEquals();
  }

  @Test
  public void JobGroupFiltersCreation_JobGroupService_Assertions()
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
  public void ClientCreation_ClientService_AssertEquals()
      throws JsonProcessingException, JSONException {
    String expectedValue =
        "{\"params\":{\"name\":\"Stark\",\"country\":\"IN\",\"exportedName\":\"stark\",\"advertiserName\":\"stark family\",\"ats\":\"Zoho\",\"atsUrl\":\"www.zoho.com\",\"frequency\":\"6 hours\",\"applyConvWindow\":20,\"timezone\":\"c1d3\",\"type\":\"DirectEmployer\",\"industry\":\"47\",\"excludedPublishers\":\"\",\"endDate\":\"04/28/2021\",\"startDate\":\"04/19/2021\",\"markdown\":\"\",\"feeds\":[{\"xmlFeedUrl\":\"https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml\",\"id\":null,\"schemaMappings\":{\"schemaMappingsJobCollection\":\"Jobs\",\"schemaMappingsJob\":\"job\",\"schemaMappingsCompany\":null,\"schemaMappingsTitle\":\"title\",\"schemaMappingsCity\":\"city\",\"schemaMappingsState\":\"state\",\"schemaMappingsCountry\":\"country\",\"schemaMappingsDescription\":\"description\",\"schemaMappingsURL\":\"url\",\"schemaMappingsZip\":null,\"schemaMappingsCategory\":\"category\",\"schemaMappingsDatePosted\":\"date\",\"schemaMappingsRefNumber\":\"referencenumber\",\"schemaMappingsModifiedDate\":null,\"schemaMappingsPublishedDate\":null,\"schemaMappingsCPCBid\":\"cpc\",\"schemaMappingsType\":null,\"schemaMappingAdditional\":{},\"schemaMappingPublisher\":{}},\"mandatoryFields\":[\"source\",\"job\",\"title\",\"description\",\"url\",\"referencenumber\"]}],\"sjCreate\":false,\"budgetCap\":{\"value\":10000,\"pacing\":false,\"freq\":\"Monthly\"},\"industries\":[\"47\"],\"globallyExcludedPublishers\":\"\"}}";
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
    clientDTO.setIndustry("Accounting");
    clientDTO.setBudget(10000.0);
    clientDTO.setStartDate(
        LocalDate.parse("04/19/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    clientDTO.setEndDate(LocalDate.parse("04/28/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    clientDTO.setCountry("IN");
    clientDTO.setDefaultValues();
    String actualValue = objectMapper.writeValueAsString(clientDTO);
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.LENIENT);
  }

  @Test
  public void CampaignCreation_CampaignService_AssertEquals()
      throws JsonProcessingException, JSONException {
    String expectedValue =
        "{\"params\":{\"clientId\":\"fae42273-9f06-47e4-89e2-a0a74411f1b9\",\"clientIds\":[\"fae42273-9f06-47e4-89e2-a0a74411f1b9\"],\"name\":\"Camp00\",\"startDate\":\"04/20/2021\",\"endDate\":\"04/28/2021\",\"budgetCap\":{\"value\":17000,\"pacing\":false,\"freq\":\"Monthly\"}}}";
    CampaignDto campaignDTO = new CampaignDto();
    campaignDTO.setClientId("fae42273-9f06-47e4-89e2-a0a74411f1b9");
    campaignDTO.setName("Camp00");
    campaignDTO.setBudget(17000.0);
    campaignDTO.setStartDate(
        LocalDate.parse("04/20/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    campaignDTO.setEndDate(
        LocalDate.parse("04/28/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
    String actualValue = objectMapper.writeValueAsString(campaignDTO);
    JSONAssert.assertEquals(expectedValue, actualValue, JSONCompareMode.STRICT);
  }

  @Test
  public void JobGroupCreation_JobGroupService_AssertEquals() {}

  @Test
  public void PublisherCreation_PublisherService_AssertEquals() {}
}
