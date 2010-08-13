package ro.gagarin.ws.userservice;

import java.security.acl.Group;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.PermissionTest;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSGroup;

public class UpdateGroupOP extends WebserviceOperation {

    private final WSGroup group;

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
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, new PermissionTest(group, PermissionEnum.UPDATE), new PermissionTest(
		CommonControlEntities.GROUP_CE, PermissionEnum.UPDATE));
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	userDAO.updateGroup(this.group);
    }
}
