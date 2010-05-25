package ro.gagarin.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.Person;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
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

    /**
     * Assigns a role to a person on a specific object.<br>
     * The person could be a {@link User} or {@link Group}. The object must be
     * something that extends {@link ControlEntity}.
     * 
     * @param role
     * @param group
     * @param object
     * @throws OperationException
     * @throws DataConstraintException
     * @throws ItemNotFoundException
     */
    void assignRoleToPerson(UserRole role, Person person, ControlEntity object) throws OperationException,
	    DataConstraintException, ItemNotFoundException;

    /**
     * Returns a set of permission that the enumerated persons have on the given
     * {@link ControlEntity}.<br>
     * 
     * @param entity
     *            the entity for query the permissions
     * @param persons
     *            a list of persons (user or group)
     * @return the distinct set of user permissions
     * @throws OperationException
     */
    Set<UserPermission> getEffectivePermissionsOnEntity(ControlEntity entity, Person... persons)
	    throws OperationException;

    /**
     * Returns all {@link ControlEntity} objects and their permissions that the
     * given persons have.<br>
     * The result is a map where the key is the control entity and the value is
     * a set of {@link UserPermission}
     * 
     * @param persons
     * @return the effective permissions for all objects where the given persons
     *         have assignments
     * @throws OperationException
     */
    Map<ControlEntity, Set<UserPermission>> getEffectivePermissions(Person... persons) throws OperationException;

    /**
     * Removes all references to this control entity from the assignment table
     * 
     * @param ce
     * @throws DataConstraintException
     * @throws OperationException
     */
    void removeControlEntityFromAssignment(ControlEntity ce) throws OperationException, DataConstraintException;

    /**
     * Removes all references to this person from the assignment table
     * 
     * @param person
     * @throws DataConstraintException
     * @throws OperationException
     */
    void removePersonFromAssignment(Person person) throws OperationException, DataConstraintException;

    /**
     * Returns all control entities for the given entity category.<br>
     * This means all rows from the table given by the <code>categoryEnum</code>
     * parameter.
     * 
     * @param categoryEnum
     * @throws OperationException
     */
    List<ControlEntity> getControlEntityListForCategory(ControlEntityCategory categoryEnum) throws OperationException;
}
