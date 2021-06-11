package helpers;

import java.util.HashMap;
import java.util.Map;

public class Month {
  public Map<String, Integer> map;

  /** Method for returning map. */
  public Month() {
    map = new HashMap<>();
    map.put("Jan", 1);
    map.put("Feb", 2);
    map.put("Mar", 3);
    map.put("Apr", 4);
    map.put("May", 5);
    map.put("Jun", 6);
    map.put("Jul", 7);
    map.put("Aug", 8);
    map.put("Sep", 9);
    map.put("Oct", 10);
    map.put("Nov", 11);
    map.put("Dec", 12);
  }
}
