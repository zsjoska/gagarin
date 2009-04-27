package ro.gagarin.exceptions;


public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = -4190202140346937221L;

	public AlreadyExistsException(String table, String clue) {
	}

	public AlreadyExistsException(String string, String roleName, Exception e) {
		super(e);
	}
}
