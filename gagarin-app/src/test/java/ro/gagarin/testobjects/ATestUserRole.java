package ro.gagarin.testobjects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;

public class ATestUserRole extends BaseEntity implements UserRole {

    private String roleName;

    public ATestUserRole(UserRole role) {
	super.setId(role.getId());
	this.roleName = role.getRoleName();
    }

    public ATestUserRole() {
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    public String getRoleName() {
	return roleName;
    }

    @Override
    public String toString() {
	return ConversionUtils.role2String(this);
    }
}
