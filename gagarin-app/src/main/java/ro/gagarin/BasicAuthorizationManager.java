package ro.gagarin;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class BasicAuthorizationManager implements AuthorizationManager {
    private static final transient Logger LOG = Logger.getLogger(BasicAuthorizationManager.class);

    @Override
    public void checkUserRole(Session session, User user) throws PermissionDeniedException, OperationException {
	User sessionUser = session.getUser();
	RoleDAO roleManager = session.getManagerFactory().getDAOManager().getRoleDAO(session);
	List<UserPermission> leftList;
	try {
	    leftList = roleManager.substractUsersRolePermissions(user.getRole(), sessionUser.getRole());
	    LOG.debug("left permissions:" + leftList.toString());
	} catch (ItemNotFoundException e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	}
	if (leftList.size() != 0)
	    throw new PermissionDeniedException(sessionUser.getUsername(), leftList.toString());
    }

    @Override
    public void requiresPermission(Session session, PermissionEnum reqPermission) throws PermissionDeniedException,
	    OperationException {

	User user = null;
	user = session.getUser();
	if (user.getRole() == null) {
	    // TODO: remove this requirement for having role
	    throw new NullPointerException("Role is still required here");
	}

	RoleDAO roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);

	Set<UserPermission> perm;
	try {
	    perm = roleDAO.getRolePermissions(user.getRole());
	} catch (ItemNotFoundException e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	}

	Iterator<? extends UserPermission> iterator = perm.iterator();
	while (iterator.hasNext()) {
	    UserPermission userPermission = iterator.next();
	    if (userPermission.getPermissionName().equals(reqPermission.name())) {
		LOG.debug(reqPermission.name() + " was found for user " + user.getUsername());
		return;
	    }

	}
	throw new PermissionDeniedException(user.getUsername(), reqPermission.name());
    }

    @Override
    public void checkUserHasThePermissions(Session session, List<UserPermission> matched) throws OperationException,
	    PermissionDeniedException {
	UserRole role = session.getUser().getRole();
	RoleDAO roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
	Set<UserPermission> loginUserPermissions;
	try {
	    loginUserPermissions = roleDAO.getRolePermissions(role);
	} catch (ItemNotFoundException e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	}
	for (UserPermission p : matched) {
	    UserPermission found = null;
	    for (UserPermission userPermission : loginUserPermissions) {
		if (!userPermission.getPermissionName().equalsIgnoreCase(p.getPermissionName())) {
		    found = userPermission;
		}
	    }
	    if (found == null) {
		throw new PermissionDeniedException(session.getUser().getUsername(), p.getPermissionName());
	    }
	}
    }

    @Override
    public void requireLogin(Session session) throws LoginRequiredException {
	UserRole role = null;
	if (session != null) {
	    User user = session.getUser();
	    if (user != null) {
		role = user.getRole();
	    }
	}
	if (role == null)
	    throw new LoginRequiredException();
    }

    @Override
    public void initializeManager() {
	// nothing to initialize
    }
}
