package ro.gagarin.jdbc.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.ConversionUtils;

public class DBUserPermission extends BaseEntity implements UserPermission {

    private static final long serialVersionUID = 1399484581989890777L;

    private String permissionName;

    public DBUserPermission() {
    }

    public DBUserPermission(UserPermission perm) {
	this.setId(perm.getId());
	this.permissionName = perm.getPermissionName();
    }

    public void setPermissionName(String permissionName) {
	this.permissionName = permissionName;
    }

    public String getPermissionName() {
	return permissionName;
    }

    @Override
    public String toString() {
	return ConversionUtils.perm2String(this);
    }
}
