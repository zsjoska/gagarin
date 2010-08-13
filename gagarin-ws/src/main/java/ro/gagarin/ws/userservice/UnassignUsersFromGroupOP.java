package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.PermissionTest;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSUser;

public class UnassignUsersFromGroupOP extends WebserviceOperation {

    private final WSGroup group;
    private final WSUser[] users;
    private UserDAO userDAO;

    public UnassignUsersFromGroupOP(String sessionId, WSGroup group, WSUser[] users) {
	super(sessionId);
	this.group = group;
	this.users = users;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	if (group == null)
	    throw new FieldRequiredException("group", Group.class);
	if (group.getId() == null && group.getName() == null)
	    throw new FieldRequiredException("id or name", Group.class);
	// TODO:(2) check users
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, new PermissionTest(group, PermissionEnum.ADMIN), new PermissionTest(
		CommonControlEntities.GROUP_CE, PermissionEnum.ADMIN));
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	for (WSUser user : this.users) {
	    userDAO.unassignUserFromGroup(user, group);
	}
    }
}
