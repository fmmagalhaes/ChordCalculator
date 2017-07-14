# ChordCalculator
Allows you to make arithmetic operation with song notes or chords. Based on stringed instruments.

To run the example simply execute  
java Main

'Chord' - Gets chord composition  
(E.g.) 'B' prints '= B Eb F#'.  
This means B Eb and F# form a B chord.

'Chord + n' - Sums Chord with n semitones  
'C + 1' prints 'C#'.  
'B + 4' prints 'Eb'.  
And obviously, 'C + 12' prints 'C'.

# Syntax

```
ChordCalculator calc = new ChordCalculator();  

calc.addSemitones("C#", 3); // returns E  

calc.subtractSemitones("E7", 3); // returns C#7  

calc.getNotes("Bm7"); // returns the list [B, D, F#, A]
calc.getNotes("D6"); // returns the list [D, F#, A, B]  

String[] notes = {"B", "D", "F#", "A"};  
calc.getChord(Arrays.asList(notes)); // returns the list [Bm7, D6] (if possible, first chord's root is first note in the argument. In this case, "Bm7" comes first, because B was the first note)
```
