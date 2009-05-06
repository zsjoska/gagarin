package ro.gagarin.hibernate.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

@Entity
@Table(name = "USER_PERMISSIONS")
public class DBUserPermission extends BaseEntity implements UserPermission {

	private static final long serialVersionUID = 1399484581989890777L;

	private String permissionName;
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	public DBUserPermission() {
	}

	public DBUserPermission(UserPermission perm) {
		this.setId(perm.getId());
		this.permissionName = perm.getPermissionName();
		this.userRoles = perm.getUserRoles();
	}

	@Override
	@Id
	public Long getId() {
		return super.getId();
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@Column(nullable = false, unique = true)
	public String getPermissionName() {
		return permissionName;
	}

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = DBUserRole.class)
	@JoinTable(name = "ROLE_ASSIGNMENT")
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

}
