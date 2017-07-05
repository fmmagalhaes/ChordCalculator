
public class UnknownNoteException extends Exception {

	public UnknownNoteException(){
		super("Unknown note. Notes: C D E F G A B (b|#)");
	}
}
