package ro.gagarin.user;

import java.util.Set;

public interface UserRole {

	long getId();

	String getRoleName();

	Set<UserPermission> getUserPermissions();

}