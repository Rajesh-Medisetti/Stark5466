package dtos;

public class SponsoredStatsInfo {
  private int clicks;
  private int botClicks;
  private int applyStarts;
  private int applyFinishes;

  /** Constructor for SponsoredStatsInfo. */
  public SponsoredStatsInfo() {
    this.clicks = 0;
    this.botClicks = 0;
    this.applyStarts = 0;
    this.applyFinishes = 0;
  }

  /**
   * Constructor for SponsoredStatsInfo.
   *
   * @param clicks Clicks
   * @param botClicks Bot Clicks
   * @param applyStarts Apply Starts
   * @param applyFinishes Apply Finishes
   */
  public SponsoredStatsInfo(int clicks, int botClicks, int applyStarts, int applyFinishes) {
    this.clicks = clicks;
    this.botClicks = botClicks;
    this.applyStarts = applyStarts;
    this.applyFinishes = applyFinishes;
  }

  public int getClicks() {
    return clicks;
  }

  public void addClicks(int clicksCount) {
    this.clicks = this.clicks + clicksCount;
  }

  public int getBotClicks() {
    return botClicks;
  }

  public void addBotClicks(int botClicksCount) {
    this.botClicks = this.botClicks + botClicksCount;
  }

  public int getApplyStarts() {
    return applyStarts;
  }

  public void addApplyStarts(int applyStartsCount) {
    this.applyStarts = this.applyStarts + applyStartsCount;
  }

  public int getApplyFinishes() {
    return applyFinishes;
  }

  public void addApplyFinishes(int applyFinishesCount) {
    this.applyFinishes = this.applyFinishes + applyFinishesCount;
  }
}
