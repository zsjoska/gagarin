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

public class DeleteGroupOP extends WebserviceOperation {

    private final WSGroup group;

    private AuthorizationManager authManager;

    private UserDAO userDAO;

    public DeleteGroupOP(String sessionId, WSGroup group) {
	super(sessionId);
	this.group = group;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	if (group.getId() == null && group.getName() == null) {
	    throw new FieldRequiredException("id or name", Group.class);
	}
	if (group.getId() != null)
	    FieldValidator.requireLongField("id", group);
	if (group.getName() != null)
	    FieldValidator.requireStringField("name", group, true);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	// TODO:(1) the group may not have Id

	authManager.requiresPermission(session, group, PermissionEnum.DELETE);
	userDAO.deleteGroup(this.group);
	userDAO.deleteGroupAssignments(this.group);
	authManager.removeControlEntityFromAssignment(getSession(), group);
	authManager.removePersonFromAssignment(session, group);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }
}
