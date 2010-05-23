package ro.gagarin;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.config.Config;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;
import ro.gagarin.util.Utils;

public class BasicAuthorizationManager implements AuthorizationManager {
    private static final transient Logger LOG = Logger.getLogger(BasicAuthorizationManager.class);

    @Deprecated
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
	    // TODO: reqPermission[0].name() is not the right way... and we have
	    // to track the object ID and name too
	    throw new PermissionDeniedException(user.getUsername(), reqPermission[0].name());
	}

	for (PermissionEnum reqPerm : reqPermission) {
	    if (permSet.contains(reqPerm)) {
		LOG.debug(reqPerm.name() + " was found for user " + user.getUsername());
		return;
	    }
	}

	// check if it has the permission for ADMIN_ENTITY
	permSet = session.getEffectivePermissions().get(BaseControlEntity.getAdminEntity());
	for (PermissionEnum reqPerm : reqPermission) {
	    if (permSet.contains(reqPerm)) {
		LOG.debug("ADMIN " + reqPerm.name() + " was found for user " + user.getUsername());
		return;
	    }
	}

	// TODO: reqPermission[0].name() is not the right way... and we have
	// to track the object ID and name too
	throw new PermissionDeniedException(user.getUsername(), reqPermission[0].name());
    }

    @Deprecated
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
	// TODO: some optimization could help here
	ConfigurationManager cfgMgr = session.getManagerFactory().getConfigurationManager();
	RoleDAO roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
	String adminRoleName = cfgMgr.getString(Config.ADMIN_ROLE_NAME);
	UserRole adminRole = roleDAO.getRoleByName(adminRoleName);
	roleDAO.assignRoleToPerson(adminRole, session.getUser(), ce);
	Set<UserPermission> permissions = roleDAO.getRolePermissions(adminRole);
	Set<PermissionEnum> permSet = Utils.convertPermissionSet(permissions);
	session.getEffectivePermissions().put(ce, permSet);
    }
}
