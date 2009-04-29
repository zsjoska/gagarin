package ro.gagarin.user;

import java.util.HashSet;
import java.util.Iterator;
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
public class DBUserPermission extends BaseEntity implements UserPermission {

	private static final long serialVersionUID = 1399484581989890777L;

	private String permissionName;
	private Set<DBUserRole> userRoles = new HashSet<DBUserRole>();

	public DBUserPermission() {
	}

	public DBUserPermission(UserPermission perm) {
		this.setId(perm.getId());
		this.permissionName = perm.getPermissionName();
		this.userRoles = new HashSet<DBUserRole>();
		for (Iterator<? extends UserRole> iterator = perm.getUserRoles().iterator(); iterator
				.hasNext();) {
			UserRole role = iterator.next();
			if (role instanceof DBUserRole) {
				this.userRoles.add((DBUserRole) role);
			} else {
				this.userRoles.add(new DBUserRole(role));
			}
		}
	}

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
	public Set<DBUserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<DBUserRole> userRoles) {
		this.userRoles = userRoles;
	}

}
