package ro.gagarin.testobjects;

import java.util.HashSet;
import java.util.Set;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class ATestUserPermission extends BaseEntity implements UserPermission {

	private static final long serialVersionUID = 1399484581989890777L;

	private String permissionName;
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	public ATestUserPermission() {
	}

	public ATestUserPermission(UserPermission perm) {
		this.setId(perm.getId());
		this.permissionName = perm.getPermissionName();
		this.userRoles = perm.getUserRoles();
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

}
