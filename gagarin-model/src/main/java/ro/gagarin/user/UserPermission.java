package ro.gagarin.user;

import java.util.Set;

public interface UserPermission {

	long getId();

	String getPermissionName();

	Set<UserRole> getUserRoles();

}