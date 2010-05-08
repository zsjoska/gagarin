package ro.gagarin.application.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;

public class AppUser extends BaseEntity implements User {

    private static final long serialVersionUID = 5758900527022073273L;
    private String name;
    private String email;

    private String phone;
    private String password;
    private String username;
    private UserRole role;

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
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
    public UserRole getRole() {
	return this.role;
    }

    @Override
    public String getUsername() {
	return this.username;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setUsername(String userName) {
	this.username = userName;
    }

    public void setRole(UserRole role) {
	this.role = role;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    @Override
    public String toString() {
	return ConversionUtils.user2String(this);
    }
}
