package ro.gagarin;

import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;

public interface AuthorizationManager extends BaseManager {

	void requiresPermission(Session session, PermissionEnum create_user)
			throws PermissionDeniedException;

	void checkUserRole(Session session, User user) throws PermissionDeniedException;

}
