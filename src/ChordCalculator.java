import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
	public ChordCalculator() {
		long start = System.currentTimeMillis();
		intTonote = new HashMap<Integer, String>();
		composition = new HashMap<List<String>, String>();
		for (int i = 0; i < Chords.NOTES.length; i++)
			intTonote.put(i, Chords.NOTES[i]);
		fillMaps();
		System.out.println(System.currentTimeMillis()-start);
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

		String chord = "";
		chord = composition.get(notes_aux);
		if (chord != null)
			return chord;

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
		String[] roots = chord.split("/");
		String bassNote = "";
		if (roots.length > 1)
			try {
				bassNote = getRootNote(roots[1]);
			} catch (UnknownNoteException e) {
			}

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
			else // eg dominant
				compositionSemitones.add(Chords.MAJOR_THIRD_SEMITONES);

			if (isAugmented(chord) || isAugmentedFifth(chord))
				compositionSemitones.add(Chords.AUGMENTED_FIFTH_SEMITONES);
			else if (isDiminished(chord) || isHalfDiminished(chord) || isJustDiminishedFifth(chord))
				compositionSemitones.add(Chords.DIMINISHED_FIFTH_SEMITONES);
			else
				compositionSemitones.add(Chords.PERFECT_FIFTH_SEMITONES);

			// 6
			if (isAddedSixth(chord)) {
				if (isFlatSixth(chord))
					compositionSemitones.add(Chords.AUGMENTED_FIFTH_SEMITONES);
				else
					compositionSemitones.add(Chords.MAJOR_SIXTH_SEMITONES);
			}

			// 7
			if (isSeventh(chord)
					|| (isNinth(chord) && !isAddedNinth(chord) && !isSharpNinth(chord) && !isFlatNinth(chord))
					|| (isEleventh(chord) && !isAddedEleventh(chord) && !isSharpEleventh(chord))
					|| (isThirteenth(chord) && !isAddedThirteenth(chord)))
				if (isMajor(chord) || isMinorMajor(chord))
					compositionSemitones.add(Chords.MAJOR_SEVENTH_SEMITONES);
				else if (isMinor(chord) || isHalfDiminished(chord))
					compositionSemitones.add(Chords.MINOR_SEVENTH_SEMITONES);
				else if (isDiminished(chord))
					compositionSemitones.add(Chords.DIMINISHED_SEVENTH_SEMITONES);
				else // dominant, augmented and others
					compositionSemitones.add(Chords.MINOR_SEVENTH_SEMITONES);

			// 9
			if ((isNinth(chord) || isAddedNinth(chord))
					|| (isEleventh(chord) && !isAddedEleventh(chord) && !isSharpEleventh(chord))
					|| (isThirteenth(chord) && !isAddedThirteenth(chord)))
				if (isSharpNinth(chord))
					compositionSemitones.add(Chords.SHARP_NINTH_SEMITONES);
				else if (!isDiminished(chord) && !isFlatNinth(chord))
					compositionSemitones.add(Chords.MAJOR_NINTH_SEMITONES);
				else if (isFlatNinth(chord) || isMinor(chord) && (isDiminished(chord) || isHalfDiminished(chord)))
					compositionSemitones.add(Chords.MINOR_NINTH_SEMITONES);
				else // eg minor major, minor dominant
					compositionSemitones.add(Chords.MAJOR_NINTH_SEMITONES);

			// 11
			if (isEleventh(chord) || (isThirteenth(chord) && !isAddedThirteenth(chord)))
				if (isDiminished(chord))
					compositionSemitones.add(Chords.DIMINISHED_ELEVENTH_SEMITONES);
				else if (isSharpEleventh(chord))
					compositionSemitones.add(Chords.SHARP_ELEVENTH_SEMITONES);
				else
					compositionSemitones.add(Chords.PERFECT_ELEVENTH_SEMITONES);

			// 13
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
		if (!bassNote.isEmpty())
			notes_aux.add(bassNote);
		for (int semitones : compositionSemitones)
			notes_aux.add(intTonote.get((n + semitones) % 12));

		if (composition.get(notes_aux) == null)
			composition.put(notes_aux, chord);
		else
			System.out.println("repeated");

		return notes_aux;
	}

	private void fillMaps() {
		for (String chord : intTonote.values()) {
			ArrayList<String> symbols = new ArrayList<String>();
			String[] numbers = { "", "7", "9", "11", "13" };
			String[] symbolsArray = { "", "m", "M", "mM", "+M", "dim", "aug" };
			for (String n : numbers)
				for (String symbol : symbolsArray) {
					if (n.isEmpty() && symbol.matches("M|(mM)|(\\+M)"))
						continue;
					symbols.add(symbol + n);
				}

			String[] moreSymbolsArray = { "sus2", "sus4", "add9", "add11", "add13", "7add11", "7add13" };
			ListIterator<String> it = symbols.listIterator();
			String symbol = "";
			while (it.hasNext()) {
				symbol = it.next();
				for (String anotherSymbol : moreSymbolsArray) {
					if (anotherSymbol.contains("sus")
							&& (isMinor(chord + symbol) || symbol.contains("dim") || symbol.contains("aug")))
						continue;
					if (symbol.contains("13") && anotherSymbol.contains("add"))
						continue;
					if (symbol.contains("11") && anotherSymbol.matches("(?s).*add([0-5][7-9])*.*"))
						continue;
					if (symbol.contains("9") && anotherSymbol.matches("(?s).*add(6|(11))*.*"))
						continue;
					if (symbol.contains("7") && anotherSymbol.contains("add9"))
						continue;
					it.add(symbol + anotherSymbol);
				}
			}

			String[] evenMoreSymbolsArray = { "b5", "b7", "b9", "#5", "#7", "#9", "#11" };
			it = symbols.listIterator();
			while (it.hasNext()) {
				symbol = it.next();
				if (!symbol.isEmpty()) {
					for (String anotherSymbol : evenMoreSymbolsArray) {
						String newSymbol = symbol + anotherSymbol;
						if (newSymbol.contains("mb5"))
							continue;
						if (newSymbol.contains("5") && (newSymbol.contains("sus") || newSymbol.contains("dim")
								|| newSymbol.contains("aug")))
							continue;
						if (isAddedNinth(chord + newSymbol)
								&& (isFlatNinth(chord + newSymbol) || isSharpNinth(chord + newSymbol)))
							continue;
						if (isAddedEleventh(chord + newSymbol) && isSharpEleventh(chord + newSymbol))
							continue;
						it.add(newSymbol);
					}
				}
			}

			symbols.add("5");

			try {
				for (String symb : symbols) {
					if ((symb).matches("(?s).*[0-9][0-9][0-9]+.*"))
						continue;
					if ((symb).matches("(?s).*[2-9][0-9]+.*"))
						continue;
					if ((getSymbols(chord + symb)).matches("(?s).*(#|b)7.*"))
						continue;
					//System.out.println(chord + symb);
					//System.out.println(getComposition(chord + symb) + "\n");
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	// returns the root of the given chord
	// can even change A# to Bb, etc, according to Chords.NOTES
	public String getRootNote(String chord) throws UnknownNoteException {
		if (chord.length() > 1)
			chord = chord.substring(0, 2);

		if (!chord.contains("b") && !chord.contains("#"))
			chord = chord.substring(0, 1);

		// check if chord matches any note in NOTES
		for (String note : Chords.NOTES)
			if (chord.equals(note))
				return note;

		// check on alternative notes representation (A# instead of Bb, etc.)
		for (int i = 0; i < Chords.NOTES_ALT.length; i++)
			if (chord.equals(Chords.NOTES_ALT[i]))
				return Chords.NOTES[i];

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

	// min maj aug dim
	private boolean isMinor(String chord) {
		return chord.replace("dim", "").replace("dom", "").replace("maj", "").contains("m") || chord.contains("min")
				|| getSymbols(chord).matches("-.*");
	}

	private boolean isMajor(String chord) {
		return chord.contains("M") || chord.contains("maj");
	}

	private boolean isMinorMajor(String chord) {
		return isMinor(chord) && isMajor(chord);
	}

	private boolean isAugmented(String chord) {
		return !isSharpNinth(chord) && !isSharpEleventh(chord) && chord.contains("+") || chord.contains("aug");
	}

	private boolean isDiminished(String chord) {
		chord = chord.replace("dom", "");
		return !isHalfDiminished(chord) && !isJustDiminishedFifth(chord) && !isFlatNinth(chord)
				&& (chord.contains("o") || chord.contains("°") || chord.contains("dim"));
	}

	private boolean isHalfDiminished(String chord) {
		return chord.contains("Ø") || chord.contains("ø") || chord.contains("half")
				|| (chord.contains("om7") && !chord.contains("dom7"));
	}

	private boolean isDiminishedMajor(String chord) {
		return chord.replace("dom", "").matches("(?s).*(dim|°|o)(M|maj).*");
	}

	// suspended chords
	private boolean isSuspendedSecond(String chord) {
		return chord.contains("sus2");
	}

	private boolean isSuspendedFourth(String chord) {
		return chord.contains("sus4") || (!isSuspendedSecond(chord) && chord.contains("sus"));
	}

	// added tone chords
	private boolean isAddedSixth(String chord) {
		return chord.contains("6");
	}

	private boolean isAddedNinth(String chord) {
		return !chord.matches("(?s).*7((/9)|(add(9|2))).*") // 7add9 = 9
				&& (chord.matches("(?s).*(/|add)(9|2).*") || (chord.matches("(?s).*2.*")
						&& !chord.matches("(?s).*[0-9]2.*") && !isSuspendedSecond(chord)));
	}

	private boolean isAddedEleventh(String chord) {
		return !chord.matches("(?s).*(9|2)(/|add)11.*") // 9add11 = 11
				&& (chord.contains("add11") || chord.contains("/11"));
	}

	private boolean isAddedThirteenth(String chord) {
		return !chord.matches("(?s).*11(/|add)13.*") // 11add13 = 13
				&& (chord.contains("add13") || chord.contains("/13"));
	}

	// numbers
	// TODO: check G5m
	private boolean isPowerChord(String chord) {
		return getSymbols(chord).matches("5|8");
	}

	private boolean isSeventh(String chord) {
		return chord.contains("7") || isHalfDiminished(chord); // CØ = CØ7
	}

	private boolean isNinth(String chord) {
		return chord.contains("9");
	}

	private boolean isEleventh(String chord) {
		return chord.contains("11");
	}

	private boolean isThirteenth(String chord) {
		return chord.contains("13");
	}

	// flats/diminished
	private boolean isFlatSixth(String chord) {
		return chord.matches("(?s).*(-|b)6.*");
	}

	private boolean isFlatNinth(String chord) {
		return chord.matches("(?s).*([0-9]+)(-|b|dim)9.*") || getSymbols(chord).contains("b9");
	}

	private boolean isJustDiminishedFifth(String chord) {
		return chord.matches("(?s).*([0-9]+)(dim|m|-|b)5.*") || getSymbols(chord).matches("(?s).*((-|b)5).*");
	}

	// sharp/augmented
	private boolean isSharpSixth(String chord) {
		return chord.matches("(?s).*(+|#)6.*");
	}

	private boolean isAugmentedFifth(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)5.*") || getSymbols(chord).matches("(?s).*(\\+|#)5.*");
	}

	private boolean isSharpNinth(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)9.*") || getSymbols(chord).contains("#9");
	}

	private boolean isSharpEleventh(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)11.*") || getSymbols(chord).contains("#11");
	}

	// ref
	// https://www.scales-chords.com/chord
	// http://jguitar.com/
	// http://scottdavies.net/chords_and_scales/music.html
	// http://members.jamplay.com/teaching-tools/chord-library/family/c?
	// https://chord-c.com/guitar-chord/
}
