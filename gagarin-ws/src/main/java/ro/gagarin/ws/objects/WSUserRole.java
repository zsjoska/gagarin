package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.ConversionUtils;

public class WSUserRole extends BaseEntity implements UserRole {

    private static final long serialVersionUID = -566662791080932756L;

    private String roleName;

    public WSUserRole(UserRole role) {
	this.roleName = role.getRoleName();
	this.setId(role.getId());
    }

    public WSUserRole() {
    }

    public WSUserRole(String roleName) {
	setRoleName(roleName);
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
