package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.manager.PermissionTest;
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

	// TODO:(3) find a way to let to delete by name
	// this would mean to initialize managers before checkInput
	FieldValidator.requireLongField("id", group);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, new PermissionTest(group, PermissionEnum.DELETE), new PermissionTest(
		CommonControlEntities.GROUP_CE, PermissionEnum.DELETE));
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	userDAO.deleteGroup(this.group);
    }
}
