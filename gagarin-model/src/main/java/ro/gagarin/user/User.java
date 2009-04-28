package ro.gagarin.user;

public class User extends BaseEntity {

	private static final long serialVersionUID = 4384614532696714328L;

	private String username;
	private String password;
	private String name;
	private UserRole role;

	public void setUsername(String username) {
		this.username = username;
	}

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

	public long getId() {
		return super.getId();
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public UserRole getRole() {
		return role;
	}
}
