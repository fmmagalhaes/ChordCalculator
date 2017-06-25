import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {

		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader bis = new BufferedReader(is);

		ChordCalculator calc = new ChordCalculator();

		System.out.println("usage: 'Chord' or 'Chord+n'");
		System.out.println("example: 'C#m+3'");
		String line = "";
		while ((line = bis.readLine()) != null) {
			line = line.trim();
			String chordMatch = "[a-zA-Z](#|[a-zA-Z])*([0-9])?(M|m)?";
			if (line.matches((chordMatch + "( +)?\\+( +)?[0-9]([0-9])?"))) {
				String[] parts = line.split("\\+");
				String chord = parts[0];
				int n = Integer.parseInt(parts[1]);
				String newchord = calc.addSemitones(chord, n);
				System.out.println(newchord + "\n");
			} else if (line.matches((chordMatch))) {
				String chord = line;
				List<String> list = calc.getComposition(chord);
				String composition = "";
				for (String note : list)
					composition += note + " ";
				System.out.println("= " + composition + "\n");
			} else {
				System.out.println("usage: 'Chord =' or 'Chord + n'\n");
			}
		}
	}
}