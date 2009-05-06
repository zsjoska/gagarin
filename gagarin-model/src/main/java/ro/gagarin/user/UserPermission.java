package ro.gagarin.user;

import java.util.Set;

public interface UserPermission {

	Long getId();

	String getPermissionName();

	Set<UserRole> getUserRoles();

}