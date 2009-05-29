package ro.gagarin.dummyimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class DummyUserDAO extends DummyBase implements UserDAO {

	private static final transient Logger LOG = Logger.getLogger(DummyUserDAO.class);

	private static HashMap<Long, User> users_id = new HashMap<Long, User>();
	private static HashMap<String, User> users_userName = new HashMap<String, User>();

	public DummyUserDAO() {
	}

	@Override
	public User userLogin(String username, String password) throws ItemNotFoundException {
		User user = DummyUserDAO.users_userName.get(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		LOG.info("User " + username + " was not authenticated");
		throw new ItemNotFoundException(User.class, username);
	}

	@Override
	public long createUser(User user) throws FieldRequiredException, ItemExistsException {

		requireStringField(user.getUsername(), "username");

		User user2 = DummyUserDAO.users_userName.get(user.getUsername());
		if (user2 != null)
			throw new ItemExistsException(User.class, user.getUsername());
		user2 = DummyUserDAO.users_id.get(user.getId());
		if (user2 != null)
			throw new ItemExistsException(User.class, user.getId().toString());

		users_id.put(user.getId(), user);
		users_userName.put(user.getUsername(), user);
		LOG.info("Created user:" + user.getUsername() + "; id:" + user.getId());
		return user.getId();
	}

	private void requireStringField(String value, String fieldname) throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname, User.class);
	}

	@Override
	public User getUserByUsername(String username) {
		return DummyUserDAO.users_userName.get(username);
	}

	@Override
	public void deleteUserById(long id) {
		User user = DummyUserDAO.users_id.get(id);
		if (user == null)
			return;
		LOG.info("Delete user:" + user.getUsername() + "; id:" + user.getId());
		DummyUserDAO.users_id.remove(id);
		DummyUserDAO.users_userName.remove(user.getUsername());
	}

	@Override
	public List<User> getUsersWithRole(UserRole role) {
		ArrayList<User> users = new ArrayList<User>();
		for (User user : DummyUserDAO.users_id.values()) {
			if (role.getRoleName().equals(user.getRole().getRoleName()))
				users.add(user);
		}
		return users;
	}

	@Override
	public void release() {
	}

	@Override
	public void deleteUser(User user) {
		LOG.info("Delete user:" + user.getUsername() + "; id:" + user.getId());
		DummyUserDAO.users_id.remove(user.getId());
		DummyUserDAO.users_userName.remove(user.getUsername());
	}

	@Override
	public List<User> getAllUsers() {
		ArrayList<User> users = new ArrayList<User>();
		for (User user : DummyUserDAO.users_id.values()) {
			users.add(user);
		}
		return users;
	}

}
