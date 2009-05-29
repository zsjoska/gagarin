package ro.gagarin.jdbc.objects;

import java.util.HashSet;
import java.util.Set;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class DBUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>();

	public DBUserRole(UserRole role) {
		super.setId(role.getId());
		this.roleName = role.getRoleName();
	}

	public DBUserRole() {
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

	public Set<UserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(Set<UserPermission> permissions) {
		this.userPermissions = permissions;
	}

}
