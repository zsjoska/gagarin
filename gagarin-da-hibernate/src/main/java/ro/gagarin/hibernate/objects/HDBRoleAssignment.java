package ro.gagarin.hibernate.objects;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.gagarin.user.BaseEntity;

@Table(name = "ROLE_ASSIGNMENT")
public class HDBRoleAssignment extends BaseEntity {

	private static final long serialVersionUID = 7838364614036967973L;
	private HDBUserPermission userPermission;
	private HDBUserRole role;

	public HDBRoleAssignment() {
		super();
	}

	// @Id
	public Long getId() {
		return super.getId();
	}

	public void setUserPermission(HDBUserPermission userPermission) {
		this.userPermission = userPermission;
	}

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, updatable = true)
	public HDBUserPermission getUserPermission() {
		return userPermission;
	}

	public void setRole(HDBUserRole role) {
		this.role = role;
	}

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, updatable = true)
	public HDBUserRole getRole() {
		return role;
	}

}
