package ro.gagarin.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class DBUser extends BaseEntity implements User {

	private static final long serialVersionUID = 4384614532696714328L;

	private String username;
	private String password;
	private String name;
	private DBUserRole role;

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
	public long getId() {
		return super.getId();
	}

	public void setRole(UserRole role) {
		if (role instanceof DBUserRole) {
			this.role = (DBUserRole) role;

		} else {
			this.role = new DBUserRole(role);
		}
	}

	public void setRole(DBUserRole role) {
		this.role = role;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "ROLE_ID", nullable = false, updatable = true)
	public DBUserRole getRole() {
		return role;
	}
}
