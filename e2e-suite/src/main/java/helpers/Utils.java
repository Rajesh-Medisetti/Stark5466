package helpers;

import java.util.Random;

public class Utils {

  /** full list of characters to choose from. */
  private static final String CHAR_LIST =
      "abcdefghijklmnopqrstuvwxyzABCDE" + "FGHIJKLMNOPQRSTUVWXYZ1234567890";

  /** the length of random int. */
  private static final int RANDOM_STRING_LENGTH = 10;

  /** * This method generates random string.   @return a random string */
  public static String generateRandomString() {
    StringBuffer randStr = new StringBuffer();
    for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
      int number = getRandomNumber(CHAR_LIST.length());
      char ch = CHAR_LIST.charAt(number);
      randStr.append(ch);
    }
    return randStr.toString();
  }

  /** . returning a random string of length size. */
  public static String getRandomString(int size) {
    char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    StringBuilder str = new StringBuilder(size);
    Random random = new Random();
    for (int i = 0; i < size; i++) {
      char c = chars[random.nextInt(chars.length)];
      str.append(c);
    }
    return str.toString();
  }

  /**
   * gives a random number.
   *
   * @param length of the number needed
   * @return an integer
   */
  public static int getRandomNumber(int length) {
    int randomInt = 0;
    Random randomGenerator = new Random();
    randomInt = randomGenerator.nextInt(length);
    if (randomInt - 1 == -1) {
      return randomInt;
    } else {
      return (randomInt - 1);
    }
  }

  public static int getRandomNumber(int minimum, int maximum) {
    Random random = new Random();
    return minimum + random.nextInt(maximum - minimum + 1);
  }
}
