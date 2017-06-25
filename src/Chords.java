
public class Chords {
	
	//intervals
	static final int ROOT_SEMITONES = 0;
	static final int MINOR_SECOND_SEMITONES = 1;
	static final int MAJOR_SECOND_SEMITONES = 2;
	static final int MINOR_THIRD_SEMITONES = 3;
	static final int MAJOR_THIRD_SEMITONES = 4;
	static final int PERFECT_FOURTH_SEMITONES = 5;
	static final int AUGMENTED_FOURTH_SEMITONES = 6;
	static final int DIMINISHED_FIFTH_SEMITONES = AUGMENTED_FOURTH_SEMITONES;
	static final int PERFECT_FIFTH_SEMITONES = 7;
	static final int AUGMENTED_FIFTH_SEMITONES = 8;
	static final int MAJOR_SIXTH_SEMITONES = 9;
	static final int DIMINISHED_SEVENTH_SEMITONES = MAJOR_SIXTH_SEMITONES;
	static final int MINOR_SEVENTH_SEMITONES = 10;
	static final int MAJOR_SEVENTH_SEMITONES = 11;
	static final int PERFECT_OCTAVE_SEMITONES = 12;
	static final int MINOR_NINTH_SEMITONES = 13;
	static final int MAJOR_NINTH_SEMITONES = 14;
	static final int PERFECT_ELEVENTH_SEMITONES = 17;
	
	//chords
	static final int[] MINOR_TRIAD_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES };
	static final int[] MAJOR_TRIAD_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES };
	
	static final int[] DIMINISHED_TRIAD_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, DIMINISHED_FIFTH_SEMITONES };
	static final int[] AUGMENTED_TRIAD_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, AUGMENTED_FIFTH_SEMITONES };
	
	static final int[] MAJOR_SIXTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SIXTH_SEMITONES}; //major_triad_chord + major_sixth_semitones

	
	static final int[] MINOR_SEVENTH_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES }; //minor_triad_chord + minor_seventh_semitones
	static final int[] MAJOR_SEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SEVENTH_SEMITONES }; //major_triad_chord + major_seventh_semitones
	static final int[] DOMINANT_SEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES }; //major_triad_chord + minor_seventh_semitones
	
	static final int[] MINOR_NINTH_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES}; //minor_seventh_chord + major_ninth_semitones
	static final int[] MAJOR_NINTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES}; //major_seventh_chord + major_ninth_semitones
	static final int[] DOMINANT_NINTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES}; //dominant_seventh_chord + major_ninth_semitones
	static final int[] ADDED_NINTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_NINTH_SEMITONES}; //major_triad_chord + major_ningth_semitones
	
	static final int[] MAJOR_ELEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES, PERFECT_ELEVENTH_SEMITONES};
			//major_ninth_chord + perfect_eleventh_semitones
	static final int[] MINOR_ELEVENTH_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES, PERFECT_ELEVENTH_SEMITONES};
			//minor_ninth_chord + perfect_eleventh_semitones
	static final int[] DOMINANT_ELEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES, PERFECT_ELEVENTH_SEMITONES};
			//dominant_ninth_chord + perfect_eleventh_semitones
	
	static final int[] SUSPENDED_SECOND_CHORD = {ROOT_SEMITONES, MAJOR_SECOND_SEMITONES, PERFECT_FIFTH_SEMITONES};
	static final int[] SUSPENDED_FOURTH_CHORD = {ROOT_SEMITONES, PERFECT_FOURTH_SEMITONES, PERFECT_FIFTH_SEMITONES};
	
	static final int[] POWER_CHORD = {ROOT_SEMITONES, PERFECT_FIFTH_SEMITONES};
	
	static final int[] SUSPENDED_CHORD = {ROOT_SEMITONES, PERFECT_FOURTH_SEMITONES, PERFECT_FIFTH_SEMITONES };
}
