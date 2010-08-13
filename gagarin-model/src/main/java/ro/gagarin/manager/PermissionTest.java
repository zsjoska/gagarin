package ro.gagarin.manager;

import ro.gagarin.ControlEntity;
import ro.gagarin.user.PermissionEnum;

public class PermissionTest {
    private final ControlEntity ce;
    private final PermissionEnum permission;

    public PermissionTest(ControlEntity ce, PermissionEnum permission) {
	this.ce = ce;
	this.permission = permission;
    }

    public ControlEntity getCe() {
	return ce;
    }

    public PermissionEnum getPermission() {
	return permission;
    }
}
