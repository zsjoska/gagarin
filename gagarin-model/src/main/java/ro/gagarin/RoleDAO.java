package ro.gagarin;

import java.util.List;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public interface RoleDAO extends BaseDAO {

	UserRole getRoleByName(String roleName);

	long createRole(UserRole role);

	long createPermission(UserPermission perm);

	List<UserPermission> getAllPermissions();

	void deleteRole(UserRole role);

	UserPermission getPermissionByName(String string);

	void deletePermission(UserPermission perm);

	List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract);

	List<UserRole> getAllRoles();

	void assignPermissionToRole(UserRole role, UserPermission perm) throws ItemNotFoundException;

}