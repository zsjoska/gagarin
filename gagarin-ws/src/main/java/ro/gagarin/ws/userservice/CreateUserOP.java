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
    private AuthorizationManager authManager;
    private UserDAO userDAO;

    public CreateUserOP(String sessionId, WSUser user) {
	super(sessionId);
	this.user = user;
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());

    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	// the session user must have CREATE_USER permission
	authManager.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.CREATE);

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

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("username", user, true);
    }

}
