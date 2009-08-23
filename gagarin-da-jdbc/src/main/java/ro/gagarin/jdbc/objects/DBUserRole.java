package ro.gagarin.jdbc.objects;

import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.UserRole;

public class DBUserRole extends BaseEntity implements UserRole {

    private static final long serialVersionUID = -566662791080932756L;

    private String roleName;

    public DBUserRole(UserRole role) {
	super.setId(role.getId());
	this.roleName = role.getRoleName();
    }

    public DBUserRole() {
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    public String getRoleName() {
	return roleName;
    }

    @Override
    public String toString() {
	return "DBUserRole [roleName=" + roleName + ", getId()=" + getId() + "]";
    }
}
