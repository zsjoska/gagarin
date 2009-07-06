package ro.gagarin.jdbc;

import static ro.gagarin.utils.ConversionUtils.user2String;

import java.util.ArrayList;
import java.util.List;

import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.user.CreateUserSQL;
import ro.gagarin.jdbc.user.DeleteUserSQL;
import ro.gagarin.jdbc.user.GetUsersWithRoleSQL;
import ro.gagarin.jdbc.user.SelectUserByUsernameSQL;
import ro.gagarin.jdbc.user.SelectUserByUsernamePasswordSQL;
import ro.gagarin.jdbc.user.SelectUsersSQL;
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

			User user = SelectUserByUsernamePasswordSQL.execute(this, username, password);
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

		try {
			User user = SelectUserByUsernameSQL.execute(this, username);
			return user;
		} catch (OperationException e) {
			throw e;
		} catch (DataConstraintException e) {
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		}
	}

	@Override
	public List<User> getUsersWithRole(UserRole role) throws OperationException {

		try {
			ArrayList<User> users = GetUsersWithRoleSQL.execute(this, role);
			return users;
		} catch (OperationException e) {
			throw e;
		} catch (DataConstraintException e) {
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		}
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
		try {
			ArrayList<User> users = SelectUsersSQL.execute(this);
			return users;
		} catch (OperationException e) {
			throw e;
		} catch (DataConstraintException e) {
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		}
	}
}
