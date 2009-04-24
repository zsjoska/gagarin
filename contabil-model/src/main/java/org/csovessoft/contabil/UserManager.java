package org.csovessoft.contabil;

import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;
import org.csovessoft.contabil.user.User;

public interface UserManager {

	User userLogin(String username, String password) throws UserNotFoundException;

	String createUser(User user) throws FieldRequiredException, UserAlreadyExistsException;

	User getUserByUsername(String username);

	void deleteUserById(String id);
}
