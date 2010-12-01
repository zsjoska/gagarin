package ro.gagarin.testobjects;

import ro.gagarin.BaseEntity;
import ro.gagarin.OwnerTypesEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.ConversionUtils;

public class ATestUser extends BaseEntity implements User {

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String authentication;
    private UserStatus status;
    private Long created;

    public ATestUser(User user) {
	super.setId(user.getId());
	this.username = user.getUsername();
	this.password = user.getPassword();
	this.name = user.getName();
	this.email = user.getEmail();
	this.phone = user.getPhone();
	this.authentication = user.getAuthentication();
	this.status = user.getStatus();
	this.created = user.getCreated();
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

    @Override
    public String getAuthentication() {
	return this.authentication;
    }

    @Override
    public UserStatus getStatus() {
	return this.status;
    }

    public void setAuthentication(String authentication) {
	this.authentication = authentication;
    }

    public void setStatus(UserStatus status) {
	this.status = status;
    }

    @Override
    public Long getCreated() {
	return this.created;
    }

    public void setCreated(Long created) {
	this.created = created;
    }

    @Override
    public OwnerTypesEnum getType() {
	return OwnerTypesEnum.USER;
    }

    @Override
    public String getTitle() {
	return this.getUsername();
    }
}
