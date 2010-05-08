package ro.gagarin.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ro.gagarin.config.ConfigEntry;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.log.LogEntry;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class ConversionUtils {

    public static String group2String(Group group) {
	return group.getClass().getSimpleName() + "[description=" + group.getDescription() + ", name="
		+ group.getName() + ", getId()=" + group.getId() + "]";
    }

    public static String user2String(User user) {
	return user.getClass().getSimpleName() + " [getEmail()=" + user.getEmail() + ", getName()=" + user.getName()
		+ ", getPassword()=" + user.getPassword() + ", getPhone()=" + user.getPhone() + ", getRole()="
		+ user.getRole() + ", getUsername()=" + user.getUsername() + ", getId()=" + user.getId() + "]";
    }

    public static String config2String(ConfigEntry config) {
	return config.getClass().getSimpleName() + " [getConfigName()=" + config.getConfigName()
		+ ", getConfigScope()=" + config.getConfigScope() + ", getConfigValue()=" + config.getConfigValue()
		+ ", getId()=" + config.getId() + "]";
    }

    public static String role2String(UserRole role) {
	return role.getClass().getSimpleName() + " [roleName=" + role.getRoleName() + ", getId()=" + role.getId() + "]";
    }

    public static String perm2String(UserPermission perm) {
	return perm.getClass().getSimpleName() + " [getPermissionName()=" + perm.getPermissionName() + ", getId()="
		+ perm.getId() + "]";
    }

    public static String logEntry2String(LogEntry logEntry) {
	return logEntry.getClass().getSimpleName() + " [getDate()=" + logEntry.getDate() + ", getLogLevel()="
		+ logEntry.getLogLevel() + ", getMessage()=" + logEntry.getMessage() + ", getSessionID()="
		+ logEntry.getSessionID() + ", getUser()=" + logEntry.getUser() + ", getId()=" + logEntry.getId() + "]";
    }

    public static List<UserPermission> matchPermissions(List<UserPermission> allPermissions,
	    UserPermission[] permissions) throws ItemNotFoundException {
	ArrayList<UserPermission> perms = new ArrayList<UserPermission>();
	for (UserPermission reqPermission : permissions) {
	    UserPermission mPerm = null;
	    for (UserPermission aDBPermission : allPermissions) {
		if (aDBPermission.getPermissionName().equalsIgnoreCase(reqPermission.getPermissionName())) {
		    mPerm = aDBPermission;
		    break;
		}
		if (aDBPermission.getId().equals(reqPermission.getId())) {
		    mPerm = aDBPermission;
		    break;
		}
	    }
	    if (mPerm == null)
		throw new ItemNotFoundException(UserPermission.class, reqPermission.getPermissionName());

	    perms.add(mPerm);
	}
	return perms;
    }

    public static HashSet<String> convertPermissionsToStringSet(Collection<? extends UserPermission> permissions) {
	HashSet<String> permSet = new HashSet<String>();
	for (UserPermission userPermission : permissions) {
	    permSet.add(userPermission.getPermissionName());
	}
	return permSet;
    }
}
