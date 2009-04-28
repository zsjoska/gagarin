package ro.gagarin.user;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.ModelFactory;
import ro.gagarin.RoleManager;
import ro.gagarin.exceptions.PermissionDeniedException;
import ro.gagarin.session.Session;

public class DummyAuthorizationManager implements AuthorizationManager {
	private static final transient Logger LOG = Logger.getLogger(DummyAuthorizationManager.class);

	@Override
	public void checkUserRole(Session session, User user) throws PermissionDeniedException {
		User sessionUser = session.getUser();
		RoleManager roleManager = ModelFactory.getRoleManager(session);
		ArrayList<UserPermission> leftList = roleManager.substractUsersRolePermissions(user
				.getRole(), sessionUser.getRole());
		LOG.debug("left permissions:" + leftList.toString());
		roleManager.release();
		if (leftList.size() != 0)
			throw new PermissionDeniedException(sessionUser.getUsername(), leftList.toString());

	}

	@Override
	public void requiresPermission(Session session, PermissionEnum reqPermission)
			throws PermissionDeniedException {
		User user = session.getUser();
		Iterator<UserPermission> iterator = user.getRole().getUserPermissions().iterator();
		while (iterator.hasNext()) {
			UserPermission userPermission = (UserPermission) iterator.next();
			if (userPermission.getPermissionName().equals(reqPermission.name())) {
				LOG.debug(reqPermission.name() + " was found for user " + user.getUsername());
				return;
			}

		}
		throw new PermissionDeniedException(user.getUsername(), reqPermission.name());
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}

}
