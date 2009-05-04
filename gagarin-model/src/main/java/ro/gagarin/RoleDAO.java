package ro.gagarin;

import java.util.List;

import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.hibernate.objects.DBUserRole;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public interface RoleDAO extends BaseDAO {

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
