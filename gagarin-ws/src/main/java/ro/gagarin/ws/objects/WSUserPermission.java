package ro.gagarin.ws.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;

public class WSUserPermission extends BaseEntity implements UserPermission {

	private static final long serialVersionUID = 1399484581989890777L;

	private String permissionName;

	public WSUserPermission() {
	}

	public WSUserPermission(UserPermission perm) {
		this.setId(perm.getId());
		this.permissionName = perm.getPermissionName();
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionName() {
		return permissionName;
	}

}
