package ro.gagarin.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.log.AppLogAction;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class JdbcUserDAO extends BaseJdbcDAO implements UserDAO {

	public JdbcUserDAO(Session session) throws OperationException {
		super(session);
	}

	@Override
	public User userLogin(String username, String password) throws ItemNotFoundException,
			OperationException {

		APPLOG.action(AppLogAction.LOGIN, User.class, username, null);

		DBUser user = new DBUser();

		ResultSet rs = null;
		try {
			// PreparedStatement query = getConnection().prepareStatement(
			// "SELECT id,username,password FROM Users WHERE username = ? and password = ?");
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT Users.id, username, name, password, roleid, roleName "
							+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id "
							+ "WHERE username = ? and password = ?");
			query.setString(1, username);
			query.setString(2, password);
			rs = query.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				DBUserRole role = new DBUserRole();
				role.setId(rs.getLong("roleid"));
				role.setRoleName(rs.getString("roleName"));
				user.setRole(role);
				return user;
			} else {
				APPLOG.action(AppLogAction.LOGIN, User.class, username, "FAILED");
			}
		} catch (SQLException e) {
			APPLOG.error("userLogin: Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("userLogin: Error on close", e);
				}
		}
		throw new ItemNotFoundException(User.class, username + " with password");
	}

	@Override
	public long createUser(User user) throws DataConstraintException, OperationException {

		if (user.getRole() == null) {
			APPLOG.error("The roleid is not completed");
			markRollback();
			throw new FieldRequiredException("roleid", User.class);
		}

		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO Users( id, username, name, password, roleid) VALUES (?,?,?,?,?)");
			query.setLong(1, user.getId());
			query.setString(2, user.getUsername());
			query.setString(3, user.getName());
			query.setString(4, user.getPassword());
			query.setLong(5, user.getRole().getId());
			query.executeUpdate();

			APPLOG.action(AppLogAction.CREATE, User.class, user.getUsername(), null);
			APPLOG.info("User " + user.getUsername() + " was created");
			return user.getId();
		} catch (SQLException e) {
			super.markRollback();
			DataConstraintException x = DataConstraintException.createException(e, User.class);
			APPLOG.error("createUser: Error Executing query", x);
			throw x;
		}
	}

	@Override
	public User getUserByUsername(String username) throws OperationException {

		DBUser user = new DBUser();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT Users.id, username, name, password, roleid, roleName "
							+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id "
							+ "WHERE username = ?");
			query.setString(1, username);
			rs = query.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				DBUserRole role = new DBUserRole();
				role.setId(rs.getLong("roleid"));
				role.setRoleName(rs.getString("roleName"));
				user.setRole(role);
				return user;
			} else {
				APPLOG.info("User " + username + " was not found");
			}
		} catch (SQLException e) {
			APPLOG.error("getUserByUsername: Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("getUserByUsername: Error on close", e);
				}
		}
		return null;
	}

	@Override
	public List<User> getUsersWithRole(UserRole role) throws OperationException {
		ArrayList<User> users = new ArrayList<User>();
		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, name, userName, roleid FROM Users WHERE roleid = ?");
			query.setLong(1, role.getId());
			rs = query.executeQuery();

			while (rs.next()) {
				DBUser user = new DBUser();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("userName"));
				user.setRole(role);
				users.add(user);
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("getRolePermissions: Error on close", e);
				}
		}
		return users;
	}

	@Override
	public void deleteUser(User user) throws OperationException {
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"DELETE FROM Users WHERE id = ?");
			query.setLong(1, user.getId());
			query.executeUpdate();
			APPLOG.info("UserRole " + user.getUsername() + " was deleted");
		} catch (SQLException e) {
			APPLOG.error("deleteRole: Error Executing query", e);
			super.markRollback();
		}
	}

	@Override
	public List<User> getAllUsers() throws OperationException {
		ArrayList<User> users = new ArrayList<User>();
		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT Users.id, username, name, password, roleid, roleName "
							+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id");
			rs = query.executeQuery();

			while (rs.next()) {
				DBUser user = new DBUser();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("userName"));
				DBUserRole role = new DBUserRole();
				role.setId(rs.getLong("roleid"));
				role.setRoleName(rs.getString("roleName"));
				user.setRole(role);
				users.add(user);
			}
		} catch (SQLException e) {
			APPLOG.error("Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					APPLOG.error("getRolePermissions: Error on close", e);
				}
		}
		return users;
	}
}
