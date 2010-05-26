package ro.gagarin.application.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;

public class AppUserRole extends BaseEntity implements UserRole {

    private String roleName;

    public AppUserRole(UserRole role) {
	this.setId(role.getId());
	this.roleName = role.getRoleName();
    }

    public AppUserRole() {
    }

    @Override
    public String getRoleName() {
	return this.roleName;
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    @Override
    public String toString() {
	return ConversionUtils.role2String(this);
    }
}
