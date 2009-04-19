package org.csovessoft.contabil.user;

import org.csovessoft.contabil.results.MResult;

public interface UserManager {

	MResult login(String username, String password);

	void createUser(User user);

}
