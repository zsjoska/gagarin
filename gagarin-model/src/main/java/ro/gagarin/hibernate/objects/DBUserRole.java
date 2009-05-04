package ro.gagarin.hibernate.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserRole;

@Entity
@Table(name = "ROLES")
public class DBUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;
	private Set<DBUserPermission> userPermissions = new HashSet<DBUserPermission>();

	public DBUserRole(UserRole role) {
		this.roleName = role.getRoleName();
		// this.userPermissions = role.getUserPermissions();
		this.setId(role.getId());
	}

	public DBUserRole() {
	}

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
	public Set<DBUserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(Set<DBUserPermission> permissions) {
		this.userPermissions = permissions;
	}

}
