package ro.gagarin.ws.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;

public class WSConversionUtils {

	public static List<WSUserPermission> convertToWSPermissionList(
			Collection<UserPermission> allPermissions) {
		ArrayList<WSUserPermission> list = new ArrayList<WSUserPermission>();
		for (UserPermission userPermission : allPermissions) {
			list.add(new WSUserPermission(userPermission));
		}
		return list;
	}

	public static List<WSUser> convertToWSUserList(Collection<User> allUsers) {
		ArrayList<WSUser> list = new ArrayList<WSUser>();
		for (User user : allUsers) {
			list.add(new WSUser(user));
		}
		return list;
	}

}
