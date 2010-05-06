package ro.gagarin;

import java.util.List;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.Group;
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
     * @throws OperationException
     * @throws DataConstraintException
     */
    User userLogin(String username, String password) throws ItemNotFoundException, OperationException;

    /**
     * Creates a user
     * 
     * @param user
     *            the user and it's data to be created
     * @return the ID of the user created
     * @throws DataConstraintException
     * @throws OperationException
     * @throws ItemNotFoundException
     */
    long createUser(User user) throws DataConstraintException, OperationException, ItemNotFoundException;

    /**
     * Returns the user with the specified username
     * 
     * @param username
     *            the username of the requested user
     * @return the user object for the given username
     * @throws OperationException
     */
    User getUserByUsername(String username) throws OperationException;

    List<User> getUsersWithRole(UserRole role) throws OperationException;

    void deleteUser(User user) throws OperationException, DataConstraintException;

    List<User> getAllUsers() throws OperationException;

    void markRollback();

    Long createGroup(Group group) throws DataConstraintException, OperationException;

    Group getGroupByName(String groupname) throws OperationException;

    List<Group> getGroups() throws OperationException;

    void deleteGroup(Group group) throws OperationException, DataConstraintException, ItemNotFoundException;
}
