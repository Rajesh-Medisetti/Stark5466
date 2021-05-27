package dataproviders;

import com.joveo.eqrtestsdk.core.entities.Client;
import com.joveo.eqrtestsdk.models.CampaignDto;
import com.joveo.eqrtestsdk.models.ClientDto;
import com.joveo.eqrtestsdk.models.JobFilterFields;
import com.joveo.eqrtestsdk.models.JobGroupDto;
import dtos.Dtos;
import dtos.JobFilterData;
import entitycreators.CampaignEntityCreator;
import entitycreators.ClientEntityCreator;
import entitycreators.JobGroupCreator;
import enums.BidLevel;
import enums.Entity;
import helpers.Utils;
import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateDP {

    public static JobFilterData data1to1;
    public static Set<Client> clientSet = new HashSet<>();

    /**
     * the actual data provider for the test.
     *
     * @return 2d array
     */
    @DataProvider(name = "test", parallel = false)
    public static Object[][] dpMethod() {
        List<List<Object>> dpList = data1to1.getDpList();
        Object[][] array = new Object[dpList.size()][dpList.get(0).size()];
        int counter = 0;
        for (List<Object> list : dpList) {
            array[counter] = list.toArray();
            counter++;
        }
        return array;
    }

    /**
     * create combination of dates for invalid start and end date.
     *
     * @return dto list
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public List<Dtos> getDateCombination() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = LocalDate.now().minusDays(2);
        LocalDate futureDate = LocalDate.now().plusDays(2);
        List<Dtos> dtoList = new ArrayList<>();

        for (Entity entity : Entity.values()) {
            ClientDto clientDto = ClientEntityCreator.dateClientCreator(currentDate, futureDate);
            CampaignDto campaignDto = CampaignEntityCreator.dateCampaignCreator(currentDate, futureDate);
            JobGroupDto jobGroupDto = JobGroupCreator.dtoWithDate(JobFilterFields.country, "India", currentDate, futureDate, 1);
            dtoList.add(getInvalidDate(entity, clientDto, campaignDto, jobGroupDto, futureDate));
            dtoList.add(getInvalidDate(entity, clientDto, campaignDto, jobGroupDto, previousDate));
        }

        return dtoList;
    }

    private Dtos getInvalidDate(
            Entity entity,
            ClientDto clientDto,
            CampaignDto campaignDto,
            JobGroupDto jobGroupDto,
            LocalDate date) {
        switch (entity) {
            case Client:
                clientDto = ClientEntityCreator.dateClientCreator(date, date);
                break;
            case Campaign:
                campaignDto = CampaignEntityCreator.dateCampaignCreator(date, date);
                break;
            case JobGroup:
<<<<<<<HEAD
                    jobGroupDto = JobGroupCreator.dtoWithDate(JobFilterFields.country, "India", date, date, 1);
=======
                jobGroupDto = JobGroupCreator.dtoWithDate(JobFilterFields.country, "India", date, date, 1);
>>>>>>>5725 b51a9562721e9f2b43fccc5ebd74add7259f
                break;
            default:
                break;
        }
        return new Dtos(
                clientDto, campaignDto, jobGroupDto, BidLevel.PLACEMENT, Utils.getRandomNumber(2, 5));
    }
}
