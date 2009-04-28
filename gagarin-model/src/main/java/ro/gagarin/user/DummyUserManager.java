package ro.gagarin.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.UserManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;

public class DummyUserManager implements UserManager {

	private static final transient Logger LOG = Logger
			.getLogger(DummyUserManager.class);


	private static HashMap<Long, User> users_id = new HashMap<Long, User>();
	private static HashMap<String, User> users_userName = new HashMap<String, User>();

	public DummyUserManager() {
	}


	@Override
	public User userLogin(String username, String password)
			throws UserNotFoundException {
		User user = DummyUserManager.users_userName.get(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		LOG.info("User " + username + " was not authenticated");
		throw new UserNotFoundException(username);
	}

	@Override
	public long createUser(User user) throws FieldRequiredException,
			UserAlreadyExistsException {

		requireStringField(user.getUsername(), "username");

		User user2 = DummyUserManager.users_userName.get(user.getUsername());
		if (user2 != null)
			throw new UserAlreadyExistsException("username", user.getUsername());

		users_id.put(user.getId(), user);
		users_userName.put(user.getUsername(), user);
		LOG.info("Created user:" + user.getUsername() + "; id:" + user.getId());
		return user.getId();
	}

	private void requireStringField(String value, String fieldname)
			throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname, User.class);
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
		for (User user : DummyUserManager.users_id.values()) {
			if (role.getRoleName().equals(user.getRole().getRoleName()))
				users.add(user);
		}
		return users;
	}

	@Override
	public void release() {
	}

}
