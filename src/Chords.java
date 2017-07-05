
public class Chords {
	
	static final String[] SYMBOLS = { "2", "4", "5", "6", "7", "8", "9", "11", "13", "-", "M", "min", "add", "maj", "Maj", "sus",
			"dim", "aug", "dom", "+", "o", "°", "Ø", "ø", "m", "b", "#", "sus"};
	static final String[] NOTES = { "C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B" };
	static final String[] NOTES_ALT = { "C", "Db", "D", "D#", "E", "F", "Gb", "G", "Ab", "A", "A#", "B" };
	static final String[] NOTES_SHARP_FLAT = { "C", "D", "E", "F", "G", "A", "B", "#", "b" };
	
	//Integerervals
	static final Integer ROOT_SEMITONES = 0;
	static final Integer MINOR_SECOND_SEMITONES = 1;
	static final Integer MAJOR_SECOND_SEMITONES = 2;
	static final Integer MINOR_THIRD_SEMITONES = 3;
	static final Integer MAJOR_THIRD_SEMITONES = 4;
	static final Integer PERFECT_FOURTH_SEMITONES = 5;
	static final Integer AUGMENTED_FOURTH_SEMITONES = 6;
	static final Integer DIMINISHED_FIFTH_SEMITONES = AUGMENTED_FOURTH_SEMITONES;
	static final Integer PERFECT_FIFTH_SEMITONES = 7;
	static final Integer AUGMENTED_FIFTH_SEMITONES = 8;
	static final Integer MAJOR_SIXTH_SEMITONES = 9;
	static final Integer DIMINISHED_SEVENTH_SEMITONES = MAJOR_SIXTH_SEMITONES;
	static final Integer AUGMENTED_SIXTH_SEMITONES = 10;
	static final Integer MINOR_SEVENTH_SEMITONES = 10;
	static final Integer MAJOR_SEVENTH_SEMITONES = 11;
	static final Integer AUGMENTED_SEVENTH_SEMITONES = 12;
	static final Integer PERFECT_OCTAVE_SEMITONES = AUGMENTED_SEVENTH_SEMITONES;
	static final Integer MINOR_NINTH_SEMITONES = 13;
	static final Integer MAJOR_NINTH_SEMITONES = 14;
	static final Integer SHARP_NINTH_SEMITONES = 15;
	static final Integer DIMINISHED_ELEVENTH_SEMITONES = 16;
	static final Integer PERFECT_ELEVENTH_SEMITONES = 17;
	static final Integer SHARP_ELEVENTH_SEMITONES = 18;
	static final Integer MAJOR_THIRTEEN_SEMITONES = 21;
	
	//chords
	static final Integer[] MINOR_TRIAD_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES };
	static final Integer[] MAJOR_TRIAD_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES };
	
	static final Integer[] DIMINISHED_TRIAD_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, DIMINISHED_FIFTH_SEMITONES };
	static final Integer[] AUGMENTED_TRIAD_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, AUGMENTED_FIFTH_SEMITONES };
	
	static final Integer[] MAJOR_SIXTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SIXTH_SEMITONES}; //major_triad_chord + major_sixth_semitones
	
	static final Integer[] MINOR_SEVENTH_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES }; //minor_triad_chord + minor_seventh_semitones
	static final Integer[] MAJOR_SEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SEVENTH_SEMITONES }; //major_triad_chord + major_seventh_semitones
	static final Integer[] DOMINANT_SEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES }; //major_triad_chord + minor_seventh_semitones
	
	static final Integer[] MINOR_NINTH_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES}; //minor_seventh_chord + major_nIntegerh_semitones
	static final Integer[] MAJOR_NINTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES}; //major_seventh_chord + major_nIntegerh_semitones
	static final Integer[] DOMINANT_NINTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES}; //dominant_seventh_chord + major_nIntegerh_semitones
	static final Integer[] ADDED_NINTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_NINTH_SEMITONES}; //major_triad_chord + major_nIntegerh_semitones
	
	static final Integer[] MAJOR_ELEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MAJOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES, PERFECT_ELEVENTH_SEMITONES};
			//major_nIntegerh_chord + perfect_eleventh_semitones
	static final Integer[] MINOR_ELEVENTH_CHORD = {ROOT_SEMITONES, MINOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES, PERFECT_ELEVENTH_SEMITONES};
			//minor_nIntegerh_chord + perfect_eleventh_semitones
	static final Integer[] DOMINANT_ELEVENTH_CHORD = {ROOT_SEMITONES, MAJOR_THIRD_SEMITONES, PERFECT_FIFTH_SEMITONES,
			MINOR_SEVENTH_SEMITONES, MAJOR_NINTH_SEMITONES, PERFECT_ELEVENTH_SEMITONES};
			//dominant_nIntegerh_chord + perfect_eleventh_semitones
	
	static final Integer[] SUSPENDED_SECOND_CHORD = {ROOT_SEMITONES, MAJOR_SECOND_SEMITONES, PERFECT_FIFTH_SEMITONES};
	static final Integer[] SUSPENDED_FOURTH_CHORD = {ROOT_SEMITONES, PERFECT_FOURTH_SEMITONES, PERFECT_FIFTH_SEMITONES};
	
	static final Integer[] POWER_CHORD = {ROOT_SEMITONES, PERFECT_FIFTH_SEMITONES};
}
