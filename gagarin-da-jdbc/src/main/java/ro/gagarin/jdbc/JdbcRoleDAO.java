package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.perm2String;
import static ro.gagarin.utils.ConversionUtils.role2String;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.Owner;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.jdbc.role.AssignPermissionToRoleSQL;
import ro.gagarin.jdbc.role.AssignRoleToOwnerSQL;
import ro.gagarin.jdbc.role.CleanupPermissionRoleAssignments;
import ro.gagarin.jdbc.role.CleanupRoleOwnerAssignments;
import ro.gagarin.jdbc.role.CleanupRolePermissionAssignments;
import ro.gagarin.jdbc.role.CreatePermissionSQL;
import ro.gagarin.jdbc.role.CreateRoleSQL;
import ro.gagarin.jdbc.role.DeletePermissionSQL;
import ro.gagarin.jdbc.role.DeleteRoleSQL;
import ro.gagarin.jdbc.role.GetControlEntityByIdAndCategorySQL;
import ro.gagarin.jdbc.role.GetControlEntityListForCategorySQL;
import ro.gagarin.jdbc.role.GetEffectivePermissionsObjectOwnerSQL;
import ro.gagarin.jdbc.role.GetEffectivePermissionsOnEntitySQL;
import ro.gagarin.jdbc.role.GetEffectivePermissionsSQL;
import ro.gagarin.jdbc.role.GetPermissionAssignmentsForControlEntitySQL;
import ro.gagarin.jdbc.role.GetPermissionRolesSQL;
import ro.gagarin.jdbc.role.GetRolePermissionsSQL;
import ro.gagarin.jdbc.role.SelectPermissionByNameSQL;
import ro.gagarin.jdbc.role.SelectPermissionsSQL;
import ro.gagarin.jdbc.role.SelectRoleByNameSQL;
import ro.gagarin.jdbc.role.SelectRolesSQL;
import ro.gagarin.jdbc.role.SubstractRolesPermissions;
import ro.gagarin.jdbc.role.UnAssignPermissionFromRoleSQL;
import ro.gagarin.jdbc.role.UnAssignRoleFromOwnerSQL;
import ro.gagarin.jdbc.role.UpdateRoleSQL;
import ro.gagarin.log.AppLog;
import ro.gagarin.log.AppLogAction;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermOwnerCEAssignment;
import ro.gagarin.user.PermissionEnum;
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
	    new CleanupRolePermissionAssignments(this, r).execute();
	    new CleanupRoleOwnerAssignments(this, r).execute();

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
	    new CleanupPermissionRoleAssignments(this, perm);

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
    public void unAssignPermissionFromRole(UserRole role, UserPermission perm) throws ItemNotFoundException,
	    OperationException {

	UserRole r = completeRoleId(role);
	UserPermission p = completePermissionId(perm);

	try {
	    new UnAssignPermissionFromRoleSQL(this, r, p).execute();
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
    public void assignRoleToOwner(UserRole role, Owner owner, ControlEntity object) throws OperationException,
	    DataConstraintException, ItemNotFoundException {

	UserRole completeRole = completeRoleId(role);

	new AssignRoleToOwnerSQL(this, completeRole, owner, object).execute();
    }

    @Override
    public void unAssignRoleFromOwner(UserRole role, Owner owner, ControlEntity ce) throws OperationException,
	    DataConstraintException, ItemNotFoundException {
	UserRole completeRole = completeRoleId(role);

	new UnAssignRoleFromOwnerSQL(this, completeRole, owner, ce).execute();
    }

    @Override
    public Set<UserPermission> getEffectivePermissionsOnEntity(ControlEntity entity, Owner... owners)
	    throws OperationException {
	GetEffectivePermissionsOnEntitySQL cmd = new GetEffectivePermissionsOnEntitySQL(this, entity, owners);
	cmd.execute();
	return cmd.getPermissions();
    }

    @Override
    public Map<ControlEntity, Set<UserPermission>> getEffectivePermissions(Owner... owners) throws OperationException {
	Map<ControlEntity, Set<UserPermission>> effectivePermissions = GetEffectivePermissionsSQL.execute(this, owners);

	// at this point, all control entities are missing the name field
	// the only way to figure it out is to do additional queries
	// this would be nice to have cached

	Map<ControlEntity, Set<UserPermission>> newMap = new HashMap<ControlEntity, Set<UserPermission>>();
	for (ControlEntity ce : effectivePermissions.keySet()) {
	    if (ce.getCategory().table() != null) {
		ControlEntity completeCe = GetControlEntityByIdAndCategorySQL.execute(this, ce.getId(),
			ce.getCategory());
		if (completeCe == null) {
		    APPLOG.error("Inconsistent control entity found. Cleanup was not done properly. Id=" + ce.getId()
			    + " Category = " + ce.getCategory());
		} else {
		    newMap.put(completeCe, effectivePermissions.get(ce));
		}
	    } else {
		if (ce.getId() == BaseControlEntity.getAdminEntity().getId()) {
		    newMap.put(BaseControlEntity.getAdminEntity(), effectivePermissions.get(ce));
		} else {
		    APPLOG.error("Unknown control entity");
		}
	    }
	}
	return newMap;
    }

    @Override
    public List<ControlEntity> getControlEntityListForCategory(ControlEntityCategory categoryEnum)
	    throws OperationException {
	return GetControlEntityListForCategorySQL.execute(this, categoryEnum);
    }

    @Override
    public List<PermOwnerCEAssignment> getPermissionAssignmentsForControlEntity(ControlEntity ce)
	    throws OperationException {
	return GetPermissionAssignmentsForControlEntitySQL.execute(this, ce);
    }

    @Override
    public void updateRole(UserRole role) throws OperationException, DataConstraintException {
	new UpdateRoleSQL(this, role).execute();
    }

    @Override
    public Set<PermissionEnum> getEffectivePermissionsObjectOwner(ControlEntity ce, Owner owner)
	    throws OperationException {
	return GetEffectivePermissionsObjectOwnerSQL.execute(this, ce, owner);
    }
}
