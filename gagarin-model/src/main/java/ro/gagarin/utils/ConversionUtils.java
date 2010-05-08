package ro.gagarin.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

// TODO: replace all toString methods with call to here
public class ConversionUtils {

    public static String group2String(Group group) {
	// TODO Auto-generated method stub
	return null;
    }

    public static String user2String(User user) {
	// TODO Auto-generated method stub
	return user.getUsername();
    }

    public static String role2String(UserRole role) {
	// TODO Auto-generated method stub
	return role.getRoleName();
    }

    public static String perm2String(UserPermission perm) {
	// TODO Auto-generated method stub
	return perm.getPermissionName();
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
