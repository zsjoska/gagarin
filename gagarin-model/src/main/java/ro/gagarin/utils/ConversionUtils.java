package ro.gagarin.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;
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
	return user.getClass().getSimpleName() + " [" + "getId()=" + user.getId() + ", getUsername()="
		+ user.getUsername() + ", getEmail()=" + user.getEmail() + ", getName()=" + user.getName()
		+ ", getPhone()=" + user.getPhone() + "]";
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

    public static String controlEntity2String(ControlEntity ce) {
	return ce.getClass().getSimpleName() + "[ id=" + ce.getId() + ", name=" + ce.getName() + ", category="
		+ ce.getCategory() + "]";
    }

    public static String owner2String(Owner owner) {
	return owner.getClass().getSimpleName() + "[ id=" + owner.getId() + ", title=" + owner.getTitle() + ", type="
		+ owner.getType() + "]";

    }

    public static UserPermission findPermission(UserPermission perm, Collection<UserPermission> permissions) {
	for (UserPermission userPermission : permissions) {
	    if (userPermission.getId() != null && perm.getId() != null) {
		if (userPermission.getId().equals(perm.getId())) {
		    return userPermission;
		}
	    }
	    if (userPermission.getPermissionName() != null && perm.getPermissionName() != null) {
		if (userPermission.getPermissionName().equals(perm.getPermissionName())) {
		    return userPermission;
		}
	    }
	}
	return null;
    }
}
