package ro.gagarin.dao;

import java.util.List;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;

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

    /**
     * Deletes a user
     * 
     * @param user
     *            the user to be deleted
     * @throws OperationException
     * @throws DataConstraintException
     * @throws ItemNotFoundException
     */
    void deleteUser(User user) throws OperationException, DataConstraintException, ItemNotFoundException;

    /**
     * Queries a list with all users
     * 
     * @return list with all users
     * @throws OperationException
     */
    List<User> getAllUsers() throws OperationException;

    /**
     * Creates a new group
     * 
     * @param group
     *            the group to be created
     * @return the <code>id</code> of the created group
     * @throws DataConstraintException
     * @throws OperationException
     */
    Long createGroup(Group group) throws DataConstraintException, OperationException;

    /**
     * Returns a group by looking up the group name
     * 
     * @param groupname
     *            the name of the group to be returned
     * @return the group
     * @throws OperationException
     */
    Group getGroupByName(String groupname) throws OperationException;

    /**
     * Returns a list with all groups
     * 
     * @return the group list
     * @throws OperationException
     */
    List<Group> getGroups() throws OperationException;

    /**
     * Deletes a group
     * 
     * @param group
     *            the group to be deleted
     * @throws OperationException
     * @throws DataConstraintException
     * @throws ItemNotFoundException
     */
    void deleteGroup(Group group) throws OperationException, DataConstraintException, ItemNotFoundException;

    /**
     * Updates a group with the <code>non-null</code> fields
     * 
     * @param group
     *            the group to be updated identified by <code>id</code>
     * @throws OperationException
     * @throws DataConstraintException
     * @throws ItemNotFoundException
     */
    void updateGroup(Group group) throws OperationException, DataConstraintException, ItemNotFoundException;

    /**
     * Assigns a user to a group
     * 
     * @param user
     *            the user to be assigned to the group
     * @param group
     *            the group
     * @throws OperationException
     * @throws ItemNotFoundException
     * @throws DataConstraintException
     */
    void assignUserToGroup(User user, Group group) throws OperationException, ItemNotFoundException,
	    DataConstraintException;

    /**
     * Returns the list of users belonging to a specific group
     * 
     * @param group
     * @return the list of users
     * @throws OperationException
     * @throws ItemNotFoundException
     */
    List<User> getGroupUsers(Group group) throws OperationException, ItemNotFoundException;

    List<Group> getUserGroups(User user) throws ItemNotFoundException, OperationException;

    void updateUser(User user) throws OperationException, DataConstraintException, ItemNotFoundException;

    void unassignUserFromGroup(User user, Group group) throws OperationException, ItemNotFoundException,
	    DataConstraintException;

    void deleteGroupAssignments(Group group) throws OperationException, DataConstraintException;

}
