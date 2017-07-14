# ChordCalculator
Allows you to make arithmetic operation with song notes or chords. Based on stringed instruments.

To run the example simply execute  
java Main

'Chord' - Gets chord composition  
'Bm7' prints '= B D F# A'.  
This means B, D, A and F# form a Bm7 chord.

'Note1 Note2 Note3...' - Gets chords formed by Note1, Note2, Note3...  
'B D F# A' prints '[Bm7, D6/B]'

'Chord + n' - Sums Chord with n semitones (half steps)  
'C+1' prints 'C#'.  
'B+4' prints 'Eb'.  
And obviously, 'C+12' prints 'C'.

'Chord + n' - Subtracts n semitones  
'F#-1' prints 'F'. 


# Syntax

```
ChordCalculator calc = new ChordCalculator();  

calc.addSemitones("C#", 3); // returns E  

calc.subtractSemitones("E7", 3); // returns C#7  

calc.getNotes("Bm7"); // returns the list [B, D, F#, A]
calc.getNotes("D6"); // returns the list [D, F#, A, B]  

String[] notes = {"B", "D", "F#", "A"};  
calc.getChord(Arrays.asList(notes)); // returns the list [Bm7, D6/B] (if possible, first chord's root is first note in the argument. In this case, "Bm7" comes first, because B was the first note)
```