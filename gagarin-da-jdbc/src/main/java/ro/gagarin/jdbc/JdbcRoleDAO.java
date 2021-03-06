package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.perm2String;
import static ro.gagarin.utils.ConversionUtils.role2String;

import java.util.List;
import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.jdbc.role.AssignPermissionToRoleSQL;
import ro.gagarin.jdbc.role.CreatePermissionSQL;
import ro.gagarin.jdbc.role.CreateRoleSQL;
import ro.gagarin.jdbc.role.DeletePermissionSQL;
import ro.gagarin.jdbc.role.DeleteRoleSQL;
import ro.gagarin.jdbc.role.GetPermissionRolesSQL;
import ro.gagarin.jdbc.role.GetRolePermissionsSQL;
import ro.gagarin.jdbc.role.SelectPermissionByNameSQL;
import ro.gagarin.jdbc.role.SelectPermissionsSQL;
import ro.gagarin.jdbc.role.SelectRoleByNameSQL;
import ro.gagarin.jdbc.role.SelectRolesSQL;
import ro.gagarin.jdbc.role.SubstractRolesPermissions;
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
    public void deleteRole(UserRole role) throws OperationException {

	try {

	    new DeleteRoleSQL(this, role).execute();

	    APPLOG.action(AppLogAction.DELETE, UserRole.class, role.getRoleName(), AppLog.SUCCESS);
	    APPLOG.info("Role " + role.getRoleName() + " was deleted");

	} catch (OperationException e) {
	    APPLOG.error("Could not delete role:" + role2String(role), e);
	    APPLOG.action(AppLogAction.DELETE, UserRole.class, role.getRoleName(), AppLog.FAILED);
	    throw e;
	} catch (DataConstraintException e) {
	    APPLOG.error("Could not delete role:" + role2String(role), e);
	    APPLOG.action(AppLogAction.DELETE, UserRole.class, role.getRoleName(), AppLog.FAILED);
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}
    }

    @Override
    public UserPermission getPermissionByName(String permissionName) throws OperationException {
	return SelectPermissionByNameSQL.execute(this, permissionName);
    }

    @Override
    public void deletePermission(UserPermission perm) throws OperationException {
	try {

	    new DeletePermissionSQL(this, perm).execute();

	    APPLOG.action(AppLogAction.DELETE, UserPermission.class, perm.getPermissionName(), AppLog.SUCCESS);
	    APPLOG.info("Permission " + perm.getPermissionName() + " was deleted");

	} catch (OperationException e) {
	    APPLOG.error("Could not delete permission:" + perm2String(perm), e);
	    APPLOG.action(AppLogAction.DELETE, UserPermission.class, perm.getPermissionName(), AppLog.FAILED);
	    throw e;
	} catch (DataConstraintException e) {
	    APPLOG.error("Could not delete permission:" + perm2String(perm), e);
	    APPLOG.action(AppLogAction.DELETE, UserPermission.class, perm.getPermissionName(), AppLog.FAILED);
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}
    }

    @Override
    public List<UserRole> getAllRoles() throws OperationException {
	return SelectRolesSQL.execute(this);
    }

    @Override
    public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract)
	    throws OperationException {

	return SubstractRolesPermissions.execute(this, main, substract);
    }

    @Override
    public void assignPermissionToRole(UserRole role, UserPermission perm) throws ItemNotFoundException,
	    OperationException {

	// TODO: move the checks to a method inside of execute
	// the markRollback could be forgotten this way
	if (role == null) {
	    markRollback();
	    throw new ItemNotFoundException(UserRole.class, "null");
	}
	if (perm == null) {
	    markRollback();
	    throw new ItemNotFoundException(UserPermission.class, "null");
	}

	try {
	    new AssignPermissionToRoleSQL(this, role, perm).execute();
	} catch (OperationException e) {
	    throw e;
	} catch (DataConstraintException e) {
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}

    }

    @Override
    public Set<UserPermission> getRolePermissions(UserRole role) throws OperationException {
	UserRole foundRole = role;
	if (role.getId() == null && role.getRoleName() != null) {
	    foundRole = this.getRoleByName(role.getRoleName());
	}
	return GetRolePermissionsSQL.execute(this, foundRole);
    }

    @Override
    public Set<UserRole> getPermissionRoles(UserPermission perm) throws OperationException {
	return GetPermissionRolesSQL.execute(this, perm);
    }
}
