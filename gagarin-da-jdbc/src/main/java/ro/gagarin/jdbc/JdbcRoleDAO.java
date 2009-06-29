package ro.gagarin.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class JdbcRoleDAO extends BaseJdbcDAO implements RoleDAO {

	public JdbcRoleDAO(Session session) {
		super(session);
	}

	@Override
	public DBUserRole getRoleByName(String roleName) {

		DBUserRole role = new DBUserRole();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, roleName FROM UserRoles WHERE roleName = ?");
			query.setString(1, roleName);
			rs = query.executeQuery();
			if (rs.next()) {
				role.setId(rs.getLong("id"));
				role.setRoleName(rs.getString("roleName"));
				return role;
			} else {
				APPLOG.info("UserRole " + roleName + " was not found");
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
	public long createRole(UserRole role) throws DataConstraintException {
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO UserRoles( id, roleName) VALUES (?,?)");
			query.setLong(1, role.getId());
			query.setString(2, role.getRoleName());
			query.executeUpdate();

			APPLOG.info("UserRole " + role.getRoleName() + " was created");
			return role.getId();
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
			throw DataConstraintException.createException(e, UserRole.class);
		}
	}

	@Override
	public long createPermission(UserPermission perm) throws DataConstraintException {

		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO UserPermissions( id, permissionName) VALUES (?,?)");
			query.setLong(1, perm.getId());
			query.setString(2, perm.getPermissionName());
			query.executeUpdate();
			APPLOG.info("UserPermission " + perm.getPermissionName() + " was created");
			return perm.getId();
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
			throw DataConstraintException.createException(e, UserPermission.class);
		}
	}

	@Override
	public List<UserPermission> getAllPermissions() {

		List<UserPermission> permissions = new ArrayList<UserPermission>();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, permissionName FROM UserPermissions");
			rs = query.executeQuery();
			while (rs.next()) {
				DBUserPermission permission = new DBUserPermission();
				permission.setId(rs.getLong("id"));
				permission.setPermissionName(rs.getString("permissionName"));
				permissions.add(permission);
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
		return permissions;
	}

	@Override
	public void deleteRole(UserRole role) {
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
	public UserPermission getPermissionByName(String permissionName) {
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
	public void deletePermission(UserPermission perm) {
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
	public List<UserRole> getAllRoles() {
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
	public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {

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
			throws ItemNotFoundException {
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
	public Set<UserPermission> getRolePermissions(UserRole role) {
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
	public Set<UserRole> getPermissionRoles(UserPermission perm) {
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
