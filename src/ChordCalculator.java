import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ChordCalculator {

	public static final String[] SYMBOLS = { "4", "5", "6", "7", "8", "9", "M", "m", "add", "maj" };
	public static final String[] NOTES = { "C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B" };
	public static final String[] NOTES_SHARP_FLAT = { "C", "D", "E", "F", "G", "A", "B", "#", "b" };

	private Map<Integer, String> intTonote; // maps 0 to C, 1 to C#, 2 to D, etc
	private Map<String, String> notes;
	private Map<List<String>, String> composition; // maps a list of notes to the chord they form
	//<list, string> and not <string, list> because otherwise it would be necessary
	//to keep <A#, A# D F> and <Bb, A# D F> , etc.

	public ChordCalculator() {
		intTonote = new HashMap<Integer, String>();
		notes = new HashMap<String, String>();
		composition = new HashMap<List<String>, String>();
		fillMaps();
		for (String chord : intTonote.values()) {
			getComposition(chord);
			getComposition(chord + "7");
			getComposition(chord + "m");
			getComposition(chord + "m7");
			getComposition(chord + "M7");
			getComposition(chord + "5");
			getComposition(chord + "9");
		}
	}

	/**
	 * @param chord
	 * @param n
	 * @return the result of adding n semitones to chord
	 */
	public String addSemitones(String chord, int n) {
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
	 */
	public String subtractSemitones(String chord, int n) {
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
	 */
	public String getChordFromComposition(List<String> notes) {
		for (Entry<List<String>, String> entry : composition.entrySet()) {
			if (Utils.equalLists(entry.getKey(), notes))
				return entry.getValue();
		}
		return null;
	}

	/**
	 * @param chord
	 * @return the list of notes which form the chord
	 */
	public List<String> getComposition(String chord) {
		String root = getRootNote(chord);
		int n = (Integer) Utils.getKeyFromValue(intTonote, root);
		List<String> notes_aux = new LinkedList<String>();

		String symbols = getSymbols(chord);
		int[] compositionSemitones = null;

		if (notes.containsKey(chord))
			compositionSemitones = Chords.MAJOR_TRIAD_CHORD;
		else if (isMinor(chord) && isSeventh(chord)){
			compositionSemitones = Chords.MINOR_SEVENTH_CHORD;
			/*String[] symbolsToRemove = {"m", "7"};
			symbols = Utils.removeExpressions(symbols, symbolsToRemove);*/
		}
		else if (isMajor(chord) && isSeventh(chord))
			compositionSemitones = Chords.MAJOR_SEVENTH_CHORD;
		else if (isMinor(chord) && isNinth(chord))
			compositionSemitones = Chords.MINOR_NINTH_CHORD;
		else if (isMajor(chord) && isNinth(chord))
			compositionSemitones = Chords.MAJOR_NINTH_CHORD;
		else if (isMinor(chord))
			compositionSemitones = Chords.MINOR_TRIAD_CHORD;
		else if (isSeventh(chord))
			compositionSemitones = Chords.DOMINANT_SEVENTH_CHORD;
		else if (isAddedNinth(chord))
			compositionSemitones = Chords.ADDED_NINTH_CHORD;
		else if (isNinth(chord))
			compositionSemitones = Chords.DOMINANT_NINTH_CHORD;
		else if (isPowerChord(chord))
			compositionSemitones = Chords.POWER_CHORD;

		for (int semitones : compositionSemitones)
			notes_aux.add(intTonote.get((n + semitones) % 12));
		composition.put(notes_aux, chord);

		return notes_aux;
	}

	private void fillMaps() {
		int i = 0;
		for (String note : NOTES)
			intTonote.put(i++, note);

		for (int note = 0; note < 12; note++) {
			String str = "";
			for (int n = 0; n < 12; n++) {
				if (n != 1 && n != 3 && n != 6 && n != 8 && n != 10 && n != note)
					str += intTonote.get(n) + (Math.abs(note - n + 12)) % 12 + " ";
			}
			notes.put(intTonote.get(note), str.trim());
		}
		notes.put("A#", notes.get("Bb"));
		notes.put("D#", notes.get("Eb"));
		notes.put("Cb", notes.get("B"));
		notes.put("Db", notes.get("C#"));
		notes.put("Fb", notes.get("E"));
		notes.put("Gb", notes.get("F#"));
		notes.put("Ab", notes.get("G#"));
	}

	// returns the root of the given chord
	private String getRootNote(String chord) {
		String root = chord;
		for (String symbol : SYMBOLS)
			root = root.replace(symbol, "");
		return root;
	}

	// returns the symbols of the given chord (ex: M7, 7, m7)
	private String getSymbols(String chord) {
		String symbols = chord;
		for (String note : NOTES_SHARP_FLAT)
			symbols = symbols.replace(note, "");
		return symbols;
	}

	private boolean isPowerChord(String note) {
		return note.contains("5") || note.contains("8");
	}

	private boolean isMinor(String note) {
		return note.contains("m");
	}
	
	private boolean isAddedNinth(String note) {
		return note.contains("add9");
	}

	private boolean isNinth(String note) {
		return note.contains("9");
	}

	private boolean isMajor(String note) {
		return note.contains("M") || note.contains("maj");
	}

	private boolean isSeventh(String note) {
		return note.contains("7");
	}
}
