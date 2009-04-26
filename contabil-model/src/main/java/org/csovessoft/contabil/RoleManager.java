package org.csovessoft.contabil;

import java.util.List;

import org.csovessoft.contabil.exceptions.AlreadyExistsException;
import org.csovessoft.contabil.user.UserPermission;
import org.csovessoft.contabil.user.UserRole;

public interface RoleManager {

	UserRole getAdminRole(String adminRoleName);

	long createRole(UserRole role) throws AlreadyExistsException;

	long createPermission(UserPermission perm) throws AlreadyExistsException;

	List<UserPermission> getAllPermissions();

	void saveRole(UserRole adminRole) throws AlreadyExistsException;

}
