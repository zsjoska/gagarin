package ro.gagarin;

import java.util.List;

import ro.gagarin.user.DBUserPermission;
import ro.gagarin.user.DBUserRole;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public interface RoleDAO extends BaseManager {

	DBUserRole getRoleByName(String roleName);

	long createRole(DBUserRole role);

	long createPermission(DBUserPermission perm);

	List<DBUserPermission> getAllPermissions();

	void deleteRole(UserRole role2);

	UserPermission getPermissionByName(String string);

	void deletePermission(UserPermission perm);

	List<DBUserPermission> substractUsersRolePermissions(UserRole main, UserRole substract);

	List<DBUserRole> getAllRoles();

}
