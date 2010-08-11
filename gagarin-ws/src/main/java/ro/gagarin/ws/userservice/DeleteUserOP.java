package ro.gagarin.ws.userservice;

import ro.gagarin.CommonControlEntities;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class DeleteUserOP extends WebserviceOperation {

    private final WSUser user;
    private UserDAO userDAO;

    public DeleteUserOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireIdField(this.user);
	// TODO:(3) enable delete by username
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	authMgr.requiresPermission(session, CommonControlEntities.USER_CE, PermissionEnum.DELETE);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(session);
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	userDAO.deleteUser(user);
	// TODO:(2) LOG
    }
}
