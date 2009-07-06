package ro.gagarin.utils;

import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class ConversionUtils {

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

}
