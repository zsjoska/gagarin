package ro.gagarin;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;

public class BasicAuthorizationManager implements AuthorizationManager {
	private static final transient Logger LOG = Logger.getLogger(BasicAuthorizationManager.class);

	@Override
	public void checkUserRole(Session session, User user) throws PermissionDeniedException,
			OperationException {
		User sessionUser = session.getUser();
		RoleDAO roleManager = session.getManagerFactory().getDAOManager().getRoleDAO(session);
		List<UserPermission> leftList = roleManager.substractUsersRolePermissions(user.getRole(),
				sessionUser.getRole());
		LOG.debug("left permissions:" + leftList.toString());

		if (leftList.size() != 0)
			throw new PermissionDeniedException(sessionUser.getUsername(), leftList.toString());

	}

	@Override
	public void requiresPermission(Session session, PermissionEnum reqPermission)
			throws PermissionDeniedException, OperationException {

		UserDAO userManager = session.getManagerFactory().getDAOManager().getUserDAO(session);
		RoleDAO roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);
		User user = null;
		try {

			user = session.getUser();
			Set<UserPermission> perm = roleDAO.getRolePermissions(user.getRole());

			Iterator<? extends UserPermission> iterator = perm.iterator();
			while (iterator.hasNext()) {
				UserPermission userPermission = iterator.next();
				if (userPermission.getPermissionName().equals(reqPermission.name())) {
					LOG.debug(reqPermission.name() + " was found for user " + user.getUsername());
					return;
				}

			}
			throw new PermissionDeniedException(user.getUsername(), reqPermission.name());
		} finally {
			try {
				userManager.release();
			} catch (OperationException e) {
				LOG.error("Exception releasing the manger", e);
				throw new PermissionDeniedException(user.getUsername(), reqPermission.name());
			}
		}
	}
}
