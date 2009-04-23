package org.csovessoft.contabil.user;

import java.util.HashMap;

import org.csovessoft.contabil.exceptions.ExceptionBase;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;

public class DummyUserManager implements UserManager {

	private static final DummyUserManager INSTANCE = new DummyUserManager();

	private HashMap<String, User> users_id = new HashMap<String, User>();
	private HashMap<String, User> users_userName = new HashMap<String, User>();

	private DummyUserManager() {

		User user = new User();
		user.setUsername("admin");
		user.setPassword("test");
		try {
			createUser(user);
		} catch (ExceptionBase e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static UserManager getInstance() {
		return INSTANCE;
	}

	@Override
	public User userLogin(String username, String password) throws UserNotFoundException {
		User user = this.users_userName.get(username);
		if (user != null && user.getPassword().equals(password))
			return user;
		throw new UserNotFoundException();
	}

	@Override
	public void createUser(User user) throws FieldRequiredException, UserAlreadyExistsException {

		requireStringField(user.getUsername(), "username");

		User user2 = this.users_userName.get(user.getUsername());
		if (user2 != null)
			throw new UserAlreadyExistsException(user.getUsername());

		user.setId(String.valueOf(System.nanoTime()));
		users_id.put(user.getId(), user);
		users_userName.put(user.getUsername(), user);
	}

	private void requireStringField(String value, String fieldname) throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname);
	}

	@Override
	public User getUserByUsername(String username) {
		return this.users_userName.get(username);
	}

	@Override
	public void deleteUserById(String id) {
		User user = this.users_id.get(id);
		if (user == null)
			return;
		this.users_id.remove(id);
		this.users_userName.remove(user.getUsername());
	}

}
