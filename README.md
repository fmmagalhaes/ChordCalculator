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

# How to use in your code

ChordCalculator calc = new ChordCalculator();  

calc.addSemitones("C#", 3); //will return E  

calc.subtractSemitones("E7", 3); //will return C#7  

calc.getComposition("Cadd9"); //will return a list with C E G Bb D  

String[] notes = {"C", "E", "G", "Bb" ,"D"};  
calc.getChordFromComposition(Arrays.asList(notes)); //will return "Cadd9"
