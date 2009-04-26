package org.csovessoft.contabil.user;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.UserManager;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;

public class HibernateUserManager implements UserManager {

	private static final transient Logger LOG = Logger.getLogger(HibernateUserManager.class);

	private static final HibernateUserManager INSTANCE = new HibernateUserManager();

	private HibernateUserManager() {

		// User user = new User();
		// user.setUsername("admin");
		// user.setPassword("test");
		//
		// try {
		// createUser(user);
		// LOG.info("User admin created with password test");
		// } catch (ExceptionBase e) {
		// LOG.error("Exception creating admin user", e);
		// }
	}

	public static UserManager getInstance() {
		return INSTANCE;
	}

	@Override
	public User userLogin(String username, String password) throws UserNotFoundException {
		// User user = this.users_userName.get(username);
		// if (user != null && user.getPassword().equals(password)) {
		// return user;
		// }
		// LOG.info("User " + username + " was not authenticated");
		// throw new UserNotFoundException(username);
		return null;
	}

	@Override
	public long createUser(User user) throws FieldRequiredException, UserAlreadyExistsException {

		// requireStringField(user.getUsername(), "username");
		//
		// User user2 = this.users_userName.get(user.getUsername());
		// if (user2 != null)
		// throw new UserAlreadyExistsException("username", user.getUsername());
		//
		// user.setId(String.valueOf(System.nanoTime()));
		// users_id.put(user.getId(), user);
		// users_userName.put(user.getUsername(), user);
		// LOG.info("Created user:" + user.getUsername() + "; id:" +
		// user.getId());
		// return user.getId();
		return 0;
	}

	private void requireStringField(String value, String fieldname) throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname, User.class);
	}

	@Override
	public User getUserByUsername(String username) {
		// return this.users_userName.get(username);
		return null;
	}

	@Override
	public void deleteUserById(String id) {
		// User user = this.users_id.get(id);
		// if (user == null)
		// return;
		// LOG.info("Delete user:" + user.getUsername() + "; id:" +
		// user.getId());
		// this.users_id.remove(id);
		// this.users_userName.remove(user.getUsername());
	}

}
