package ro.gagarin.hibernate.objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.gagarin.user.BaseEntity;

@Table(name = "ROLE_ASSIGNMENT")
public class DBRoleAssignment extends BaseEntity {

	private static final long serialVersionUID = 7838364614036967973L;
	private DBUserPermission userPermission;
	private DBUserRole role;

	public DBRoleAssignment() {
		super();
	}

	// @Id
	public Long getId() {
		return super.getId();
	}

	public void setUserPermission(DBUserPermission userPermission) {
		this.userPermission = userPermission;
	}

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, updatable = true)
	public DBUserPermission getUserPermission() {
		return userPermission;
	}

	public void setRole(DBUserRole role) {
		this.role = role;
	}

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, updatable = true)
	public DBUserRole getRole() {
		return role;
	}

}
