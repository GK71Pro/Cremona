package com.gkaraffa.cremona.theoretical.chord;

import com.gkaraffa.cremona.theoretical.IntervalNumber;
import com.gkaraffa.cremona.theoretical.IntervalPattern;
import com.gkaraffa.cremona.theoretical.IntervalPatternFactory;
import com.gkaraffa.cremona.theoretical.TonalSpectrum;
import com.gkaraffa.cremona.theoretical.Tone;
import com.gkaraffa.cremona.theoretical.ToneCollection;
import com.gkaraffa.cremona.theoretical.ToneCollectionBuilder;

public class ChordFactory {

  static {
    IntervalPatternFactory iPF = IntervalPatternFactory.getInstance();

    MAJOR_CHORD_PATTERN = iPF.createIntervalPattern("Major", "P1,M3,P5");
    MINOR_CHORD_PATTERN = iPF.createIntervalPattern("Minor", "P1,m3,P5");
    DIMINISHED_CHORD_PATTERN = iPF.createIntervalPattern("Diminished", "P1,m3,d5");
    AUGMENTED_CHORD_PATTERN = iPF.createIntervalPattern("Augmented", "P1,M3,A5");
    MAJOR_SEVENTH_CHORD_PATTERN = iPF.createIntervalPattern("Major Seventh", "P1,M3,P5,M7");
    DOMINANT_SEVENTH_CHORD_PATTERN = iPF.createIntervalPattern("Dominant Seventh", "P1,M3,P5,m7");
    MINOR_SEVENTH_CHORD_PATTERN = iPF.createIntervalPattern("Minor Seventh", "P1,m3,P5,m7");
    MINORMAJOR_SEVENTH_CHORD_PATTERN =
        iPF.createIntervalPattern("Minor-Major Seventh", "P1,m3,P5,M7");
    HALF_DIMINISHED_SEVENTH_CHORD_PATTERN =
        iPF.createIntervalPattern("Half-Diminished Seventh", "P1,m3,d5,m7");
    DIMINISHED_SEVENTH_CHORD_PATTERN =
        iPF.createIntervalPattern("Diminished Seventh", "P1,m3,d5,d7");
    AUGMENTED_MAJOR_SEVENTH_CHORD_PATTERN =
        iPF.createIntervalPattern("Augmented-Major Seventh", "P1,M3,A5,M7");
  }

  public ChordFactory() {}

  public Chord createChordFromIntervalPattern(IntervalPattern intervalPattern, Tone tonic) {
    ToneCollection toneCollection = convertIntervalPatternToToneCollection(intervalPattern, tonic);
    ChordQuality chordQuality = evaluateChordQualityFromIntervalPattern(intervalPattern);

    Chord chord = new Chord(toneCollection.getTone(0).toString() + " " + chordQuality.getText(),
        toneCollection, chordQuality, intervalPattern, null);

    return chord;
  }

  private ToneCollection convertIntervalPatternToToneCollection(IntervalPattern intervalPattern,
      Tone tonic) {
    int toneCount = intervalPattern.getSize();
    ToneCollectionBuilder tCB = new ToneCollectionBuilder();

    tCB.insert(tonic);
    for (int index = 1; index < toneCount; index++) {
      int halfSteps = intervalPattern.getIntervalByLocation(index).getHalfSteps();
      Tone currentTone = TonalSpectrum.traverseDistance(tonic, halfSteps);
      tCB.insert(currentTone);
    }

    return tCB.toToneCollection();
  }

  private ChordQuality evaluateChordQualityFromIntervalPattern(IntervalPattern intervalPattern) {
    QualityEvaluationRule qualityRule =
        chooseQualityEvaluationRuleForIntervalPattern(intervalPattern);
    ChordQuality chordQuality = qualityRule.applyRuleForIntervalPattern(intervalPattern);

    return chordQuality;
  }

  private QualityEvaluationRule chooseQualityEvaluationRuleForIntervalPattern(
      IntervalPattern intervalPattern) {
    if (isTriad(intervalPattern)) {
      return new TriadQualityEvaluationRule();
    }
    else if (isSeventhChord(intervalPattern)) {
      return new SeventhChordQualityEvaluationRule();
    }

    throw new IllegalArgumentException();
  }

  private boolean isTriad(IntervalPattern intervalPattern) {
    if (intervalPattern.getSize() != 3) {
      return false;
    }

    int count = 0;
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.FIRST);
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.THIRD);
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.FIFTH);

    if (count != 3) {
      return false;
    }

    return true;
  }

  private boolean isSeventhChord(IntervalPattern intervalPattern) {
    if (intervalPattern.getSize() != 4) {
      return false;
    }

    int count = 0;
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.FIRST);
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.THIRD);
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.FIFTH);
    count += intervalPattern.getCountByIntervalNumber(IntervalNumber.SEVENTH);

    if (count != 4) {
      return false;
    }

    return true;
  }



  /*
  abstract public Chord createChordFromIntervalPattern(IntervalPattern intervalPattern, Tone tonic)
      throws IllegalArgumentException;
  
  abstract public Chord createChordFromHarmonizable(Harmonizable harmonizableScale,
      HarmonicPreference preference, IntervalNumber intervalNumber) throws IllegalArgumentException;
  
  abstract protected HarmonicProfile evaluateProfile(Tone[] toneArray, HarmonicPreference preference);
  
  
  
  public Chord createChordFromHarmonizable(Harmonizable harmonizableScale,
      HarmonicPreference preference, IntervalNumber intervalNumber) throws CremonaException {
  
    Tone[] tones = harmonizableScaleAndIntervalNumberToToneArray(harmonizableScale, intervalNumber,
        preference, 3);
    HarmonicProfile harmonicProfile = evaluateProfile(tones, preference);
  
    return new Chord(tones[0].getText() + " " + harmonicProfile.chordQuality.getText(), tones,
        harmonicProfile.chordQuality, harmonicProfile.intervalNumberSet);
  }
  
  private Tone[] harmonizableScaleAndIntervalNumberToToneArray(Harmonizable harmonizableScale,
      IntervalNumber sourceInterval, HarmonicPreference preference, int limit) {
    Tone[] toneArray = new Tone[limit];
  
    for (int index = 0, offset = 0; index < limit; index++, offset += preference.getOffset()) {
      toneArray[index] = harmonizableScale.getToneAtRelativeIntervalNumber(sourceInterval,
          IntervalNumber.values()[offset]);
    }
  
    return toneArray;
  }
   
  
  private HarmonicProfile evaluateProfile(Tone[] toneArray, HarmonicPreference preference)
      throws CremonaException {
    Interval[] intervalArray = this.convertToneArrayToIntervalArray(toneArray, preference);
  
  
    
    switch (preference) {
      case TERTIARY:
        return renderTertiaryProfile(intervalArray);
      case QUARTAL:
        break;
      case QUINTAL:
        break;
    }
    
  
    return renderTertiaryProfile(intervalArray);
  }
  
  
  private Interval[] convertToneArrayToIntervalArray(Tone[] toneArray,
      HarmonicPreference preference) throws IllegalArgumentException {
    Interval[] intervalArray = new Interval[toneArray.length - 1];
    int offset = preference.getOffset();
  
    for (int index = 0, segment = 0; index < intervalArray.length; index++, segment += offset) {
      intervalArray[index] = Interval.halfStepsAndIntervalNumberToInterval(
          TonalSpectrum.measureDistance(toneArray[0], toneArray[index + 1]),
          IntervalNumber.values()[segment + offset]);
    }
  
    return intervalArray;
  }
  
  private HarmonicProfile renderTertiaryProfile(Interval[] intervalArray) throws CremonaException {
    HarmonicProfile harmonicProfile = new HarmonicProfile();
    LinkedHashSet<IntervalNumber> intervalNumberSet = new LinkedHashSet<IntervalNumber>();
  
    if (intervalArray[0] == Interval.MAJOR_THIRD) {
      intervalNumberSet.add(IntervalNumber.THIRD);
  
      if (intervalArray[1] == Interval.PERFECT_FIFTH) {
        intervalNumberSet.add(IntervalNumber.FIFTH);
        harmonicProfile.chordQuality = ChordQuality.MAJOR;
        harmonicProfile.intervalNumberSet = intervalNumberSet;
  
        return harmonicProfile;
      }
      else {
        if (intervalArray[1] == Interval.AUGMENTED_FIFTH) {
          intervalNumberSet.add(IntervalNumber.FIFTH);
          harmonicProfile.chordQuality = ChordQuality.AUGMENTED;
          harmonicProfile.intervalNumberSet = intervalNumberSet;
  
          return harmonicProfile;
        }
      }
    }
    else {
      if (intervalArray[0] == Interval.MINOR_THIRD) {
        intervalNumberSet.add(IntervalNumber.THIRD);
  
        if (intervalArray[1] == Interval.PERFECT_FIFTH) {
          intervalNumberSet.add(IntervalNumber.FIFTH);
          harmonicProfile.chordQuality = ChordQuality.MINOR;
          harmonicProfile.intervalNumberSet = intervalNumberSet;
  
          return harmonicProfile;
        }
        else {
          if (intervalArray[1] == Interval.DIMINISHED_FIFTH) {
            intervalNumberSet.add(IntervalNumber.FIFTH);
            harmonicProfile.chordQuality = ChordQuality.DIMINISHED;
            harmonicProfile.intervalNumberSet = intervalNumberSet;
  
            return harmonicProfile;
          }
        }
      }
    }
  
    throw new CremonaException("Intervals invalid");
  }
  
  
  
  private Tone[] intervalPatternAndTonicToToneArray(ChordIntervalPattern chordIntervalPattern,
      Tone tonic) {
    int toneCount = chordIntervalPattern.getSize() + 1;
    Tone[] tones = new Tone[toneCount];
  
    tones[0] = tonic;
  
    for (int index = 1; index < toneCount; index++) {
      Tone cur = TonalSpectrum.traverseDistance(tones[0],
          chordIntervalPattern.getIntervalByLocation(index - 1).getHalfSteps());
  
      tones[index] = cur;
    }
  
    return tones;
  }
  
  class HarmonicProfile {
    ChordQuality chordQuality = null;
    LinkedHashSet<IntervalNumber> intervalNumberSet = null;
  }
  
  public static String majorPattern = "M3,P5";
  public static String minorPattern = "m3,P5";
  public static String diminishedPattern = "m3,d5";
  public static String augmentedPattern = "M3,A5";
  public static String suspendedFourthPattern = "P4,P5";
  */

  public static IntervalPattern MAJOR_CHORD_PATTERN;
  public static IntervalPattern MINOR_CHORD_PATTERN;
  public static IntervalPattern DIMINISHED_CHORD_PATTERN;
  public static IntervalPattern AUGMENTED_CHORD_PATTERN;
  public static IntervalPattern MAJOR_SEVENTH_CHORD_PATTERN;
  public static IntervalPattern DOMINANT_SEVENTH_CHORD_PATTERN;
  public static IntervalPattern MINOR_SEVENTH_CHORD_PATTERN;
  public static IntervalPattern MINORMAJOR_SEVENTH_CHORD_PATTERN;
  public static IntervalPattern HALF_DIMINISHED_SEVENTH_CHORD_PATTERN;
  public static IntervalPattern DIMINISHED_SEVENTH_CHORD_PATTERN;
  public static IntervalPattern AUGMENTED_MAJOR_SEVENTH_CHORD_PATTERN;
}