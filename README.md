# ChordCalculator
Java music calculator for operations with notes and chords.  
ChordCalculator can transpose notes and chords (by summing and subtracting half steps). It can also list the group of notes in a chord and identify which chords are formed by a list of notes.

To run the example simply run  
java Main

**'Chord+n'** - Adds n semitones (half steps) to 'Chord'   
'C+1' prints 'C#'.  
'B+4' prints 'Eb'.  
And obviously, 'C+12' prints 'C'.

**'Chord-n'** - Subtracts n semitones  
'F#-1' prints 'F'. 

**'Chord'** - Gets 'Chord' composition  
'Bm7' prints '= [B, D, F#, A]'.  
This means B, D, A and F# form a Bm7 chord.

**'Note1 Note2 Note3...'** - Gets chords formed by Note1, Note2, Note3...  
'B D F# A' prints '[Bm7, D6/B]'


# Syntax

```java
ChordCalculator calc = new ChordCalculator();  

calc.addSemitones("C#", 3); // returns E  

calc.subtractSemitones("E7", 3); // returns C#7  

calc.getNotes("Bm7"); // returns the list [B, D, F#, A]
calc.getNotes("D6"); // returns the list [D, F#, A, B]  

String[] notes = {"B", "D", "F#", "A"};  
calc.getChord(Arrays.asList(notes)); // returns the list [Bm7, D6/B] (if possible, first chord's root is first note in the argument. In this case, "Bm7" comes first, because B was the first note)
```
