package ro.gagarin.ws.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserRole;

public class WSUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;

	public WSUserRole(UserRole role) {
		this.roleName = role.getRoleName();
		this.setId(role.getId());
	}

	public WSUserRole() {
	}

	public Long getId() {
		return super.getId();
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}
}
