package ro.gagarin.user;

import java.util.HashSet;
import java.util.Set;

public class UserRole extends BaseEntity {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>();

	@Override
	public long getId() {
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
