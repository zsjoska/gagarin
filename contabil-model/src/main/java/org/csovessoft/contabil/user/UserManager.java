package org.csovessoft.contabil.user;

import org.csovessoft.contabil.exceptions.UserNotFoundException;


public interface UserManager {

	User userLogin(String username, String password) throws UserNotFoundException;

	void createUser(User user);

}
