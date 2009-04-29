package ro.gagarin;

import java.util.List;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.user.DBUser;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

/**
 * Base interface to handle the operations related to the users of the system.
 * 
 * @author zsjoska
 * 
 */
public interface UserManager extends BaseManager {

	/**
	 * Verifies the validity of <code>username</code> and <code>password</code>
	 * credentials and returns the respective user.
	 * 
	 * @param username
	 *            the username to log-in
	 * @param password
	 *            the user's password
	 * @return the user identified by the given credentials
	 * @throws UserNotFoundException
	 *             if the user was not found or the password does not match
	 */
	User userLogin(String username, String password) throws UserNotFoundException;

	/**
	 * Creates a user
	 * 
	 * @param user
	 *            the user and it's data to be created
	 * @return the ID of the user created
	 * @throws FieldRequiredException
	 *             a required field was missing
	 * @throws UserAlreadyExistsException
	 *             the user or a key-field like username or email already exists
	 */
	long createUser(DBUser user) throws FieldRequiredException, UserAlreadyExistsException;

	/**
	 * Returns the user with the specified username
	 * 
	 * @param username
	 *            the username of the requested user
	 * @return the user object for the given username
	 */
	User getUserByUsername(String username);

	/**
	 * Deletes a user with the given ID
	 * 
	 * @param id
	 *            the ID of the user to be deleted
	 */
	void deleteUserById(long id);

	List<User> getUsersWithRole(UserRole role);
}
