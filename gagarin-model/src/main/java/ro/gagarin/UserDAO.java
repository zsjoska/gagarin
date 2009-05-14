package ro.gagarin;

import java.util.List;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemExistsException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

/**
 * Base interface to handle the operations related to the users of the system.
 * 
 * @author zsjoska
 * 
 */
public interface UserDAO extends BaseDAO {

	/**
	 * Verifies the validity of <code>username</code> and <code>password</code>
	 * credentials and returns the respective user.
	 * 
	 * @param username
	 *            the username to log-in
	 * @param password
	 *            the user's password
	 * @return the user identified by the given credentials
	 * @throws ItemNotFoundException
	 *             if the user was not found or the password does not match
	 */
	User userLogin(String username, String password) throws ItemNotFoundException;

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
	 * @throws ItemNotFoundException
	 * @throws ItemExistsException
	 */
	long createUser(User user) throws FieldRequiredException, ItemNotFoundException,
			ItemExistsException;

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

	void deleteUser(User user);

	List<User> getAllUsers();
}
