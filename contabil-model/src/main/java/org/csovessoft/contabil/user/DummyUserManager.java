package org.csovessoft.contabil.user;

import java.util.HashMap;

import org.csovessoft.contabil.results.MResult;

public class DummyUserManager implements UserManager {

	private static final DummyUserManager INSTANCE = new DummyUserManager();

	private HashMap<String, User> users = new HashMap<String, User>();

	private DummyUserManager() {

	}

	public static UserManager getInstance() {
		return INSTANCE;
	}

	@Override
	public MResult login(String username, String password) {
		for (User user : users.values()) {
			if (user.getUsername().equals(username)
					&& user.getPassword().equals(password)) {
				return new MResult(true);
			}
		}
		return new MResult(false);
	}

	@Override
	public void createUser(User user) {
		user.setId(String.valueOf(System.nanoTime()));
		users.put(user.getId(), user);
	}

}
