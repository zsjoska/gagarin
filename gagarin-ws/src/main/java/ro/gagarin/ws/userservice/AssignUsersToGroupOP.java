package ro.gagarin.ws.userservice;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSUser;

public class AssignUsersToGroupOP extends WebserviceOperation {

    private final WSGroup group;
    private final WSUser[] users;

    private AuthorizationManager authManager;

    private UserDAO userManager;

    public AssignUsersToGroupOP(String sessionId, WSGroup group, WSUser[] users) {
	super(sessionId);
	this.group = group;
	this.users = users;
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	if (group == null)
	    throw new FieldRequiredException("group", Group.class);
	if (group.getId() == null && group.getName() == null)
	    throw new FieldRequiredException("id or name", Group.class);
	// TODO: check users
    }

    @Override
    public void execute() throws ExceptionBase {
	authManager.requiresPermission(getSession(), PermissionEnum.UPDATE_GROUP);

	for (WSUser user : this.users) {
	    userManager.assignUserToGroup(user, group);
	}

    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager(getSession());
	userManager = FACTORY.getDAOManager().getUserDAO(getSession());
    }

}
