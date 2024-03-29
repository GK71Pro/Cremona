package com.gkaraffa.cremona.theoretical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntervalPatternFactory {
  private static IntervalPatternFactory instance = null;
  private static HashMap<String, Interval> lookup = new HashMap<>();

  static {
    for (Interval currInterval : Interval.values()) {
      lookup.put(currInterval.getAbbrev(), currInterval);
    }
  }

  private IntervalPatternFactory() {}

  public static synchronized IntervalPatternFactory getInstance() {
    if (instance == null) {
      instance = new IntervalPatternFactory();
    }

    return instance;
  }

  public IntervalPattern createIntervalPattern(String name, String patternString) {
    String[] intervalArray = patternString.split(",");
    List<Interval> intervalList = new ArrayList<>();

    for (String currentIntervalString : intervalArray) {
      Interval currentIntervalUnit = this.getIntervalByString(currentIntervalString);
      intervalList.add(currentIntervalUnit);
    }

    return new IntervalPattern(name, intervalList);
  }

  private Interval getIntervalByString(String lookupString) {
    Interval lookupValue = lookup.get(lookupString);

    if (lookupValue == null) {
      throw new IllegalArgumentException("Illegal lookup string.");
    }

    return lookupValue;
  }

  public IntervalPattern createIntervalPattern(List<Interval> patternList) {
    return null;
  }

}
