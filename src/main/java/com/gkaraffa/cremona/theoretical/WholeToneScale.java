package com.gkaraffa.cremona.theoretical;

public class WholeToneScale extends SymmetricScale {

  public WholeToneScale(String name, Tone[] tones, ScaleQuality scaleQuality,
      IntervalPattern intervalPattern) {
    super(name, tones, scaleQuality, intervalPattern);
  }

  private int calculateLocation(int segment, int offset) {
    int location = segment + offset;
    int limit = IntervalNumber.NINTH.getPosition();

    if (location > limit) {
      location -= limit;
    }

    return location;
  }

  public Tone getToneAtRelativeIntervalNumber(IntervalNumber rootInterval,
      IntervalNumber offsetInterval) {
    return getTone(calculateLocation(rootInterval.getPosition(), offsetInterval.getPosition()));
  }
}
