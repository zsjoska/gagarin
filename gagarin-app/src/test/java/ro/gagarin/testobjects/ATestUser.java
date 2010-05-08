package ro.gagarin.testobjects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;

public class ATestUser extends BaseEntity implements User {

    private static final long serialVersionUID = 4384614532696714328L;

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;

    public ATestUser(User user) {
	super.setId(user.getId());
	this.username = user.getUsername();
	this.password = user.getPassword();
	this.name = user.getName();
	this.role = user.getRole();
	this.email = user.getEmail();
	this.phone = user.getPhone();
    }

    public ATestUser() {
    }

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

    public void setRole(UserRole role) {
	this.role = role;
    }

    public UserRole getRole() {
	return role;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getEmail() {
	return email;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getPhone() {
	return phone;
    }

    @Override
    public String toString() {
	return ConversionUtils.user2String(this);
    }

}
