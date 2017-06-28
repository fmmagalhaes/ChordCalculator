import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ChordCalculator {

	private Map<Integer, String> intTonote; // maps 0 to C, 1 to C#, 2 to D, etc
	private Map<List<String>, String> composition; // maps a list of notes to
													// the chord they form
	// <list, string> and not <string, list> because otherwise it would be
	// necessary
	// to keep keys like CM7, Cmaj7, C7maj, Cmajor7, etc, which all mean the
	// same

	/**
	 * Constructs a ChordCalculator which allows a set of operations
	 * 
	 * @throws UnknownNoteException
	 */
	public ChordCalculator() throws UnknownNoteException {
		intTonote = new HashMap<Integer, String>();
		composition = new HashMap<List<String>, String>();
		fillMaps();
	}

	/**
	 * @param chord
	 * @param n
	 * @return the result of adding n semitones to chord
	 * @throws UnknownNoteException
	 */
	public String addSemitones(String chord, int n) throws UnknownNoteException {
		String symbols = getSymbols(chord);
		String root = getRootNote(chord);
		int m = (Integer) Utils.getKeyFromValue(intTonote, root);
		String newRoot = intTonote.get((m + n) % 12);
		newRoot += symbols;
		return newRoot;
	}

	/**
	 * @param chord
	 * @param n
	 * @return the result of subtracting n semitones to chord
	 * @throws UnknownNoteException
	 */
	public String subtractSemitones(String chord, int n) throws UnknownNoteException {
		String symbols = getSymbols(chord);
		String root = getRootNote(chord);
		int m = (Integer) Utils.getKeyFromValue(intTonote, root);
		String newRoot = intTonote.get((Math.abs(m - n + 12)) % 12);
		newRoot += symbols;
		return newRoot;
	}

	/**
	 * @param notes
	 * @return the chord which is formed exactly by the notes present in the
	 *         parameter
	 * @throws UnknownNoteException
	 */
	public String getChordFromComposition(List<String> notes) throws UnknownNoteException {
		List<String> notes_aux = new ArrayList<String>();

		// need to transform A# to Bb, Db to C#, etc.
		for (String note : notes)
			notes_aux.add(getRootNote(note) + getSymbols(note));

		// TODO: just try composition.get(notes_aux). than execute this cycle
		for (Entry<List<String>, String> entry : composition.entrySet()) {
			if (Utils.equalLists(entry.getKey(), notes_aux))
				return entry.getValue();
		}
		return null;
	}

	/**
	 * @param chord
	 * @return the list of notes which form the chord
	 * @throws UnknownNoteException
	 */
	public List<String> getComposition(String chord) throws UnknownNoteException {
		String root = getRootNote(chord);
		int n = (Integer) Utils.getKeyFromValue(intTonote, root);

		ArrayList<Integer> compositionSemitones = new ArrayList<Integer>();
		compositionSemitones.add(Chords.ROOT_SEMITONES);

		boolean others = false;
		if (isPowerChord(chord)) {
			compositionSemitones.add(Chords.PERFECT_FIFTH_SEMITONES);
			others = true;
		}

		if (!others) {
			if (isMajor(chord) && !isMinorMajor(chord) && !isDiminishedMajor(chord))
				compositionSemitones.add(Chords.MAJOR_THIRD_SEMITONES);
			else if (isMinor(chord) || isDiminished(chord) || isHalfDiminished(chord))
				compositionSemitones.add(Chords.MINOR_THIRD_SEMITONES);
			else // eg dominant, flat five
				compositionSemitones.add(Chords.MAJOR_THIRD_SEMITONES);

			if (isAugmented(chord))
				compositionSemitones.add(Chords.AUGMENTED_FIFTH_SEMITONES);
			else if (isDiminished(chord) || isHalfDiminished(chord) || isJustDiminishedFifth(chord))
				compositionSemitones.add(Chords.DIMINISHED_FIFTH_SEMITONES);
			else
				compositionSemitones.add(Chords.PERFECT_FIFTH_SEMITONES);

			//6
			if(isAddedSixth(chord))
				compositionSemitones.add(Chords.MAJOR_SIXTH_SEMITONES);
			
			//7
			if (!isAddedNinth(chord)
					&& (isSeventh(chord) || isNinth(chord) || isEleventh(chord) || isThirteenth(chord)))
				if (isMajor(chord) || isMinorMajor(chord))
					compositionSemitones.add(Chords.MAJOR_SEVENTH_SEMITONES);
				else if (isMinor(chord) || isHalfDiminished(chord))
					compositionSemitones.add(Chords.MINOR_SEVENTH_SEMITONES);
				else if (isDiminished(chord))
					compositionSemitones.add(Chords.DIMINISHED_SEVENTH_SEMITONES);
				else // dominant, augmented and others
					compositionSemitones.add(Chords.MINOR_SEVENTH_SEMITONES);

			//9
			if (!isAddedEleventh(chord)
					&& (isAddedNinth(chord) || isNinth(chord) || isEleventh(chord) || isThirteenth(chord)))
				if (isSharpNinth(chord))
					compositionSemitones.add(Chords.SHARP_NINTH_SEMITONES);
				else if (!isDiminished(chord) && !isFlatNinth(chord))
					compositionSemitones.add(Chords.MAJOR_NINTH_SEMITONES);
				else if (isFlatNinth(chord) || isMinor(chord) && (isDiminished(chord) || isHalfDiminished(chord)))
					compositionSemitones.add(Chords.MINOR_NINTH_SEMITONES);
				else // eg minor major, minor dominant
					compositionSemitones.add(Chords.MAJOR_NINTH_SEMITONES);

			//11
			if (!isAddedThirteenth(chord)
					&& (isEleventh(chord) || isThirteenth(chord)))
				if (isDiminished(chord))
					compositionSemitones.add(Chords.DIMINISHED_ELEVENTH_SEMITONES);
				else if (isSharpEleventh(chord))
					compositionSemitones.add(Chords.SHARP_ELEVENTH_SEMITONES);
				else
					compositionSemitones.add(Chords.PERFECT_ELEVENTH_SEMITONES);

			//13
			if (isThirteenth(chord))
				compositionSemitones.add(Chords.MAJOR_THIRTEEN_SEMITONES);

			boolean sus2 = isSuspendedSecond(chord);
			boolean sus4 = isSuspendedFourth(chord);
			if (sus2 || sus4) {
				compositionSemitones.remove(1);
				if (sus2)
					compositionSemitones.add(1, Chords.MAJOR_SECOND_SEMITONES);
				else
					compositionSemitones.add(1, Chords.PERFECT_FOURTH_SEMITONES);
			}
		}

		ArrayList<String> notes_aux = new ArrayList<String>();
		for (int semitones : compositionSemitones)
			notes_aux.add(intTonote.get((n + semitones) % 12));
		composition.put(notes_aux, chord);

		return notes_aux;
	}

	private void fillMaps() throws UnknownNoteException {
		int i = 0;
		for (String note : Chords.NOTES)
			intTonote.put(i++, note);

		for (String chord : intTonote.values()) {
			getComposition(chord);
			getComposition(chord + "m");
			getComposition(chord + "dim");
			getComposition(chord + "aug");

			getComposition(chord + "5");
			getComposition(chord + "add9");

			getComposition(chord + "7");
			getComposition(chord + "m7");
			getComposition(chord + "M7");
			getComposition(chord + "aug7");
			getComposition(chord + "dim7");
			getComposition(chord + "mM7");
			getComposition(chord + "+M7"); // augMaj

			getComposition(chord + "9");
			getComposition(chord + "m9");
			getComposition(chord + "M9");
			getComposition(chord + "aug9");
			getComposition(chord + "dim9");
			getComposition(chord + "mM9");
			getComposition(chord + "+M9");

			getComposition(chord + "13");
			getComposition(chord + "m13");
			getComposition(chord + "M13");
			getComposition(chord + "aug13");
			getComposition(chord + "mM13");
			getComposition(chord + "+M13");
		}
	}

	// returns the root of the given chord
	// can even change A# to Bb, etc, according to Chords.NOTES
	public String getRootNote(String chord) throws UnknownNoteException {
		String ret = "";

		/*
		 * trying to find root in NOTES must keep searching the whole array
		 * otherwise getRootNote(C#) would return C, because C#.contains(C) is
		 * true
		 */
		for (String note : Chords.NOTES)
			if (chord.contains(note) && ret.length() < 2)
				ret = note;
		if (ret.length() == 2)
			return ret;

		// check on alternate notes representation (A# instead of Bb, etc.)
		for (int i = 0; i < Chords.NOTES_ALT.length; i++)
			if (chord.contains(Chords.NOTES_ALT[i]) && ret.length() < 2)
				ret = Chords.NOTES[i];

		if (ret.length() > 0)
			return ret;

		throw new UnknownNoteException();
	}

	// returns the symbols of the given chord (ex: M7, 7, m7)
	public String getSymbols(String chord) {
		String sharpFlat = "";
		if (chord.length() > 1)
			sharpFlat = chord.substring(1, 2);

		if (sharpFlat.equals("b") || sharpFlat.equals("#"))
			return chord.substring(2);
		else
			return chord.substring(1);
	}

	private boolean isPowerChord(String chord) {
		return getSymbols(chord).matches("5|8");
	}

	private boolean isMinor(String chord) {
		return chord.replace("dim", "").replace("maj", "").contains("m") || chord.contains("min");
	}

	private boolean isAddedSixth(String chord) {
		return chord.contains("6");
	}

	private boolean isAddedNinth(String chord) {
		return !chord.matches("(?s).*7((/9)|(add(9|2))).*") // 7add9 = 9
				&& (chord.contains("add9") || chord.contains("/9")
						|| (chord.contains("2") && !isSuspendedSecond(chord)));
	}
	
	private boolean isAddedEleventh(String chord) {
		return !chord.matches("(?s).*(9|2)(/|add)11.*") // 9add11 = 11
				&& (chord.contains("add11") || chord.contains("/11"));
	}
	
	private boolean isAddedThirteenth(String chord) {
		return !chord.matches("(?s).*11(/|add)13.*") // 11add13 = 13
				&& (chord.contains("add13") || chord.contains("/13"));
	}

	private boolean isSuspendedFourth(String chord) {
		return chord.contains("sus4") || (!isSuspendedSecond(chord) && chord.contains("sus"));
	}

	private boolean isSuspendedSecond(String chord) {
		return chord.contains("sus2");
	}

	private boolean isEleventh(String chord) {
		return chord.contains("11");
	}

	private boolean isNinth(String chord) {
		return chord.contains("9");
	}

	private boolean isThirteenth(String chord) {
		return chord.contains("13");
	}

	private boolean isMinorMajor(String chord) {
		return isMinor(chord) && isMajor(chord);
	}

	private boolean isDiminishedMajor(String chord) {
		return chord.matches("(?s).*(dim|°|o)(M|maj).*");
	}

	private boolean isMajor(String chord) {
		return chord.contains("M") || chord.contains("maj");
	}

	private boolean isFlatNinth(String chord) {
		return chord.matches("(?s).*([0-9]+)(-|b|dim)9.*");
	}

	private boolean isSharpNinth(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)9.*");
	}

	private boolean isSharpEleventh(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)11.*");
	}

	private boolean isAugmentedFifth(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)5.*");
	}

	private boolean isAugmented(String chord) {
		return !isSharpNinth(chord) && !isSharpEleventh(chord) && chord.contains("+") || chord.contains("aug");
	}

	private boolean isJustDiminishedFifth(String chord) {
		return chord.matches("(?s).*(7|9|11|13)(dim|m|-|b)5.*");
	}

	private boolean isDiminished(String chord) {
		return !isHalfDiminished(chord) && !isJustDiminishedFifth(chord) && !isFlatNinth(chord)
				&& (chord.contains("o") || chord.contains("°") || chord.contains("dim"));
	}

	private boolean isHalfDiminished(String chord) {
		return chord.contains("Ø") || chord.contains("ø") || chord.contains("half")
				|| (chord.contains("om7") && !chord.contains("dom7"));
	}

	private boolean isSeventh(String chord) {
		return chord.contains("7") || isHalfDiminished(chord); // CØ = CØ7
	}
}
