package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.OwnerTypesEnum;
import ro.gagarin.user.AuthenticationType;
import ro.gagarin.user.User;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.ConversionUtils;

public class WSUser extends BaseEntity implements User {

    private String username;
    private String name;
    private String email;
    private String phone;
    private String password;
    private AuthenticationType authentication;
    private UserStatus status;
    private Long created;

    public WSUser() {
    }

    public WSUser(User user) {
	this.setId(user.getId());
	this.setName(user.getName());
	this.setPassword(user.getPassword());
	this.setUsername(user.getUsername());
	this.setEmail(user.getEmail());
	this.setPhone(user.getPhone());
	this.authentication = user.getAuthentication();
	this.status = user.getStatus();
	this.created = user.getCreated();
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
    public String getUsername() {
	return this.username;
    }

    public WSUser setUsername(String username) {
	this.username = username;
	return this;
    }

    public WSUser setName(String name) {
	this.name = name;
	return this;
    }

    public WSUser setPassword(String password) {
	this.password = password;
	return this;
    }

    public String getEmail() {
	return email;
    }

    public WSUser setEmail(String email) {
	this.email = email;
	return this;
    }

    public String getPhone() {
	return phone;
    }

    public WSUser setPhone(String phone) {
	this.phone = phone;
	return this;
    }

    @Override
    public String toString() {
	return ConversionUtils.user2String(this);
    }

    @Override
    public AuthenticationType getAuthentication() {
	return this.authentication;
    }

    @Override
    public UserStatus getStatus() {
	return this.status;
    }

    public WSUser setAuthentication(AuthenticationType authentication) {
	this.authentication = authentication;
	return this;
    }

    public WSUser setStatus(UserStatus status) {
	this.status = status;
	return this;
    }

    @Override
    public Long getCreated() {
	return this.created;
    }

    public WSUser setCreated(Long created) {
	this.created = created;
	return this;
    }

    @Override
    public OwnerTypesEnum getType() {
	return OwnerTypesEnum.USER;
    }

    @Override
    public String getTitle() {
	return getUsername();
    }

    @Override
    public WSUser setId(Long id) {
	super.setId(id);
	return this;
    }
}
