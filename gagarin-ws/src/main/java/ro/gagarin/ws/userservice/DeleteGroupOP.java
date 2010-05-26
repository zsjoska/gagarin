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
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// TODO:(1) the group may not have Id
	authMgr.requiresPermission(session, group, PermissionEnum.DELETE);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	userDAO.deleteGroup(this.group);
	userDAO.deleteGroupAssignments(this.group);
	getAuthorizationManager().removeControlEntityFromAssignment(getSession(), group);
	getAuthorizationManager().removePersonFromAssignment(session, group);
    }
}
