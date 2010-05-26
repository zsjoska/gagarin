package ro.gagarin.ws.userservice;

import java.security.acl.Group;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;

public class UpdateGroupOP extends WebserviceOperation {

    private final WSGroup group;

    private AuthorizationManager authManager;

    private UserDAO userDAO;

    public UpdateGroupOP(String sessionId, WSGroup group) {
	super(sessionId);
	this.group = group;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// id is required for identification
	FieldValidator.requireLongField("id", group);

	if (group.getName() == null && group.getDescription() == null) {
	    throw new FieldRequiredException("name or description", Group.class);
	}

	if (group.getName() != null) {
	    FieldValidator.requireStringField("name", group, true);
	}
	if (group.getDescription() != null) {
	    FieldValidator.requireStringField("description", group, false);
	}
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	// TODO:(1) The group Id here could be null
	authManager.requiresPermission(session, group, PermissionEnum.UPDATE);
	userDAO.updateGroup(this.group);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

}
