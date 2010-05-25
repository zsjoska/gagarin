package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.perm2String;
import static ro.gagarin.utils.ConversionUtils.role2String;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.Person;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.jdbc.role.AssignPermissionToRoleSQL;
import ro.gagarin.jdbc.role.AssignRoleToPersonSQL;
import ro.gagarin.jdbc.role.CreatePermissionSQL;
import ro.gagarin.jdbc.role.CreateRoleSQL;
import ro.gagarin.jdbc.role.DeletePermissionSQL;
import ro.gagarin.jdbc.role.DeleteRoleSQL;
import ro.gagarin.jdbc.role.GetControlEntityByIdAndCategorySQL;
import ro.gagarin.jdbc.role.GetControlEntityListForCategorySQL;
import ro.gagarin.jdbc.role.GetEffectivePermissionsOnEntitySQL;
import ro.gagarin.jdbc.role.GetEffectivePermissionsSQL;
import ro.gagarin.jdbc.role.GetPermissionRolesSQL;
import ro.gagarin.jdbc.role.GetRolePermissionsSQL;
import ro.gagarin.jdbc.role.RemoveControlEntityFromAssignmentSQL;
import ro.gagarin.jdbc.role.RemovePersonFromAssignmentSQL;
import ro.gagarin.jdbc.role.SelectPermissionByNameSQL;
import ro.gagarin.jdbc.role.SelectPermissionsSQL;
import ro.gagarin.jdbc.role.SelectRoleByNameSQL;
import ro.gagarin.jdbc.role.SelectRolesSQL;
import ro.gagarin.jdbc.role.SubstractRolesPermissions;
import ro.gagarin.jdbc.role.UnAssignRoleFromPersonSQL;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.AppLogAction;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class JdbcRoleDAO extends BaseJdbcDAO implements RoleDAO {

    public JdbcRoleDAO(Session session) throws OperationException {
	super(session);
    }

    @Override
    public UserRole completeRoleId(UserRole role) throws OperationException, ItemNotFoundException {
	UserRole r = role;
	if (r.getId() == null && r.getRoleName() != null) {
	    r = SelectRoleByNameSQL.execute(this, r.getRoleName());
	    if (r == null) {
		throw new ItemNotFoundException(UserRole.class, role.getRoleName());
	    }
	}
	return r;
    }

    @Override
    public UserPermission completePermissionId(UserPermission perm) throws OperationException, ItemNotFoundException {
	UserPermission permission = perm;
	if (permission.getId() == null && permission.getPermissionName() != null) {
	    permission = SelectPermissionByNameSQL.execute(this, permission.getPermissionName());
	    if (permission == null) {
		throw new ItemNotFoundException(UserPermission.class, perm.getPermissionName());
	    }
	}
	return permission;
    }

    @Override
    public UserRole getRoleByName(String roleName) throws OperationException {

	UserRole role = SelectRoleByNameSQL.execute(this, roleName);
	return role;

    }

    @Override
    public long createRole(UserRole role) throws DataConstraintException, OperationException {

	try {

	    DBUserRole dbUserRole = new DBUserRole(role);
	    dbUserRole.setId(DBUserRole.getNextId());

	    new CreateRoleSQL(this, dbUserRole).execute();

	    APPLOG.action(AppLogAction.CREATE, UserRole.class, role.getRoleName(), AppLog.SUCCESS);
	    APPLOG.info("Role " + role.getRoleName() + " was created");
	    return dbUserRole.getId();
	} catch (OperationException e) {
	    APPLOG.error("Could not create role:" + role2String(role), e);
	    APPLOG.action(AppLogAction.CREATE, UserRole.class, role.getRoleName(), AppLog.FAILED);
	    throw e;
	}
    }

    @Override
    public long createPermission(UserPermission perm) throws DataConstraintException, OperationException {

	try {

	    DBUserPermission dbUserPermission = new DBUserPermission(perm);
	    dbUserPermission.setId(DBUserPermission.getNextId());

	    new CreatePermissionSQL(this, dbUserPermission).execute();

	    APPLOG.action(AppLogAction.CREATE, UserPermission.class, perm.getPermissionName(), AppLog.SUCCESS);
	    APPLOG.info("Permission " + perm.getPermissionName() + " was created");
	    return dbUserPermission.getId();
	} catch (OperationException e) {
	    APPLOG.error("Could not create permission:" + perm2String(perm), e);
	    APPLOG.action(AppLogAction.CREATE, UserPermission.class, perm.getPermissionName(), AppLog.FAILED);
	    throw e;
	}
    }

    @Override
    public List<UserPermission> getAllPermissions() throws OperationException {

	List<UserPermission> permissions;
	permissions = SelectPermissionsSQL.execute(this);
	return permissions;
    }

    @Override
    public void deleteRole(UserRole role) throws OperationException, ItemNotFoundException {

	UserRole r = completeRoleId(role);
	try {

	    new DeleteRoleSQL(this, r).execute();

	    APPLOG.action(AppLogAction.DELETE, UserRole.class, r.getRoleName(), AppLog.SUCCESS);
	    APPLOG.info("Role " + r.getRoleName() + " was deleted");

	} catch (OperationException e) {
	    APPLOG.error("Could not delete role:" + role2String(r), e);
	    APPLOG.action(AppLogAction.DELETE, UserRole.class, r.getRoleName(), AppLog.FAILED);
	    throw e;
	} catch (DataConstraintException e) {
	    APPLOG.error("Could not delete role:" + role2String(r), e);
	    APPLOG.action(AppLogAction.DELETE, UserRole.class, r.getRoleName(), AppLog.FAILED);
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}
    }

    @Override
    public UserPermission getPermissionByName(String permissionName) throws OperationException {
	return SelectPermissionByNameSQL.execute(this, permissionName);
    }

    // TODO:(3) Think again: why we would delete a permission;
    // it is recreated at application startup
    // What should happen on application upgrade when a permission is removed
    @Override
    public void deletePermission(UserPermission perm) throws OperationException, ItemNotFoundException {
	UserPermission permission = completePermissionId(perm);
	try {

	    new DeletePermissionSQL(this, permission).execute();

	    APPLOG.action(AppLogAction.DELETE, UserPermission.class, permission.getPermissionName(), AppLog.SUCCESS);
	    APPLOG.info("Permission " + permission.getPermissionName() + " was deleted");

	} catch (OperationException e) {
	    APPLOG.error("Could not delete permission:" + perm2String(permission), e);
	    APPLOG.action(AppLogAction.DELETE, UserPermission.class, permission.getPermissionName(), AppLog.FAILED);
	    throw e;
	} catch (DataConstraintException e) {
	    APPLOG.error("Could not delete permission:" + perm2String(permission), e);
	    APPLOG.action(AppLogAction.DELETE, UserPermission.class, permission.getPermissionName(), AppLog.FAILED);
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}
    }

    @Override
    public List<UserRole> getAllRoles() throws OperationException {
	return SelectRolesSQL.execute(this);
    }

    // TODO:(4) This is not used since the new permission framework
    @Override
    public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract)
	    throws OperationException, ItemNotFoundException {
	UserRole m = completeRoleId(main);
	UserRole s = completeRoleId(substract);
	return SubstractRolesPermissions.execute(this, m, s);
    }

    @Override
    public void assignPermissionToRole(UserRole role, UserPermission perm) throws ItemNotFoundException,
	    OperationException {

	// TODO:(1) move the checks to a method inside of execute
	// the markRollback could be forgotten this way
	if (role == null) {
	    markRollback();
	    throw new ItemNotFoundException(UserRole.class, "null");
	}
	if (perm == null) {
	    markRollback();
	    throw new ItemNotFoundException(UserPermission.class, "null");
	}

	UserRole r = completeRoleId(role);
	UserPermission p = completePermissionId(perm);

	try {
	    new AssignPermissionToRoleSQL(this, r, p).execute();
	} catch (OperationException e) {
	    throw e;
	} catch (DataConstraintException e) {
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}

    }

    @Override
    public Set<UserPermission> getRolePermissions(UserRole role) throws OperationException, ItemNotFoundException {
	UserRole foundRole = completeRoleId(role);
	return GetRolePermissionsSQL.execute(this, foundRole);
    }

    @Override
    public Set<UserRole> getPermissionRoles(UserPermission perm) throws OperationException, ItemNotFoundException {
	UserPermission permission = completePermissionId(perm);
	return GetPermissionRolesSQL.execute(this, permission);
    }

    @Override
    public void assignRoleToPerson(UserRole role, Person person, ControlEntity object) throws OperationException,
	    DataConstraintException, ItemNotFoundException {

	UserRole completeRole = completeRoleId(role);

	new AssignRoleToPersonSQL(this, completeRole, person, object).execute();
    }

    @Override
    public void unAssignRoleFromPerson(UserRole role, Person person, ControlEntity ce) throws OperationException,
	    DataConstraintException, ItemNotFoundException {
	UserRole completeRole = completeRoleId(role);

	new UnAssignRoleFromPersonSQL(this, completeRole, person, ce).execute();
    }

    @Override
    public Set<UserPermission> getEffectivePermissionsOnEntity(ControlEntity entity, Person... persons)
	    throws OperationException {
	GetEffectivePermissionsOnEntitySQL cmd = new GetEffectivePermissionsOnEntitySQL(this, entity, persons);
	cmd.execute();
	return cmd.getPermissions();
    }

    @Override
    public Map<ControlEntity, Set<UserPermission>> getEffectivePermissions(Person... persons) throws OperationException {
	Map<ControlEntity, Set<UserPermission>> effectivePermissions = GetEffectivePermissionsSQL
		.execute(this, persons);

	// at this point, all control entities are missing the name field
	// the only way to figure it out is to do additional queries
	// this would be nice to have cached

	Map<ControlEntity, Set<UserPermission>> newMap = new HashMap<ControlEntity, Set<UserPermission>>();
	for (ControlEntity ce : effectivePermissions.keySet()) {
	    if (ce.getCategory().table() != null) {
		ControlEntity completeCe = GetControlEntityByIdAndCategorySQL.execute(this, ce.getId(), ce
			.getCategory());
		if (completeCe == null) {
		    APPLOG.error("Inconsistent control entity found. Cleanup was not done properly. Id=" + ce.getId()
			    + " Category = " + ce.getCategory());
		} else {
		    newMap.put(completeCe, effectivePermissions.get(ce));
		}
	    } else {

	    }
	}
	return newMap;
    }

    @Override
    public void removeControlEntityFromAssignment(ControlEntity ce) throws OperationException, DataConstraintException {
	new RemoveControlEntityFromAssignmentSQL(this, ce).execute();

    }

    @Override
    public void removePersonFromAssignment(Person person) throws OperationException, DataConstraintException {
	new RemovePersonFromAssignmentSQL(this, person).execute();
    }

    @Override
    public List<ControlEntity> getControlEntityListForCategory(ControlEntityCategory categoryEnum)
	    throws OperationException {
	return GetControlEntityListForCategorySQL.execute(this, categoryEnum);
    }
}
