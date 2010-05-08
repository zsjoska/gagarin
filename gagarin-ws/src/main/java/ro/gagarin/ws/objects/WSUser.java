package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;

public class WSUser extends BaseEntity implements User {

    private static final long serialVersionUID = -9100286781286495864L;

    private String username;
    private String name;
    private String email;
    private String phone;
    private String password;
    private WSUserRole role;

    public WSUser() {
    }

    public WSUser(User user) {
	this.setId(user.getId());
	this.setName(user.getName());
	this.setPassword(user.getPassword());
	this.setRole(user.getRole());
	this.setUsername(user.getUsername());
	this.setEmail(user.getEmail());
	this.setPhone(user.getPhone());
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
	if (role != null)
	    this.role = new WSUserRole(role);
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

    @Override
    public String toString() {
	return ConversionUtils.user2String(this);
    }
}
