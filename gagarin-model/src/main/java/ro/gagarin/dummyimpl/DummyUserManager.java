package ro.gagarin.dummyimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.UserManager;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.user.DBUser;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class DummyUserManager implements UserManager {

	private static final transient Logger LOG = Logger.getLogger(DummyUserManager.class);

	private static HashMap<Long, DBUser> users_id = new HashMap<Long, DBUser>();
	private static HashMap<String, DBUser> users_userName = new HashMap<String, DBUser>();

	public DummyUserManager() {
	}

	@Override
	public User userLogin(String username, String password) throws UserNotFoundException {
		User user = DummyUserManager.users_userName.get(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		LOG.info("User " + username + " was not authenticated");
		throw new UserNotFoundException(username);
	}

	@Override
	public long createUser(DBUser user) throws FieldRequiredException, UserAlreadyExistsException {

		requireStringField(user.getUsername(), "username");

		User user2 = DummyUserManager.users_userName.get(user.getUsername());
		if (user2 != null)
			throw new UserAlreadyExistsException("username", user.getUsername());

		users_id.put(user.getId(), user);
		users_userName.put(user.getUsername(), user);
		LOG.info("Created user:" + user.getUsername() + "; id:" + user.getId());
		return user.getId();
	}

	private void requireStringField(String value, String fieldname) throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname, DBUser.class);
	}

	@Override
	public User getUserByUsername(String username) {
		return DummyUserManager.users_userName.get(username);
	}

	@Override
	public void deleteUserById(long id) {
		User user = DummyUserManager.users_id.get(id);
		if (user == null)
			return;
		LOG.info("Delete user:" + user.getUsername() + "; id:" + user.getId());
		DummyUserManager.users_id.remove(id);
		DummyUserManager.users_userName.remove(user.getUsername());
	}

	@Override
	public List<User> getUsersWithRole(UserRole role) {
		ArrayList<User> users = new ArrayList<User>();
		for (DBUser user : DummyUserManager.users_id.values()) {
			if (role.getRoleName().equals(user.getRole().getRoleName()))
				users.add(user);
		}
		return users;
	}

	@Override
	public void release() {
	}

}
