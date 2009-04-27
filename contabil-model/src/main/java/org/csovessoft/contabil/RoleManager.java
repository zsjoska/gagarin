package org.csovessoft.contabil;

import java.util.List;

import org.csovessoft.contabil.exceptions.AlreadyExistsException;
import org.csovessoft.contabil.user.UserPermission;
import org.csovessoft.contabil.user.UserRole;

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
