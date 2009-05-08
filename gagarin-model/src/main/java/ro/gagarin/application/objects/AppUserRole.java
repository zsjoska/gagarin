package ro.gagarin.application.objects;

import java.util.Set;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class AppUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -6987428071490775538L;
	private String roleName;
	private Set<UserPermission> userPermissions;

	@Override
	public String getRoleName() {
		return this.roleName;
	}

	@Override
	public Set<UserPermission> getUserPermissions() {
		return this.userPermissions;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setUserPermissions(Set<UserPermission> userPermissions) {
		this.userPermissions = userPermissions;
	}

}
