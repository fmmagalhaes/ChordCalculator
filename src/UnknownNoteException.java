
public class UnknownNoteException extends Exception {

	public UnknownNoteException(){
		super("Unknown note. Notes: C(#) D(b|#) E(b) F(#) G(b|#) A(b|#) B(b)");
	}
}
