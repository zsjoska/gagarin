package ro.gagarin;

import java.util.List;
import java.util.Set;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public interface RoleDAO extends BaseDAO {

    UserRole getRoleByName(String roleName) throws OperationException;

    long createRole(UserRole role) throws DataConstraintException, OperationException;

    long createPermission(UserPermission perm) throws DataConstraintException, OperationException;

    List<UserPermission> getAllPermissions() throws OperationException;

    void deleteRole(UserRole role) throws OperationException, ItemNotFoundException;

    UserPermission getPermissionByName(String string) throws OperationException;

    void deletePermission(UserPermission perm) throws OperationException, ItemNotFoundException;

    List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) throws OperationException,
	    ItemNotFoundException;

    List<UserRole> getAllRoles() throws OperationException;

    void assignPermissionToRole(UserRole role, UserPermission perm) throws ItemNotFoundException, OperationException;

    Set<UserPermission> getRolePermissions(UserRole role) throws OperationException, ItemNotFoundException;

    Set<UserRole> getPermissionRoles(UserPermission perm) throws OperationException, ItemNotFoundException;

    UserRole completeRoleId(UserRole role) throws OperationException, ItemNotFoundException;

    UserPermission completePermissionId(UserPermission perm) throws OperationException, ItemNotFoundException;

}
