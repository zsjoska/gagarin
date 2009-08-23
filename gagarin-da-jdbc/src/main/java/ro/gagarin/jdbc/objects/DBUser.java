package ro.gagarin.jdbc.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class DBUser extends BaseEntity implements User {

    private static final long serialVersionUID = 4384614532696714328L;

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private UserRole role;

    public DBUser(User user) {
	super.setId(user.getId());
	this.username = user.getUsername();
	this.password = user.getPassword();
	this.name = user.getName();
	this.email = user.getEmail();
	this.phone = user.getPhone();
	this.role = user.getRole();
    }

    public DBUser() {
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

    public Long getId() {
	return super.getId();
    }

    public void setRole(UserRole role) {
	this.role = role;
    }

    public UserRole getRole() {
	return role;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }
}
