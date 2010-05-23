package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.PersonTypesEnum;
import ro.gagarin.user.AuthenticationType;
import ro.gagarin.user.User;
import ro.gagarin.user.UserStatus;
import ro.gagarin.utils.ConversionUtils;

public class WSUser extends BaseEntity implements User {

    private static final long serialVersionUID = -9100286781286495864L;

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

    public void setUsername(String username) {
	this.username = username;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPassword(String password) {
	this.password = password;
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

    @Override
    public AuthenticationType getAuthentication() {
	return this.authentication;
    }

    @Override
    public UserStatus getStatus() {
	return this.status;
    }

    public void setAuthentication(AuthenticationType authentication) {
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
    public PersonTypesEnum getType() {
	return PersonTypesEnum.USER;
    }
}
