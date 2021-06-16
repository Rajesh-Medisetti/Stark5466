package dataproviders;


import enums.Language;
import enums.ui.ClientTabs;
import java.time.ZoneId;
import org.testng.annotations.DataProvider;

/** class for calendar data provider. */
public class LanguageDp {

  /** Data provider for AllClients. */
  @DataProvider(name = "clientsLevelDataProvider", parallel = true)
  public static Object[][] allClientsProvider() {
    return new Object[][] {
        {"Ned", ClientTabs.CAMPAIGN, "Campaigns", Language.English},
        {"Ned", ClientTabs.CAMPAIGN, "Kampagnen", Language.German},
        {"Ned", ClientTabs.CAMPAIGN, "Campagnes", Language.French},

        {"Ned", ClientTabs.JOBGROUPS, "Job Groups", Language.English},
        {"Ned", ClientTabs.JOBGROUPS, "Job-Gruppen", Language.German},
        {"Ned", ClientTabs.JOBGROUPS, "Groupes d'emplois", Language.French},

        {"Ned", ClientTabs.JOBS, "Jobs", Language.English},
        {"Ned", ClientTabs.JOBS, "Jobs", Language.German},
        {"Ned", ClientTabs.JOBS, "Emplois", Language.French},

        {"Ned", ClientTabs.PLACEMENTS, "Placements", Language.English},
        {"Ned", ClientTabs.PLACEMENTS, "Platzierungen", Language.German},
        {"Ned", ClientTabs.PLACEMENTS, "Emplacements", Language.French},

        {"Ned", ClientTabs.AUTOMATION, "Automation", Language.English},
        {"Ned", ClientTabs.AUTOMATION, "Automatisierung", Language.German},
        {"Ned", ClientTabs.AUTOMATION, "Automatisation", Language.French},

        {"Ned", ClientTabs.POSTINGS, "Postings", Language.English},
        {"Ned", ClientTabs.POSTINGS, "Laufzeitinserate", Language.German},
        {"Ned", ClientTabs.POSTINGS, "Annonce durée fixe", Language.French},

        {"Ned", ClientTabs.SLOTS, "Slots", Language.English},
        {"Ned", ClientTabs.SLOTS, "Slots", Language.German},
        {"Ned", ClientTabs.SLOTS, "Slots", Language.French},

        {"Ned", ClientTabs.GOOGLEADS, "Google Ads", Language.English},
        {"Ned", ClientTabs.GOOGLEADS, "Google Ads", Language.German},
        {"Ned", ClientTabs.GOOGLEADS, "Annonces Google", Language.French},

        {"Ned", ClientTabs.FACEBOOK, "Facebook", Language.English},
        {"Ned", ClientTabs.FACEBOOK, "Facebook", Language.German},
        {"Ned", ClientTabs.FACEBOOK, "Facebook", Language.French}
    };
  }

  //
  /** Data provider for calendar. */
  @DataProvider(name = "clientDataProvider", parallel = true)
  public static Object[][] clientDataProvider() {
    return new Object[][] {
        {"All Clients", Language.English},
        {"Alle Kunden", Language.German},
        {"Tous les clients", Language.French}
    };
  }

  /** Data provider for calendar. */
  @DataProvider(name = "publisherDataProvider", parallel = true)
  public static Object[][] publisherDataProvider() {
    return new Object[][] {
        {"All Publishers", Language.English},
        {"Alle Herausgeber", Language.German},
        {"Tous les éditeurs", Language.French}
    };
  }
}

