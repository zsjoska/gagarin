package ro.gagarin.dummyimpl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.ModelFactory;
import ro.gagarin.RoleDAO;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;

public class DummyAuthorizationManager implements AuthorizationManager {
	private static final transient Logger LOG = Logger.getLogger(DummyAuthorizationManager.class);

	@Override
	public void checkUserRole(Session session, User user) throws PermissionDeniedException {
		User sessionUser = session.getUser();
		RoleDAO roleManager = ModelFactory.getDAOManager().getRoleDAO(session);
		List<UserPermission> leftList = roleManager.substractUsersRolePermissions(user.getRole(),
				sessionUser.getRole());
		LOG.debug("left permissions:" + leftList.toString());

		if (leftList.size() != 0)
			throw new PermissionDeniedException(sessionUser.getUsername(), leftList.toString());

	}

	@Override
	public void requiresPermission(Session session, PermissionEnum reqPermission)
			throws PermissionDeniedException {
		UserDAO userManager = ModelFactory.getDAOManager().getUserDAO(session);

		User user = userManager.getUserByUsername(session.getUser().getUsername());

		Iterator<? extends UserPermission> iterator = user.getRole().getUserPermissions()
				.iterator();
		while (iterator.hasNext()) {
			UserPermission userPermission = iterator.next();
			if (userPermission.getPermissionName().equals(reqPermission.name())) {
				LOG.debug(reqPermission.name() + " was found for user " + user.getUsername());
				return;
			}

		}
		userManager.release();
		throw new PermissionDeniedException(user.getUsername(), reqPermission.name());
	}

}
