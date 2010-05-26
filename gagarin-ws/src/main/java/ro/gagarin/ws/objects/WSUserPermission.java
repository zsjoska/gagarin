package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.ConversionUtils;

public class WSUserPermission extends BaseEntity implements UserPermission {

    private String permissionName;

    public WSUserPermission() {
    }

    public WSUserPermission(UserPermission perm) {
	this.setId(perm.getId());
	this.permissionName = perm.getPermissionName();
    }

    public WSUserPermission(String string) {
	this.setPermissionName(string);
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
