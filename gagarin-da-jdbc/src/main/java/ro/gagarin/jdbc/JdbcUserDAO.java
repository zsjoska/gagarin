package ro.gagarin.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.log.AppLogAction;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class JdbcUserDAO extends BaseJdbcDAO implements UserDAO {

	private static final transient Logger LOG = Logger.getLogger(JdbcUserDAO.class);

	public JdbcUserDAO(Session session) {
		super(session);
	}

	@Override
	public User userLogin(String username, String password) throws ItemNotFoundException {

		APPLOG.action(AppLogAction.LOGIN, User.class, username, null);

		DBUser user = new DBUser();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id,username,password FROM Users WHERE username = ? and password = ?");
			query.setString(1, username);
			query.setString(2, password);
			rs = query.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				rs.close();
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
	public long createUser(User user) throws FieldRequiredException, ItemNotFoundException,
			ItemExistsException {

		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO Users( id, username, name, password, roleid) VALUES (?,?,?,?,?)");
			query.setLong(1, user.getId());
			query.setString(2, user.getUsername());
			query.setString(3, user.getName());
			query.setString(4, user.getPassword());
			query.setLong(5, user.getRole().getId());
			int rows = query.executeUpdate();

			if (rows == 1) {
				APPLOG.action(AppLogAction.CREATE, User.class, user.getUsername(), null);
				LOG.info("User " + user.getUsername() + " was created");
				return user.getId();
			} else {
				LOG.info("User " + user.getUsername() + " was not created");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			APPLOG.error("createUser: Error Executing query", e);
			super.markRollback();
			throw new ItemExistsException();
		} catch (SQLException e) {
			APPLOG.error("createUser: Error Executing query", e);
			super.markRollback();
		}

		// HibernateUtils.requireStringField("getUsername", user);
		// HibernateUtils.requireStringField("getId", user);
		//
		// try {
		// DBUserRole dbRole = JdbcRoleDAO.findOrCreateRole(getEM(),
		// user.getRole());
		// if (dbRole == null) {
		// throw new ItemNotFoundException(UserRole.class, "" +
		// user.getRole().getId());
		// }
		// DBUser dbUser = new DBUser(user);
		// dbUser.setRole(dbRole);
		//
		// getEM().persist(dbUser);
		// getEM().flush();
		//
		// LOG.info("Created user:" + user.getUsername() + "; id:" +
		// user.getId());
		// return user.getId();
		// } catch (RuntimeException e) {
		// markRollback();
		// LOG.error("createUser", e);
		// throw e;
		// }
		return 0;
	}

	@Override
	public User getUserByUsername(String username) {

		DBUser user = new DBUser();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, username, name, password, roleid FROM Users WHERE username = ?");
			query.setString(1, username);
			rs = query.executeQuery();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				// TODO: fill UserRole
				rs.close();
				return user;
			} else {
				LOG.info("User " + username + " was not found");
			}
		} catch (SQLException e) {
			LOG.error("getUserByUsername: Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					LOG.error("getUserByUsername: Error on close", e);
				}
		}
		return null;
	}

	@Override
	public List<User> getUsersWithRole(UserRole role) {
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
			rs.close();
		} catch (SQLException e) {
			LOG.error("getRolePermissions: Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					LOG.error("getRolePermissions: Error on close", e);
				}
		}
		return users;
	}

	@Override
	public void deleteUser(User user) {
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"DELETE FROM Users WHERE id = ?");
			query.setLong(1, user.getId());
			query.executeUpdate();
			LOG.info("UserRole " + user.getUsername() + " was deleted");
		} catch (SQLException e) {
			LOG.error("deleteRole: Error Executing query", e);
			super.markRollback();
		}
	}

	@Override
	public List<User> getAllUsers() {
		// try {
		// Query query = getEM().createQuery("select u from DBUser u");
		// List result = query.getResultList();
		// return result;
		// } catch (RuntimeException e) {
		// super.markRollback();
		// LOG.error("getAllUsers", e);
		// throw e;
		// }
		return null;
	}
}
