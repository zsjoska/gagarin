package ro.gagarin.exceptions;

public class PermissionDeniedException extends Exception {

    private static final long serialVersionUID = -6315990986935884611L;

    private final String username;
    private final String name;

    public PermissionDeniedException(String username, String name) {
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
