package org.csovessoft.contabil.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER_PERMISSIONS")
public class UserPermission extends BaseEntity {

	private static final long serialVersionUID = 1399484581989890777L;

	private String permissionName;
	private Set<UserRole> userRoles;

	@Override
	@Id
	public long getId() {
		return super.getId();
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@Column(nullable = false, unique = true)
	public String getPermissionName() {
		return permissionName;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ROLE_ASSIGNMENT")
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

}
