package com.joveo.eqrtestsdk.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.joveo.eqrtestsdk.fixtures.TestServices;
import com.joveo.eqrtestsdk.models.*;
import com.joveo.eqrtestsdk.models.TimeZone;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidationsTest {

  private static TestServices testServices;

  @BeforeAll
  public static void setUp() {
    testServices = TestServices.setup("staging");
  }

  @Nested
  class ClientCreation {
    ClientDto clientDTO;

    @BeforeEach
    public void ClientDTO_Object() {
      clientDTO = new ClientDto();
      clientDTO.setName("sdk_check_03");
      clientDTO.setAdvertiserName("Stark");
      clientDTO.setAts("Zoho");
      clientDTO.setApplyConvWindow(30);
      clientDTO.setAtsUrl("");
      clientDTO.setCountry("IN");
      clientDTO.setBudget(5000.0);
      clientDTO.setStartDate(LocalDate.now());
      clientDTO.setEndDate(LocalDate.now().plusWeeks(4));
      clientDTO.setFrequency(Frequency._1_Hours);
      clientDTO.setTimezone(TimeZone.UTC_plus_05_30);
      clientDTO.setIndustry("96");

      Set<MandatoryFields> mandatoryFields = new HashSet<>();
      mandatoryFields.add(MandatoryFields.source);
      mandatoryFields.add(MandatoryFields.title);
      mandatoryFields.add(MandatoryFields.job);
      mandatoryFields.add(MandatoryFields.description);
      mandatoryFields.add(MandatoryFields.url);
      mandatoryFields.add(MandatoryFields.referencenumber);

      clientDTO.addFeed(
          "https://joveo-samplefeed.s3.amazonaws.com/abhinay/AbSample.xml", mandatoryFields);

      ClientDto.ClientParams.Feeds feed = clientDTO.getFeeds().get(0);
      feed.schemaMappings.schemaMappingsJobCollection = "Jobs";
      feed.schemaMappings.schemaMappingsTitle = "title";
      feed.schemaMappings.schemaMappingsJob = "job";
      feed.schemaMappings.schemaMappingsDescription = "description";
      feed.schemaMappings.schemaMappingsUrl = "url";
      feed.schemaMappings.schemaMappingsRefNumber = "referencenumber";

      clientDTO.setBudget(2000.0);
    }

    @Test
    @DisplayName("Testing client creation with valid inputs")
    public void ClientCreationWithValidInputs_ClientService_ThrowsNULL() {
      assertNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing client creation with invalid client name")
    public void ClientCreationWithInValidClientName_ClientService_ThrowsNotNULL(String clientName) {
      clientDTO.setName(clientName);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing client creation with invalid ATS")
    public void ClientCreationWithInValidATS_ClientService_ThrowsNotNULL(String ATS) {
      clientDTO.setAts(ATS);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing client creation with invalid frequency")
    public void ClientCreationWithInValidFrequency_ClientService_ThrowsNotNULL(Frequency freq) {
      clientDTO.setFrequency(freq);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing client creation with invalid timezone")
    public void ClientCreationWithInValidTimeZone_ClientService_ThrowsNotNULL(TimeZone timeZone) {
      clientDTO.setTimezone(timeZone);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {1, 31, 100, -1, 0, -231})
    @DisplayName("Testing client creation with invalid apply conversion window")
    public void ClientCreationWithInValidApplyConversionWindow_ClientService_ThrowsNotNULL(
        Integer applyConversionWindow) {
      clientDTO.setApplyConvWindow(applyConversionWindow);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing client creation with invalid feed url")
    public void ClientCreationWithInValidFeedURL_ClientService_ThrowsNotNULL(String feedURL) {
      clientDTO.addFeed(feedURL);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(doubles = {-12, -234})
    @DisplayName("Testing client creation with invalid budget")
    public void ClientCreationWithInValidBudget_ClientService_ThrowsNotNULL(Double budget) {
      clientDTO.setBudget(budget);
      assertNotNull(
          testServices.clientService.validateEntity(
              clientDTO, testServices.clientService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"04/28/2020", "02/28/3030"})
    @DisplayName("Testing client creation with invalid start date")
    public void ClientCreationWithInvalidStartDates_ClientService_ThrowsNotNULL(String input) {
      LocalDate startDate =
          (input == null)
              ? null
              : LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      clientDTO.setStartDate(startDate);
      assertNotNull(
          testServices.campaignService.validateEntity(
              clientDTO, testServices.campaignService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"04/28/2020"})
    @DisplayName("Testing client creation with invalid end date")
    public void ClientCreationWithInvalidEndDates_ClientService_ThrowsNotNULL(String input) {
      LocalDate endDate =
          (input == null)
              ? null
              : LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      clientDTO.setEndDate(endDate);
      assertNotNull(
          testServices.campaignService.validateEntity(
              clientDTO, testServices.campaignService.validator));
    }

    @Nested
    class clientCreationEmptyFeed {
      @Test
      @DisplayName("Creating client with empty list of feeds")
      public void ClientCreationEmptyListOfFeed_ClientService_ThrowsNotNull() {
        ClientDto clientDTO = new ClientDto();
        clientDTO.setName("sdk_check_03");
        clientDTO.setAts("Zoho");
        clientDTO.setApplyConvWindow(30);
        clientDTO.setBudget(5000.0);
        clientDTO.setStartDate(LocalDate.now());
        clientDTO.setEndDate(LocalDate.now().plusWeeks(4));
        clientDTO.setFrequency(Frequency._1_Hours);
        clientDTO.setTimezone(TimeZone.UTC_plus_05_30);
        clientDTO.setBudget(2000.0);
        System.out.println(
            testServices.clientService.validateEntity(
                clientDTO, testServices.clientService.validator));
        assertNotNull(
            testServices.clientService.validateEntity(
                clientDTO, testServices.clientService.validator));
      }
    }
  }

  @Nested
  class CampaignCreation {
    CampaignDto campaignDTO;

    @BeforeEach
    public void campaignDTO_Object() {
      campaignDTO = new CampaignDto();
      campaignDTO.setClientId("d12d23x2a");
      campaignDTO.setName("camp00");
      campaignDTO.setStartDate(LocalDate.now());
      campaignDTO.setEndDate(LocalDate.now().plusWeeks(4));
    }

    @Test
    @DisplayName("Testing campaign creation for valid inputs")
    public void CampaignCreationWithValidInputs_CampaignService_ThrowsNULL() {
      assertNull(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing campaign creation with invalid client id")
    public void CampaignCreationWithInvalidClientID_CampaignService_ThrowsNotNULL(String clientID) {
      campaignDTO.setClientId(clientID);
      System.out.println(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
      assertNotNull(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing campaign creation with invalid campaign name")
    public void CampaignCreationWithInvalidCampaignName_CampaignService_ThrowsNotNULL(
        String campaignName) {
      campaignDTO.setName(campaignName);
      System.out.println(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
      assertNotNull(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"04/28/1998", "02/28/3030"})
    @DisplayName("Testing campaign creation with invalid start date")
    public void CampaignCreationWithInvalidStartDates_CampaignService_ThrowsNotNULL(String input) {
      LocalDate startDate =
          (input == null)
              ? null
              : LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      campaignDTO.setStartDate(startDate);
      System.out.println(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
      assertNotNull(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"04/28/1998"})
    @DisplayName("Testing campaign creation with invalid end date")
    public void CampaignCreationWithInvalidEndDates_CampaignService_ThrowsNotNULL(String input) {
      LocalDate endDate =
          (input == null)
              ? null
              : LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      campaignDTO.setEndDate(endDate);
      System.out.println(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
      assertNotNull(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
    }

    @ParameterizedTest
    // @CsvSource({"-1, 10", "-17, -17", "1000, 103", "1000, -17"})
    @ValueSource(doubles = {-1, -17})
    @DisplayName("Testing campaign creation with invalid end date")
    public void CampaignCreationWithInvalidBudgetCap_CampaignService_ThrowsNotNULL(Double budget) {
      campaignDTO.setBudget(budget);
      System.out.println(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
      assertNotNull(
          testServices.campaignService.validateEntity(
              campaignDTO, testServices.campaignService.validator));
    }
  }

  @Nested
  class JobGroupCreation {
    JobGroupDto jobGroupDTO;

    @BeforeEach
    public void JobGroupDTO_Object() {
      jobGroupDTO = new JobGroupDto();

      jobGroupDTO.setName("JG00");
      jobGroupDTO.setClientId("2d3zx3r1");
      jobGroupDTO.setCampaignId("s15f3d13e");
      jobGroupDTO.setStartDate(LocalDate.now());
      jobGroupDTO.setEndDate(LocalDate.now().plusWeeks(4));

      GroupingJobFilter groupingJobFilter =
          JobFilter.and(
              JobFilter.eq(JobFilterFields.state, "Telangana"),
              JobFilter.notEq(JobFilterFields.category, "Recruitment"),
              JobFilter.or(JobFilter.eq(JobFilterFields.city, "Hyderabad")));

      jobGroupDTO.setJobFilter(groupingJobFilter);

      jobGroupDTO.addIoDetail("123x", 200, LocalDate.now(), LocalDate.now().plusWeeks(4));

      jobGroupDTO.setPriority(1);

      jobGroupDTO.setCpaBid(2.0);
      jobGroupDTO.setCpcBid(1.5);

      jobGroupDTO.setBudgetCap(200.0);
      jobGroupDTO.setApplyCap(true, Freq.Monthly, 80.0, 200);
      jobGroupDTO.setClickCap(true, Freq.Monthly, 80.0, 200);

      jobGroupDTO.setJobBudgetCap(true, Freq.Monthly, 60.0, 150);
      jobGroupDTO.setJobApplyCap(true, Freq.Monthly, 70.0, 150);
      jobGroupDTO.setJobClickCap(true, Freq.Monthly, 50.0, 200);

      jobGroupDTO.setDaysToSchedule(Arrays.asList(0, 1, 2, 3, 4, 6));

      jobGroupDTO.addPlacement("pg");
    }

    @Test
    public void JobGroupCreationValidInputs_JobGroupService_ThrowsNULL() {
      assertNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing for JobGroup Creation with invalid jobgroup names")
    public void JobGroupCreationInvalidName_JobGroupService_ThrowsNotNULL(String jobGroupName) {
      jobGroupDTO.setName(jobGroupName);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing job group creation with invalid campaignId")
    public void JobGroupCreationInvalidCampaignId_JobGroupService_ThrowsNotNULL(String campaignId) {
      jobGroupDTO.setCampaignId(campaignId);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"04/28/1998", "02/28/3030"})
    @DisplayName("Testing job group creation with invalid start date")
    public void JobGroupCreationInvalidStartDates_JobGroupService_ThrowsNotNULL(String input) {
      LocalDate startDate =
          (input == null)
              ? null
              : LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      System.out.println(startDate);
      System.out.println(jobGroupDTO.getEndDate());
      jobGroupDTO.setStartDate(startDate);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"04/28/1998"})
    @DisplayName("Testing job group creation with invalid end date")
    public void JobGroupCreationInvalidEndDates_JobGroupService_ThrowsNotNULL(String input) {
      LocalDate endDate =
          (input == null)
              ? null
              : LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      jobGroupDTO.setEndDate(endDate);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, -30, 11})
    @DisplayName("Testing Job group creation with invalid priorities")
    public void JobGroupCreationInvalidPriority_JobGroupService_ThrowsNotNULL(Integer priority) {
      jobGroupDTO.setPriority(priority);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({"-10.0, 10.0", "10.0, -10.0", "-10.0, -10.0"})
    @DisplayName("testing job group creation with invalid CPA and CPC bids")
    public void JobGroupCreationInvalidCPAandCPCBid_JobGroupService_ThrowsNotNULL(
        Double cpaBid, Double cpcBid) {
      jobGroupDTO.setCpaBid(cpaBid);
      jobGroupDTO.setCpcBid(cpcBid);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing job group creation with invalid clientIds")
    public void JobGroupCreationInvalidClientId_JobGroupService_ThrowsNotNULL(String clientId) {
      jobGroupDTO.setClientId(clientId);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({
      "true, Daily, 101, -1.0",
      "true, Weekly, 117, -20.0",
      "true, Lifetime, 111, -10.0",
      "false, Lifetime, -102, -17.0"
    })
    @DisplayName("Testing job group creation with invalid budget options")
    public void JobGroupCreationInvalidBudgetCap_JobGroupService_ThrowsNotNULL(
        Boolean pacing, String frequency, Double threshold, Double value) {
      jobGroupDTO.setBudgetCap(pacing, Freq.valueOf(frequency), threshold, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullSource
    public void JobGroupCreationInvalidBudgetCapvalue_JobGroupService_ThrowsNotNULL(Double value) {
      jobGroupDTO.setBudgetCap(false, Freq.Weekly, 80.0, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({
      "true, Daily, 101, -1",
      "true, Weekly, 117, -20",
      "true, Lifetime, 111, -13",
      "false, Lifetime, -102, -17",
      "false, Daily, 30, 10"
    })
    @DisplayName("testing job group creation with invalid ClickCap options")
    public void JobGroupCreationInvalidClicksCap_JobGroupService_ThrowsNotNULL(
        Boolean pacing, String frequency, Double threshold, int value) {
      jobGroupDTO.setClickCap(pacing, Freq.valueOf(frequency), threshold, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({
      "true, Daily, 101, -1",
      "true, Weekly, 117, -20",
      "true, Lifetime, 111, -10",
      "false, Lifetime, -102, -17",
      "false, Daily, 30, 10"
    })
    @DisplayName("Testing job group creation with invalid Apply Cap options")
    public void JobGroupCreationInvalidAppliesCap_JobGroupService_ThrowsNotNULL(
        Boolean pacing, String frequency, Double threshold, int value) {
      jobGroupDTO.setApplyCap(pacing, Freq.valueOf(frequency), threshold, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({
      "true, Daily, -10, -10",
      "true, Weekly, -10, -10",
      "true, Lifetime, 110, -10",
      "true, Monthly, 110, -10",
      "false, Daily, 110, -10"
    })
    @DisplayName("Testing job group creation with Invalid JobBudgetCap options")
    public void JobGroupCreationInvalidJobBudgetCap_JobGroupService_ThrowsNotNULL(
        Boolean pacing, String frequency, Double threshold, int value) {
      jobGroupDTO.setJobBudgetCap(pacing, Freq.valueOf(frequency), threshold, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({
      "true, Daily, -10, -10",
      "true, Weekly, -10, -10",
      "true, Lifetime, 110, -10",
      "true, Monthly, 110, -10",
      "false, Daily, 110, -10"
    })
    @DisplayName("Testing job group creation with Invalid JobClicksCap options")
    public void JobGroupCreationInvalidJobclicksCap_JobGroupService_ThrowsNotNULL(
        Boolean pacing, String frequency, Double threshold, int value) {
      jobGroupDTO.setJobClickCap(pacing, Freq.valueOf(frequency), threshold, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({
      "true, Daily, -10, -10",
      "true, Weekly, -10, -10",
      "true, Lifetime, 110, -10",
      "true, Monthly, 110, -10",
      "false, Daily, 110, -10"
    })
    @DisplayName("Testing job group creation with Invalid JobAppliesCap options")
    public void JobGroupCreationInvalidJobAppliesCap_JobGroupService_ThrowsNotNULL(
        Boolean pacing, String frequency, Double threshold, int value) {
      jobGroupDTO.setJobApplyCap(pacing, Freq.valueOf(frequency), threshold, value);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing jon group creation with Group Filter as NULL.")
    public void JobGroupCreationNULLGroupFilter_JobGroupService_ThrowsNotNUL(
        GroupingJobFilter groupingJobFilter) {
      jobGroupDTO.setJobFilter(groupingJobFilter);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @Test
    @DisplayName("Testing job group creation with depth of filers > 3")
    public void JobGroupCreationInvalidNestedFilters_JobGroupService_Assertions() {
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(
              JobFilter.and(JobFilter.or(JobFilter.and(JobFilter.eq(JobFilterFields.city, "AP")))));
      System.out.println(jobGroupDTO.isValidJobFilter(groupingJobFilter, 1));
      assertFalse(jobGroupDTO.isValidJobFilter(groupingJobFilter, 1));
    }

    @Test
    @DisplayName("Testing job group creation with depth of filers < 3")
    public void JobGroupCreationValidNestedFilters_JobGroupService_Assertions() {
      GroupingJobFilter groupingJobFilter = JobFilter.and();
      assertTrue(jobGroupDTO.isValidJobFilter(groupingJobFilter, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"04/28/2020", "02/29/2020"})
    public void JobGroupCreationValidDateFormats_JobGroupService_ThrowsNULL(String date) {
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(JobFilter.on(JobFilterFields.postedDate, date));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.before(JobFilterFields.postedDate, date));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.after(JobFilterFields.postedDate, date));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      List<String> dates = new ArrayList<>();
      dates.add(date);
      dates.add(date);
      groupingJobFilter = JobFilter.and(JobFilter.between(JobFilterFields.postedDate, dates));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @ValueSource(
        strings = {"-1", "0,23", ".123", "fourdays", "04/28/199", "02/29/2021", "01/01/101"})
    public void
        JobGroupCreationInvalidFormatForDatePostedMoreThanLessThan_JobGroupService_ThrowsNULL(
            String days) {
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(JobFilter.greaterThan(JobFilterFields.postedDate, days));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.lessThan(JobFilterFields.postedDate, days));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {"28April2020", "12/32/2020", "02/29/2021", "04/31/2021"})
    public void JobGroupCreationInvalidDateFormats_JobGroupService_ThrowsNULL(String date) {
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(JobFilter.on(JobFilterFields.postedDate, date));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.before(JobFilterFields.postedDate, date));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.after(JobFilterFields.postedDate, date));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      List<String> dates = new ArrayList<>();
      dates.add(date);
      groupingJobFilter = JobFilter.and(JobFilter.between(JobFilterFields.postedDate, dates));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      dates.add(date);
      groupingJobFilter = JobFilter.and(JobFilter.between(JobFilterFields.postedDate, dates));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @CsvSource({"04/28/2019, 03/20/1998", "01/01/7070, 01/01/8080", "04/01/1990, 03/01/1990"})
    public void JobGroupJobFilterBetweenInvalidDates__JobGroupService_ThrowsNotNull(
        String startDate, String endDate) {
      List<String> dates = new ArrayList<>();
      dates.add(startDate);
      dates.add(endDate);
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(JobFilter.between(JobFilterFields.postedDate, dates));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0", "132.98", "23"})
    public void JobGroupValidCPCBid_JobGroupService_ThrowsNotNull(String cpcBid) {
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(JobFilter.greaterThan(JobFilterFields.cpcBid, cpcBid));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {"onehundred", "1.24k", "-12.0", "1.2.3", ".23"})
    public void JobGroupCreationInvalidCPCBid_JobGroupService_ThrowsNotNULL(String cpcBid) {
      GroupingJobFilter groupingJobFilter =
          JobFilter.and(JobFilter.greaterThan(JobFilterFields.cpcBid, cpcBid));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.lessThan(JobFilterFields.cpcBid, cpcBid));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.eq(JobFilterFields.cpcBid, cpcBid));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      List<String> bids = new ArrayList<>();
      bids.add(cpcBid);
      bids.add(cpcBid);
      groupingJobFilter = JobFilter.and(JobFilter.between(JobFilterFields.cpcBid, bids));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.greaterThanEqual(JobFilterFields.cpcBid, cpcBid));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));

      groupingJobFilter = JobFilter.and(JobFilter.lessThanEqual(JobFilterFields.cpcBid, cpcBid));
      jobGroupDTO.setJobFilter(groupingJobFilter);
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing jon group creation with invalid placement input.")
    public void JobGroupCreationInvalidPlacement_JobGroupService_ThrowsNotNULL(String publisher) {
      jobGroupDTO.addPlacement(publisher);
      System.out.println(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
      assertNotNull(
          testServices.jobGroupService.validateEntity(
              jobGroupDTO, testServices.jobGroupService.validator));
    }

    @Nested
    class JobGroupCreationEmptyPlacementList {
      @Test
      @DisplayName("Testing Job group creation with empty list of placements")
      public void JobGroupCreationEmptyListOfPlacements_JobGroupService_ThrowsNotNull() {
        JobGroupDto jobGroupDTO = new JobGroupDto();

        jobGroupDTO.setName("JG00");
        jobGroupDTO.setClientId("2d3zx3r1");
        jobGroupDTO.setCampaignId("s15f3d13e");
        jobGroupDTO.setStartDate(LocalDate.now());
        jobGroupDTO.setEndDate(LocalDate.now().plusWeeks(4));

        GroupingJobFilter groupingJobFilter =
            JobFilter.and(
                JobFilter.eq(JobFilterFields.state, "Telangana"),
                JobFilter.notEq(JobFilterFields.category, "Recruitment"),
                JobFilter.or(JobFilter.eq(JobFilterFields.city, "Hyderabad")));

        jobGroupDTO.setJobFilter(groupingJobFilter);

        jobGroupDTO.addIoDetail("123x", 200, LocalDate.now(), LocalDate.now().plusWeeks(4));

        jobGroupDTO.setBudgetCap(200.0);

        System.out.println(
            testServices.jobGroupService.validateEntity(
                jobGroupDTO, testServices.jobGroupService.validator));
        assertNotNull(
            testServices.jobGroupService.validateEntity(
                jobGroupDTO, testServices.jobGroupService.validator));
      }
    }
  }

  @Nested
  class PublisherCreation {
    PublisherDto publisherDTO;

    @BeforeEach
    public void publisherDTO_Object() {
      publisherDTO = new PublisherDto();
      publisherDTO.setName("pb00");
      publisherDTO.setBidType("CPC");
      publisherDTO.setPublisherUrl("www.url.com");

      publisherDTO.setDefaultValue();
      publisherDTO.setAgency("", publisherDTO.getAgencyIdClass());
      publisherDTO.setCreatedBy("");
      publisherDTO.setAgencyIdForClickDefinitions("agencyId");
    }

    @Test
    public void PublisherCreationValidInputs_PublisherService_ThrowsNULL() {
      System.out.println(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
      assertNull(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing publisher creation with invalid publisher names")
    public void PublisherCreationPublisherName_PublisherService_ThrowsNULL(String name) {
      publisherDTO.setName(name);
      System.out.println(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
      assertNotNull(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Testing publisher creation with invalid publisher url")
    public void PublisherCreationPublisherURL_PublisherService_ThrowsNULL(String url) {
      publisherDTO.setPublisherUrl(url);
      System.out.println(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
      assertNotNull(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, -20})
    @DisplayName("Testing publisher creation with invalid minBids")
    public void PublisherCreationInvalidMinBids_PublisherService_ThrowsNULL(Double minBid) {
      publisherDTO.setMinBid(minBid);
      System.out.println(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
      assertNotNull(
          testServices.publisherService.validateEntity(
              publisherDTO, testServices.publisherService.validator));
    }
  }
}
