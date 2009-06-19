package ro.gagarin.application.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserRole;

public class AppUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -6987428071490775538L;
	private String roleName;

	public AppUserRole(UserRole role) {
		this.setId(role.getId());
		this.roleName = role.getRoleName();
	}

	public AppUserRole() {
	}

	@Override
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
