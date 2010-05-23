package ro.gagarin.manager;

import java.util.List;

import ro.gagarin.ControlEntity;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;

public interface AuthorizationManager extends BaseManager {

    /**
     * Verifies if the current logged in user has one of the required
     * permissions.<br>
     * The given list is verified against the current effective permission set
     * of the current session<br>
     * The permission list provided is considered with OR operation. If more
     * permissions are required (AND would be required) an additional method
     * call should be performed.
     * 
     * @param session
     *            current session
     * @param ce
     *            the control entity object on which the permission to be
     *            verified
     * @param permission
     *            a list of permissions
     * @throws PermissionDeniedException
     *             is thrown when no one of the permissions were found for the
     *             current user
     * @throws OperationException
     */
    void requiresPermission(Session session, ControlEntity ce, PermissionEnum... permission)
	    throws PermissionDeniedException, OperationException;

    void checkUserRole(Session session, User user) throws PermissionDeniedException, OperationException;

    void checkUserHasThePermissions(Session session, List<UserPermission> matched) throws OperationException,
	    PermissionDeniedException;

    void requireLogin(Session session) throws LoginRequiredException;

    void addCreatorPermission(Group group, Session session) throws OperationException, DataConstraintException,
	    ItemNotFoundException;

}
