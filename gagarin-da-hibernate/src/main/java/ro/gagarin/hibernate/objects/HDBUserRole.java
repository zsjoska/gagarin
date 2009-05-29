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
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

@Entity
@Table(name = "ROLES")
public class HDBUserRole extends BaseEntity implements UserRole {

	private static final long serialVersionUID = -566662791080932756L;

	private String roleName;
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>();

	public HDBUserRole(UserRole role) {
		super.setId(role.getId());
		this.roleName = role.getRoleName();
	}

	public HDBUserRole() {
	}

	@Id
	public Long getId() {
		return super.getId();
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(nullable = false, unique = true)
	public String getRoleName() {
		return roleName;
	}

	@ManyToMany(mappedBy = "userRoles", cascade = CascadeType.ALL, targetEntity = HDBUserPermission.class)
	public Set<UserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(Set<UserPermission> permissions) {
		this.userPermissions = permissions;
	}

}