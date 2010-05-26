package ro.gagarin.exceptions;

import ro.gagarin.ControlEntity;
import ro.gagarin.user.PermissionEnum;

public class PermissionDeniedException extends ExceptionBase {

    private static final long serialVersionUID = -6315990986935884611L;

    private final String username;

    private final PermissionEnum[] reqPermissions;

    private final ControlEntity ce;

    public PermissionDeniedException(String username, PermissionEnum[] reqPermissions, ControlEntity ce) {
	super(ErrorCodes.PERMISSION_DENIED, username + " does not have permission " + reqPermissions + " on " + ce);
	this.username = username;
	this.reqPermissions = reqPermissions;
	this.ce = ce;
    }

    public String getUsername() {
	return username;
    }

    public PermissionEnum[] getReqPermissions() {
	return reqPermissions;
    }

    public ControlEntity getCe() {
	return ce;
    }

}
