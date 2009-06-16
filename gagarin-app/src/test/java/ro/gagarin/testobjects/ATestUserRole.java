package ro.gagarin.testobjects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserRole;

public class ATestUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;

	public ATestUserRole(UserRole role) {
		super.setId(role.getId());
		this.roleName = role.getRoleName();
	}

	public ATestUserRole() {
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}
}
