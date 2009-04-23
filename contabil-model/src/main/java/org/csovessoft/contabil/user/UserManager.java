package org.csovessoft.contabil.user;

import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;

public interface UserManager {

	User userLogin(String username, String password) throws UserNotFoundException;

	void createUser(User user) throws FieldRequiredException, UserAlreadyExistsException;

	User getUserByUsername(String username);

	void deleteUserById(String id);
}
