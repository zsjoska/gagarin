package ro.gagarin;

import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.config.Configuration;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.PermissionTest;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.util.Utils;

public class BasicAuthorizationManager implements AuthorizationManager {
    private static final transient Logger LOG = Logger.getLogger(BasicAuthorizationManager.class);

    @Override
    public void requiresPermission(Session session, ControlEntity ce, PermissionEnum... reqPermission)
	    throws PermissionDeniedException, OperationException {

	User user = null;
	user = session.getUser();

	// check if the session is admin session
	if (session.isAdminSession()) {
	    LOG.debug("Admin session, skipping permission check");
	    return;
	}

	Set<PermissionEnum> permSet = session.getEffectivePermissions().get(ce);
	if (permSet == null) {
	    throw new PermissionDeniedException(user.getUsername(), reqPermission, ce);
	}

	for (PermissionEnum reqPerm : reqPermission) {
	    if (permSet.contains(reqPerm)) {
		LOG.debug(reqPerm.name() + " was found for user " + user.getUsername());
		return;
	    }
	}

	// check if it has the permission for ADMIN_ENTITY
	permSet = session.getEffectivePermissions().get(CommonControlEntities.ADMIN_CE);
	for (PermissionEnum reqPerm : reqPermission) {
	    if (permSet.contains(reqPerm)) {
		LOG.debug("ADMIN " + reqPerm.name() + " was found for user " + user.getUsername());
		return;
	    }
	}

	throw new PermissionDeniedException(user.getUsername(), reqPermission, ce);
    }

    // @Override
    public void requiresPermission(Session session, PermissionTest... tests) throws PermissionDeniedException,
	    OperationException {

	User user = null;
	user = session.getUser();

	// check if the session is admin session
	if (session.isAdminSession()) {
	    LOG.debug("Admin session, skipping permission check");
	    return;
	}

	for (PermissionTest test : tests) {
	    Set<PermissionEnum> permSet = session.getEffectivePermissions().get(test.getCe());
	    if (permSet == null) {
		continue;
	    }
	    if (permSet.contains(test.getPermission())) {
		LOG.debug(test.getPermission().name() + " was found for user " + user.getUsername());
		return;
	    }
	}

	throw new PermissionDeniedException(user.getUsername(), new PermissionEnum[] { tests[0].getPermission() },
		tests[0].getCe());
    }

    @Override
    public void requireLogin(Session session) throws LoginRequiredException {
	if (session != null) {
	    User user = session.getUser();
	    if (user != null) {
		if (session.getEffectivePermissions() != null) {
		    return;
		}
	    }
	}
	throw new LoginRequiredException();
    }

    @Override
    public void initializeManager() {
	// nothing to initialize
    }

    @Override
    public void addCreatorPermission(ControlEntity ce, Session session) throws OperationException,
	    DataConstraintException, ItemNotFoundException {
	// TODO:(2) some optimization could help here
	RoleDAO roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
	String adminRoleName = Configuration.ADMIN_ROLE_NAME;
	UserRole adminRole = roleDAO.getRoleByName(adminRoleName);
	roleDAO.assignRoleToOwner(adminRole, session.getUser(), ce);
	Set<UserPermission> permissions = roleDAO.getRolePermissions(adminRole);
	Set<PermissionEnum> permSet = Utils.convertPermissionSet(permissions);
	session.getEffectivePermissions().put(ce, permSet);
    }
}
