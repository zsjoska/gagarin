package ro.gagarin.hibernate.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

@Entity
@Table(name = "USERS")
public class DBUser extends BaseEntity implements User {

	private static final long serialVersionUID = 4384614532696714328L;

	private String username;
	private String password;
	private String name;
	private UserRole role;

	public DBUser(User user) {
		super.setId(user.getId());
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.name = user.getName();
	}

	public DBUser() {
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Id
	public Long getId() {
		return super.getId();
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@ManyToOne(optional = false, targetEntity = DBUserRole.class)
	@JoinColumn(name = "ROLE_ID", nullable = false, updatable = true)
	public UserRole getRole() {
		return role;
	}
}
