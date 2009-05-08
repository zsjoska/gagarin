package ro.gagarin.ws.objects;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class WSUserPermission extends BaseEntity implements UserPermission {

	private static final long serialVersionUID = 1399484581989890777L;

	private String permissionName;
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	public WSUserPermission() {
	}

	public WSUserPermission(UserPermission perm) {
		this.setId(perm.getId());
		this.permissionName = perm.getPermissionName();
		this.userRoles = new HashSet<UserRole>();
		for (Iterator<UserRole> iterator = perm.getUserRoles().iterator(); iterator.hasNext();) {
			UserRole role = iterator.next();
			if (role instanceof WSUserRole) {
				this.userRoles.add((WSUserRole) role);
			} else {
				this.userRoles.add(new WSUserRole(role));
			}
		}
	}

	public Long getId() {
		return super.getId();
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
