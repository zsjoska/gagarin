package org.csovessoft.contabil.user;

import org.csovessoft.contabil.results.MResult;

public class DefaultUserManager implements UserManager {

	private static final DefaultUserManager INSTANCE = new DefaultUserManager();

	private DefaultUserManager() {

	}

	public static UserManager getInstance() {
		return INSTANCE;
	}

	@Override
	public MResult login(String username, String password) {
		return new MResult(true);
	}

	@Override
	public void createUser(User user) {
		// TODO Auto-generated method stub
		
	}

}
