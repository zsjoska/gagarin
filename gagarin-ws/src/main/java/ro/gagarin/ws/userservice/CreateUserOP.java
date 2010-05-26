package ro.gagarin.ws.userservice;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class CreateUserOP extends WebserviceOperation {

    private final WSUser user;
    private long userId = -1;
    private UserDAO userDAO;

    public CreateUserOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("username", user, true);
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the session user must have CREATE_USER permission
	authMgr.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.CREATE);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	this.userId = userDAO.createUser(user);
	getApplog().info("Created User " + user.getId() + ":" + user.getUsername() + "; session:" + getSessionString());
    }

    public long getUserId() {
	return userId;
    }

    @Override
    public String toString() {
	return "CreateUserOP [user=" + user + "]";
    }
}
