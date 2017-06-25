import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ChordCalculator {

	private Map<Integer, String> intTonote; // maps 0 to C, 1 to C#, 2 to D, etc
	private Map<List<String>, String> composition; // maps a list of notes to the chord they form
	//<list, string> and not <string, list> because otherwise it would be necessary
	//to keep <A#, A# D F> and <Bb, A# D F> , etc.

	/**
	 * Constructs a ChordCalculator which allows a set of operations
	 */
	public ChordCalculator() {
		intTonote = new HashMap<Integer, String>();
		composition = new HashMap<List<String>, String>();
		fillMaps();
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

		//String symbols = getSymbols(chord);
		int[] compositionSemitones = null;

		if (Utils.arrayContains(Chords.NOTES, chord))
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
		else if (isMajor(chord) && isEleventh(chord))
			compositionSemitones = Chords.MAJOR_ELEVENTH_CHORD;
		else if (isMinor(chord) && isEleventh(chord))
			compositionSemitones = Chords.MINOR_ELEVENTH_CHORD;
		else if (isMinor(chord))
			compositionSemitones = Chords.MINOR_TRIAD_CHORD;
		else if (isSeventh(chord))
			compositionSemitones = Chords.DOMINANT_SEVENTH_CHORD;
		else if (isNinth(chord))
			compositionSemitones = Chords.DOMINANT_NINTH_CHORD;
		else if (isAddedNinth(chord))
			compositionSemitones = Chords.ADDED_NINTH_CHORD;
		else if (isEleventh(chord))
			compositionSemitones = Chords.DOMINANT_ELEVENTH_CHORD;
		else if (isPowerChord(chord))
			compositionSemitones = Chords.POWER_CHORD;

		for (int semitones : compositionSemitones)
			notes_aux.add(intTonote.get((n + semitones) % 12));
		composition.put(notes_aux, chord);

		return notes_aux;
	}

	private void fillMaps() {
		int i = 0;
		for (String note : Chords.NOTES)
			intTonote.put(i++, note);
		
		for (String chord : intTonote.values()) {
			getComposition(chord);
			getComposition(chord + "m");
			
			getComposition(chord + "7");
			getComposition(chord + "m7");
			getComposition(chord + "M7");
			
			getComposition(chord + "5");
			
			getComposition(chord + "9");
			getComposition(chord + "m9");
			getComposition(chord + "M9");
			getComposition(chord + "add9");
			
			getComposition(chord + "11");
			getComposition(chord + "m11");
			getComposition(chord + "M11");
		}
	}

	// returns the root of the given chord
	private String getRootNote(String chord) {
		String root = chord;
		for (String symbol : Chords.SYMBOLS)
			root = root.replace(symbol, "");
		return root;
	}

	// returns the symbols of the given chord (ex: M7, 7, m7)
	private String getSymbols(String chord) {
		String symbols = chord;
		for (String note : Chords.NOTES_SHARP_FLAT)
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

	private boolean isEleventh(String note){
		return note.contains("11");
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
