package ro.gagarin.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.UserPermission;

public class Utils {

    public static Map<ControlEntity, Set<PermissionEnum>> convertUserPermissionSetToPermissionEnumSet(
	    Map<ControlEntity, Set<UserPermission>> effectivePermissions) {
	Map<ControlEntity, Set<PermissionEnum>> permMap = new HashMap<ControlEntity, Set<PermissionEnum>>();
	for (ControlEntity ce : effectivePermissions.keySet()) {
	    Set<UserPermission> set = effectivePermissions.get(ce);
	    Set<PermissionEnum> newSet = convertPermissionSet(set);
	    permMap.put(ce, newSet);
	}
	return permMap;
    }

    public static Set<PermissionEnum> convertPermissionSet(Set<UserPermission> set) {
	Set<PermissionEnum> newSet = new HashSet<PermissionEnum>();
	for (UserPermission userPermission : set) {
	    PermissionEnum perm = PermissionEnum.valueOf(userPermission.getPermissionName());
	    newSet.add(perm);

	}
	return newSet;
    }

}
