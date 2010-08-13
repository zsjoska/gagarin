package ro.gagarin.exceptions;

import ro.gagarin.ControlEntity;
import ro.gagarin.user.PermissionEnum;

public class PermissionDeniedException extends ExceptionBase {

    private static final long serialVersionUID = -6315990986935884611L;

    private final String username;

    private final PermissionEnum[] reqPermissions;

    private final ControlEntity ce;

    public PermissionDeniedException(String username, PermissionEnum[] reqPermissions, ControlEntity ce) {
	super(ErrorCodes.PERMISSION_DENIED, createMessage(username, reqPermissions, ce));
	this.username = username;
	this.reqPermissions = reqPermissions;
	this.ce = ce;
    }

    private static String createMessage(String name, PermissionEnum[] perms, ControlEntity ce) {
	StringBuilder sb = new StringBuilder();
	sb.append(name);
	sb.append(" does not have any permission of [");
	for (PermissionEnum perm : perms) {
	    sb.append(perm.toString());
	    sb.append(", ");
	}
	sb.delete(sb.length() - 2, sb.length());
	sb.append("] on ");
	sb.append(ce);
	return sb.toString();
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
