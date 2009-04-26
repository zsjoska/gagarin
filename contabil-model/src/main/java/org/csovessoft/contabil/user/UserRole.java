package org.csovessoft.contabil.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class UserRole extends BaseEntity {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;
	private Set<UserPermission> userPermissions;

	@Override
	@Id
	public long getId() {
		return super.getId();
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(nullable = false, unique = true)
	public String getRoleName() {
		return roleName;
	}

	@ManyToMany(mappedBy = "userRoles", cascade = CascadeType.ALL)
	public Set<UserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(Set<UserPermission> permissions) {
		this.userPermissions = permissions;
	}

}
