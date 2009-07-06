package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.perm2String;
import static ro.gagarin.utils.ConversionUtils.role2String;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.jdbc.role.CreatePermissionSQL;
import ro.gagarin.jdbc.role.CreateRoleSQL;
import ro.gagarin.jdbc.role.SelectPermissionsSQL;
import ro.gagarin.jdbc.role.SelectRoleByNameSQL;
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

		try {
			UserRole role = SelectRoleByNameSQL.execute(this, roleName);
			return role;
		} catch (OperationException e) {
			throw e;
		} catch (DataConstraintException e) {
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		}

	}

	@Override
	public long createRole(UserRole role) throws DataConstraintException, OperationException {

		try {

			new CreateRoleSQL(this, role).execute();

			APPLOG.action(AppLogAction.CREATE, UserRole.class, role.getRoleName(), AppLog.SUCCESS);
			APPLOG.info("Role " + role.getRoleName() + " was created");
			return role.getId();
		} catch (OperationException e) {
			APPLOG.error("Could not create role:" + role2String(role), e);
			APPLOG.action(AppLogAction.CREATE, UserRole.class, role.getRoleName(), AppLog.FAILED);
			throw e;
		}
	}

	@Override
	public long createPermission(UserPermission perm) throws DataConstraintException,
			OperationException {

		try {

			new CreatePermissionSQL(this, perm).execute();

			APPLOG.action(AppLogAction.CREATE, UserPermission.class, perm.getPermissionName(),
					AppLog.SUCCESS);
			APPLOG.info("Permission " + perm.getPermissionName() + " was created");
			return perm.getId();
		} catch (OperationException e) {
			APPLOG.error("Could not create permission:" + perm2String(perm), e);
			APPLOG.action(AppLogAction.CREATE, UserPermission.class, perm.getPermissionName(),
					AppLog.FAILED);
			throw e;
		}
	}

	@Override
	public List<UserPermission> getAllPermissions() throws OperationException {

		List<UserPermission> permissions;
		try {
			permissions = SelectPermissionsSQL.execute(this);
			return permissions;
		} catch (DataConstraintException e) {
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		}
	}

	@Override
	public void deleteRole(UserRole role) throws OperationException {
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"DELETE FROM UserRoles WHERE id = ?");
			query.setLong(1, role.getId());
			query.executeUpdate();
			APPLOG.info("UserRole " + role.getRoleName() + " was deleted");
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		}
	}

	@Override
	public UserPermission getPermissionByName(String permissionName) throws OperationException {
		DBUserPermission perm = new DBUserPermission();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, permissionName FROM UserPermissions WHERE permissionName = ?");
			query.setString(1, permissionName);
			rs = query.executeQuery();
			if (rs.next()) {
				perm.setId(rs.getLong("id"));
				perm.setPermissionName(rs.getString("permissionName"));
				return perm;
			} else {
				APPLOG.info("UserPermission " + permissionName + " was not found");
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("Error on close", e);
				}
		}
		return null;
	}

	@Override
	public void deletePermission(UserPermission perm) throws OperationException {
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"DELETE FROM UserPermissions WHERE id = ?");
			query.setLong(1, perm.getId());
			query.executeUpdate();
			APPLOG.info("UserRole " + perm.getPermissionName() + " was deleted");
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		}
	}

	@Override
	public List<UserRole> getAllRoles() throws OperationException {
		List<UserRole> roles = new ArrayList<UserRole>();
		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, roleName FROM UserRoles");
			rs = query.executeQuery();

			while (rs.next()) {
				DBUserRole role = new DBUserRole();
				role.setId(rs.getLong("id"));
				role.setRoleName(rs.getString("roleName"));
				roles.add(role);
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("Error on close", e);
				}
		}
		return roles;
	}

	@Override
	public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract)
			throws OperationException {

		ArrayList<UserPermission> perms = new ArrayList<UserPermission>();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection()
					.prepareStatement(
							"SELECT id, permissionName FROM UserPermissions INNER JOIN PermissionAssignment ON UserPermissions.id = PermissionAssignment.perm_id WHERE PermissionAssignment.role_id = ? AND id NOT IN "
									+ "("
									+ "SELECT id FROM UserPermissions INNER JOIN PermissionAssignment ON UserPermissions.id = PermissionAssignment.perm_id WHERE PermissionAssignment.role_id = ?"
									+ ")");

			query.setLong(1, main.getId());
			query.setLong(2, substract.getId());
			rs = query.executeQuery();
			while (rs.next()) {
				DBUserPermission role = new DBUserPermission();
				role.setId(rs.getLong("id"));
				role.setPermissionName(rs.getString("permissionName"));
				perms.add(role);
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
			// TODO: throw exception to signal error
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("Error on close", e);
				}
		}
		return perms;
	}

	@Override
	public void assignPermissionToRole(UserRole role, UserPermission perm)
			throws ItemNotFoundException, OperationException {
		if (role == null) {
			markRollback();
			throw new ItemNotFoundException(UserRole.class, "null");
		}
		if (perm == null) {
			markRollback();
			throw new ItemNotFoundException(UserPermission.class, "null");
		}

		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO PermissionAssignment( role_id, perm_id) VALUES (?,?)");
			query.setLong(1, role.getId());
			query.setLong(2, perm.getId());
			query.executeUpdate();
			APPLOG.info("UserPermission " + perm.getPermissionName() + " was assigned to role "
					+ role.getRoleName());
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
			// TODO: add exception handling
		}
	}

	@Override
	public Set<UserPermission> getRolePermissions(UserRole role) throws OperationException {
		HashSet<UserPermission> perms = new HashSet<UserPermission>();
		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection()
					.prepareStatement(
							"SELECT id, permissionName FROM UserPermissions INNER JOIN PermissionAssignment ON id = perm_id WHERE role_id = ?");
			query.setLong(1, role.getId());
			rs = query.executeQuery();

			while (rs.next()) {
				DBUserPermission permission = new DBUserPermission();
				permission.setId(rs.getLong("id"));
				permission.setPermissionName(rs.getString("permissionName"));
				perms.add(permission);
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("Error on close", e);
				}
		}
		return perms;
	}

	@Override
	public Set<UserRole> getPermissionRoles(UserPermission perm) throws OperationException {
		HashSet<UserRole> roles = new HashSet<UserRole>();
		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection()
					.prepareStatement(
							"SELECT id, roleName FROM UserRoles INNER JOIN PermissionAssignment ON id = role_id WHERE perm_id = ?");
			query.setLong(1, perm.getId());
			rs = query.executeQuery();

			while (rs.next()) {
				DBUserRole role = new DBUserRole();
				role.setId(rs.getLong("id"));
				role.setRoleName(rs.getString("roleName"));
				roles.add(role);
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("Error on close", e);
				}
		}
		return roles;
	}
}
