package ro.gagarin;

import java.util.List;

import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;

public interface AuthorizationManager extends BaseManager {

    void requiresPermission(Session session, PermissionEnum create_user) throws PermissionDeniedException,
	    OperationException;

    void checkUserRole(Session session, User user) throws PermissionDeniedException, OperationException;

    void checkUserHasThePermissions(Session session, List<UserPermission> matched) throws OperationException,
	    PermissionDeniedException;

    void requireLogin(Session session) throws LoginRequiredException;

}
