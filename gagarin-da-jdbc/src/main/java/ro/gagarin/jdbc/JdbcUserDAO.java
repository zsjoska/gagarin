package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.user2String;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.jdbc.user.CreateUserSQL;
import ro.gagarin.jdbc.user.DeleteUserSQL;
import ro.gagarin.jdbc.user.SelectUserByUsernamePassword;
import ro.gagarin.log.AppLog;
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

		try {

			User user = SelectUserByUsernamePassword.execute(this, username, password);
			if (user == null) {
				throw new ItemNotFoundException(User.class, username + " with password");
			}

			APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.SUCCESS);
			APPLOG.info("User " + username + " logged in");
			return user;
		} catch (OperationException e) {
			APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.FAILED);
			throw e;
		} catch (DataConstraintException e) {
			APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.FAILED);
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		} catch (ItemNotFoundException e) {
			APPLOG.action(AppLogAction.LOGIN, User.class, username, AppLog.FAILED);
			throw e;
		}

	}

	@Override
	public long createUser(User user) throws DataConstraintException, OperationException,
			ItemNotFoundException {

		try {
			if (user.getRole() == null) {
				APPLOG.error("The roleid is not completed");
				markRollback();
				throw new FieldRequiredException("ROLE", User.class);
			}

			new CreateUserSQL(this, user).execute();

			APPLOG.action(AppLogAction.CREATE, User.class, user.getUsername(), AppLog.SUCCESS);
			APPLOG.info("User " + user.getUsername() + " was created");
			return user.getId();
		} catch (OperationException e) {
			APPLOG.error("Could not create user:" + user2String(user), e);
			APPLOG.action(AppLogAction.CREATE, User.class, user.getUsername(), AppLog.FAILED);
			throw e;
		}
	}

	@Override
	public User getUserByUsername(String username) throws OperationException {

		DBUser user = new DBUser();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT Users.id, username, name, email, phone, password, roleid, roleName "
							+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id "
							+ "WHERE username = ?");
			query.setString(1, username);
			rs = query.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
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
	public void deleteUser(User user) throws OperationException, DataConstraintException {

		try {
			new DeleteUserSQL(this, user).execute();
			APPLOG.action(AppLogAction.DELETE, User.class, user.getUsername(), AppLog.SUCCESS);
			APPLOG.info("User " + user.getUsername() + " was deleted");
		} catch (OperationException e) {
			APPLOG.error("Could not delete user:" + user2String(user), e);
			APPLOG.action(AppLogAction.DELETE, User.class, user.getUsername(), AppLog.FAILED);
			throw e;
		}
	}

	@Override
	public List<User> getAllUsers() throws OperationException {
		ArrayList<User> users = new ArrayList<User>();
		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT Users.id, username, name, email, phone, password, roleid, roleName "
							+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id");
			rs = query.executeQuery();

			while (rs.next()) {
				DBUser user = new DBUser();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
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
