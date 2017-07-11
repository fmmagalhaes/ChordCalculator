import java.util.ArrayList;
import java.util.Collections;
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

	public ChordCalculator() {
		long start = System.currentTimeMillis();
		intTonote = new HashMap<Integer, String>();
		composition = new HashMap<List<String>, String>();
		for (int i = 0; i < Chords.NOTES.length; i++)
			intTonote.put(i, Chords.NOTES[i]);
		fillMaps();
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * Constructs a ChordCalculator which allows a set of operations
	 * 
	 * @param fill
	 *            whether there's a need to fill composition map with thousands
	 *            of chords. Only needed if getChordFromComposition will be used
	 * @throws UnknownNoteException
	 */
	public ChordCalculator(boolean fill) {
		long start = System.currentTimeMillis();
		intTonote = new HashMap<Integer, String>();
		composition = new HashMap<List<String>, String>();
		for (int i = 0; i < Chords.NOTES.length; i++)
			intTonote.put(i, Chords.NOTES[i]);
		if (fill)
			fillMaps();
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * @param chord
	 * @param n
	 * @return the result of adding n semitones (half steps) to chord
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
	 * @return the result of subtracting n semitones (half steps) to chord
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
		chord = root + getSymbols(chord, true);
		int n = (Integer) Utils.getKeyFromValue(intTonote, root);

		ArrayList<Integer> compositionSemitones = new ArrayList<Integer>();
		compositionSemitones.add(Chords.ROOT_SEMITONES);

		boolean others = false;
		if (isPowerChord(chord)) {
			compositionSemitones.add(Chords.PERFECT_FIFTH_SEMITONES);
			others = true;
		}

		if (!others) {
			// 3
			if (isSuspendedSecond(chord))
				compositionSemitones.add(Chords.MAJOR_SECOND_SEMITONES);
			else if (isSuspendedFourth(chord))
				compositionSemitones.add(Chords.PERFECT_FOURTH_SEMITONES);
			else if (isMinor(chord) || isDiminished(chord) || isHalfDiminished(chord))
				compositionSemitones.add(Chords.MINOR_THIRD_SEMITONES);
			else // eg major, dominant...
				compositionSemitones.add(Chords.MAJOR_THIRD_SEMITONES);

			// 5
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
					|| (isNinth(chord) && !isAddedNinth(chord) && !isFlatNinth(chord) && !isSharpNinth(chord))
					|| (isEleventh(chord) && !isAddedEleventh(chord) && !isDiminishedEleventh(chord)
							&& !isSharpEleventh(chord))
					|| (isThirteenth(chord) && !isAddedThirteenth(chord)))
				if (isMajor(chord))
					compositionSemitones.add(Chords.MAJOR_SEVENTH_SEMITONES);
				else if (isDiminished(chord))
					compositionSemitones.add(Chords.DIMINISHED_SEVENTH_SEMITONES);
				else // minor, half diminished, dominant, augmented and others
					compositionSemitones.add(Chords.MINOR_SEVENTH_SEMITONES);

			// 9
			if ((isNinth(chord) || isAddedNinth(chord)) || (isEleventh(chord) && !isAddedEleventh(chord)
					&& !isDiminishedEleventh(chord) && !isSharpEleventh(chord))
					|| (isThirteenth(chord) && !isAddedThirteenth(chord)))
				if (isSharpNinth(chord))
					compositionSemitones.add(Chords.SHARP_NINTH_SEMITONES);
				else if (isFlatNinth(chord))
					compositionSemitones.add(Chords.MINOR_NINTH_SEMITONES);
				else // eg minor major, minor dominant
					compositionSemitones.add(Chords.MAJOR_NINTH_SEMITONES);

			// 11
			if (isEleventh(chord) || (isThirteenth(chord) && !isAddedThirteenth(chord)))
				if (isSharpEleventh(chord))
					compositionSemitones.add(Chords.SHARP_ELEVENTH_SEMITONES);
				else if (isDiminished(chord) || isDiminishedEleventh(chord))
					compositionSemitones.add(Chords.DIMINISHED_ELEVENTH_SEMITONES);
				else
					compositionSemitones.add(Chords.PERFECT_ELEVENTH_SEMITONES);

			// 13
			if (isThirteenth(chord))
				if (isDiminishedThirteenth(chord))
					compositionSemitones.add(Chords.MINOR_THIRTEEN_SEMITONES);
				else
					compositionSemitones.add(Chords.MAJOR_THIRTEEN_SEMITONES);

		}

		ArrayList<String> notes_aux = new ArrayList<String>();
		if (!bassNote.isEmpty())
			notes_aux.add(bassNote);
		for (int semitones : compositionSemitones) {
			String note = intTonote.get((n + semitones) % 12);
			if (!notes_aux.contains(note))
				notes_aux.add(note);
		}

		// Collections.sort(notes_aux);
		if (composition.get(notes_aux) == null)
			composition.put(notes_aux, chord);
		else
			System.out.println("repeated");

		return notes_aux;
	}

	private void fillMaps() {
		for (String chord : intTonote.values()) {

			ArrayList<String> symbols = new ArrayList<String>();
			String[] numbers = { "", "6", "7", "9", "11", "13" };
			String[] symbolsArray = { "", "m", "M", "mM", "+M", "dim", "aug" };
			for (String n : numbers)
				for (String symbol : symbolsArray) {
					if (n.isEmpty() && symbol.matches("M|(mM)|(\\+M)"))
						continue;
					if (n.equals("6") && symbol.matches("dim|M|mM|(\\+M)"))
						continue; // Cm6 = CmM6
					symbols.add(symbol + n);
				}

			String[] moreSymbolsArray = { "sus2", "sus4", "add9", "add11" };// ,
																			// "add13"
																			// };
			ListIterator<String> it = symbols.listIterator();
			String symbol = "";
			while (it.hasNext()) {
				symbol = it.next();
				for (String anotherSymbol : moreSymbolsArray) {
					if (anotherSymbol.contains("sus")
							&& (isMinor(chord + symbol) || symbol.contains("dim") || symbol.contains("aug")))
						continue;
					if ((symbol.contains("dim") || symbol.contains("aug"))
							&& anotherSymbol.matches("(?s).*add([0-57-9])*.*"))
						continue; // there's already add9#5 and add9b5
					if (symbol.contains("13") && anotherSymbol.contains("add"))
						continue;
					if (symbol.contains("11") && anotherSymbol.matches("(?s).*add([0-57-9])*.*")
							|| anotherSymbol.contains("sus4"))
						continue; // perfect eleventh interval = suspended
									// f(redundancy)
					if (symbol.contains("9")
							&& (anotherSymbol.matches("(?s).*add(6|(11))*.*") || anotherSymbol.contains("sus2")))
						continue; // major ninth interval = suspended second
									// (redundancy)
					if (symbol.contains("7") && anotherSymbol.contains("add9"))
						continue;
					if ((symbol.matches(".*\\d.*") || symbol.contains("6")) && anotherSymbol.contains("add13"))
						continue; // Cm6 = Cmadd13
					it.add(symbol + anotherSymbol);
				}
			}

			String[] evenMoreSymbolsArray = { "b5", "b7", "b9", "#5", "#7", "#9", "#11", "b11" };
			it = symbols.listIterator();
			while (it.hasNext()) {
				symbol = it.next();
				if (!symbol.isEmpty()) {
					for (String anotherSymbol : evenMoreSymbolsArray) {
						String newSymbol = symbol + anotherSymbol;
						String newChord = chord + newSymbol;
						if (symbol.contains("9") && anotherSymbol.contains("9"))
							continue;
						if (symbol.contains("11") && anotherSymbol.contains("11"))
							continue;
						if (newSymbol.contains("mb5"))
							continue; // dim5
						if (newSymbol.contains("m6add9b5"))
							continue; // dim9
						if (newSymbol.contains("5") && newSymbol.contains("M"))
							continue; // there's already +M
						if (newSymbol.contains("5") && (newSymbol.contains("sus") || newSymbol.contains("dim")
								|| newSymbol.contains("aug") || newSymbol.contains("+")))
							continue;
						if (isAddedNinth(newChord) && (isFlatNinth(newChord) || isSharpNinth(newChord)))
							continue;
						if (isAddedEleventh(newChord) && isSharpEleventh(newChord))
							continue;
						if (newSymbol.contains("m6b5") || newSymbol.contains("madd13b5"))
							continue; // it's the same as dim7
						if (newSymbol.contains("6#5") || newSymbol.contains("7#5") || newSymbol.contains("9#5")
								|| newSymbol.contains("11#5") || newSymbol.contains("13#5"))
							continue; // it's the same as aug6, aug7, aug9,
										// aug11 and
										// aug13
						if (newSymbol.contains("11") && newSymbol.contains("9") && newSymbol.contains("7")
								&& newSymbol.contains("add"))
							continue; // no need to have "add" if we have 7 9
										// and 11
						if ((isMinor(newChord) || isDiminished(newChord)) && isSharpNinth(newChord))
							continue; // #9 is the same as m3 (redundancy)
						if (!(isMinor(newChord) || isDiminished(newChord)) && isDiminishedEleventh(newChord))
							continue; // b11 is the same as 3 (major third)
										// (redundancy)
						if (isDiminished(newChord) && isSharpEleventh(newChord))
							continue; // #11 is the same as m7 (redundancy)
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
					System.out.println(chord + symb);
					// getComposition(chord + symb);
					System.out.println(getComposition(chord + symb) + "\n");
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

	public String getSymbols(String chord) {
		return getSymbols(chord, false);
	}

	// returns the symbols of the given chord (ex: M7, 7, m7)
	public String getSymbols(String chord, boolean clean) {
		String sharpFlat = "";
		if (chord.length() > 1)
			sharpFlat = chord.substring(1, 2);

		if (sharpFlat.equals("b") || sharpFlat.equals("#"))
			chord = chord.substring(2);
		else
			chord = chord.substring(1);

		if (clean)
			return cleanChord(chord);
		else
			return chord;
	}

	public String cleanChord(String chord) {
		String regex = Utils.arrayToStringRegex(Chords.SYMBOLS, "|");
		chord = chord.replaceAll(chord.replaceAll(regex, ""), "");
		return chord;
	}

	// min maj aug dim
	private boolean isMinor(String chord) {
		return chord.replace("dim", "").replace("dom", "").replace("maj", "").contains("m") || chord.contains("min")
				|| getSymbols(chord).matches("-.*");
	}

	private boolean isMajor(String chord) {
		return chord.contains("M") || chord.contains("maj");
	}

	private boolean isAugmented(String chord) {
		return getSymbols(chord).matches("(\\+|aug).*");
	}

	private boolean isDiminished(String chord) {
		chord = chord.replace("dom", "");
		return !isHalfDiminished(chord)
				&& (chord.contains("o") || chord.contains("°") || getSymbols(chord).matches("dim.*"));
	}

	private boolean isHalfDiminished(String chord) {
		return chord.contains("Ø") || chord.contains("ø") || chord.contains("half")
				|| (chord.contains("om7") && !chord.contains("dom7"));
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
	private boolean isJustDiminishedFifth(String chord) {
		return chord.matches("(?s).*([0-9]+)(dim|m|-|b)5.*") || getSymbols(chord).matches("(?s).*((-|b)5).*");
	}

	private boolean isFlatSixth(String chord) {
		return chord.matches("(?s).*(-|b)6.*");
	}

	private boolean isFlatNinth(String chord) {
		return getSymbols(chord).matches("\\S+(-|b|dim)9.*") || chord.matches("(?s).*add\\(?m2.*")
				|| getSymbols(chord).contains("b9");
	}

	private boolean isDiminishedEleventh(String chord) {
		return getSymbols(chord).matches("\\S+(-|b|dim)11.*") || getSymbols(chord).contains("b11");
	}

	private boolean isDiminishedThirteenth(String chord) {
		return getSymbols(chord).matches("\\S+(-|b|dim)13.*") || getSymbols(chord).contains("b13");
	}

	// sharp/augmented
	private boolean isSharpSixth(String chord) {
		return chord.matches("(?s).*(+|#)6.*");
	}

	private boolean isAugmentedFifth(String chord) {
		return chord.matches("(?s).*([0-9]+)(\\+|#|aug)5.*") || getSymbols(chord).matches("(?s).*(\\+|#)5.*");
	}

	private boolean isSharpNinth(String chord) {
		return getSymbols(chord).matches("\\S+(\\+|#|aug)9.*") || getSymbols(chord).contains("#9");
	}

	private boolean isSharpEleventh(String chord) {
		return getSymbols(chord).matches("\\S+(\\+|#|aug)11.*") || getSymbols(chord).contains("#11");
	}

	// ref
	// https://www.scales-chords.com/chord
	// http://jguitar.com/
	// http://scottdavies.net/chords_and_scales/music.html
	// http://members.jamplay.com/teaching-tools/chord-library/family/c?
	// https://chord-c.com/guitar-chord/
}
