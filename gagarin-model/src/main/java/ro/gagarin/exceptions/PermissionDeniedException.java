package ro.gagarin.exceptions;

public class PermissionDeniedException extends ExceptionBase {

    private static final long serialVersionUID = -6315990986935884611L;

    private final String username;
    private final String name;

    public PermissionDeniedException(String username, String name) {
	super(ErrorCodes.PERMISSION_DENIED, name);
	this.username = username;
	this.name = name;
    }

    public String getUsername() {
	return username;
    }

    public String getName() {
	return name;
    }

}
