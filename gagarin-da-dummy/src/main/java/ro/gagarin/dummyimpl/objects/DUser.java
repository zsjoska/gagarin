package ro.gagarin.dummyimpl.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class DUser extends BaseEntity implements User {

	private static final long serialVersionUID = 5758900527022073273L;
	private String name;
	private String password;
	private String userName;
	private UserRole role;

	@Override
	public String getName() {

		return this.name;
	}

	@Override
	public String getPassword() {

		return this.password;
	}

	@Override
	public UserRole getRole() {
		return this.role;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

}
