package ro.gagarin.ws.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class WSUser extends BaseEntity implements User {

	private static final long serialVersionUID = -9100286781286495864L;

	private String username;
	private String name;
	private String password;
	private WSUserRole role;

	public WSUser() {
	}

	public WSUser(User user) {

	}

	@Override
	public Long getId() {
		return super.getId();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public WSUserRole getRole() {
		return this.role;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(WSUserRole role) {
		this.role = role;
	}

	public void setRole(UserRole role) {
		if (role instanceof WSUserRole) {
			this.role = (WSUserRole) role;

		} else {
			this.role = new WSUserRole(role);
		}
	}
}
