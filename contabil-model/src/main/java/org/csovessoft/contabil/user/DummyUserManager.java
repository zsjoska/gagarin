package org.csovessoft.contabil.user;

import java.util.HashMap;

import org.csovessoft.contabil.exceptions.UserNotFoundException;

public class DummyUserManager implements UserManager {

	private static final DummyUserManager INSTANCE = new DummyUserManager();

	private HashMap<String, User> users = new HashMap<String, User>();

	private DummyUserManager() {

	}

	public static UserManager getInstance() {
		return INSTANCE;
	}

	@Override
	public User userLogin(String username, String password)
			throws UserNotFoundException {
		for (User user : users.values()) {
			if (user.getUsername().equals(username)
					&& user.getPassword().equals(password)) {
				return user;
			}
		}
		throw new UserNotFoundException();
	}

	@Override
	public void createUser(User user) {
		user.setId(String.valueOf(System.nanoTime()));
		users.put(user.getId(), user);
	}

}
