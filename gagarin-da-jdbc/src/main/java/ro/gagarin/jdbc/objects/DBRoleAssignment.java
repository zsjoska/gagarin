package ro.gagarin.jdbc.objects;

import ro.gagarin.user.BaseEntity;

public class DBRoleAssignment extends BaseEntity {

	private static final long serialVersionUID = 7838364614036967973L;
	private DBUserPermission userPermission;
	private DBUserRole role;

	public DBRoleAssignment() {
		super();
	}

	public Long getId() {
		return super.getId();
	}

	public void setUserPermission(DBUserPermission userPermission) {
		this.userPermission = userPermission;
	}

	public DBUserPermission getUserPermission() {
		return userPermission;
	}

	public void setRole(DBUserRole role) {
		this.role = role;
	}

	public DBUserRole getRole() {
		return role;
	}

}
