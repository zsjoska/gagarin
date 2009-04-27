package ro.gagarin;

import java.util.List;

import ro.gagarin.exceptions.AlreadyExistsException;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public interface RoleManager {

	UserRole getRoleByName(String roleName);

	long createRole(UserRole role) throws AlreadyExistsException;

	long createPermission(UserPermission perm) throws AlreadyExistsException;

	List<UserPermission> getAllPermissions();

	void saveRole(UserRole role) throws AlreadyExistsException;

	void release();

	void deleteRole(UserRole role2);

	UserPermission getPermissionByName(String string);

	void deletePermission(UserPermission perm);

}
