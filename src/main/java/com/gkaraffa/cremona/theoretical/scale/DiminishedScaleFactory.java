package com.gkaraffa.cremona.theoretical.scale;

import com.gkaraffa.cremona.theoretical.Interval;
import com.gkaraffa.cremona.theoretical.IntervalNumber;
import com.gkaraffa.cremona.theoretical.IntervalPattern;
import com.gkaraffa.cremona.theoretical.Tone;
import com.gkaraffa.cremona.theoretical.ToneCollection;

public class DiminishedScaleFactory extends ScaleFactory {

  public DiminishedScaleFactory() {
    super();
  }

  @Override
  protected Scale getScale(Tone key, IntervalPattern intervalPattern, ToneCollection toneCollection,
      ScaleQuality scaleQuality) {
    return new DiminishedScale(key.getText() + " " + intervalPattern.getText(), toneCollection,
        scaleQuality, intervalPattern);
  }

  @Override
  protected ScaleQuality evaluateScaleQuality(IntervalPattern intervalPattern) {
    return ScaleQuality.DIMINISHED;
  }

  @Override
  protected boolean validateInputPattern(IntervalPattern intervalPattern) {
    Interval thirdInterval;
    Interval fifthInterval;

    if (((thirdInterval =
        intervalPattern.getIntervalByIntervalNumber(IntervalNumber.THIRD)) == null)
        || (thirdInterval != Interval.MINOR_THIRD)) {
      return false;
    }

    if (((fifthInterval =
        intervalPattern.getIntervalByIntervalNumber(IntervalNumber.FIFTH)) == null)
        || (fifthInterval != Interval.DIMINISHED_FIFTH)) {
      return false;
    }
    
    return true;
  }
}
