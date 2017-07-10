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

calc.getComposition("Cadd9"); // returns a list with C E G Bb D  

String[] notes = {"C", "E", "G", "Bb" ,"D"};  
calc.getChordFromComposition(Arrays.asList(notes)); // returns "Cadd9"
```
