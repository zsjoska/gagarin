package ro.gagarin.user;

import java.util.Set;

public interface UserRole {

	Long getId();

	String getRoleName();

	Set<UserPermission> getUserPermissions();

}