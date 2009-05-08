package ro.gagarin.ws.objects;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class WSUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;

	private Set<UserPermission> userPermissions = new HashSet<UserPermission>();

	public WSUserRole(UserRole role) {
		this.roleName = role.getRoleName();
		this.setId(role.getId());
		this.setUserPermissions(role.getUserPermissions());
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

	@XmlElement(type = WSUserPermission.class)
	public Set<UserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(Set<UserPermission> permissions) {
		this.userPermissions = permissions;
	}

}
