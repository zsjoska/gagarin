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
    public void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());

    }

    @Override
    public void execute() throws ExceptionBase {

	// the session user must have CREATE_USER permission
	authManager.requiresPermission(getSession(), BaseControlEntity.getAdminEntity(), PermissionEnum.CREATE);

	this.setUserId(userDAO.createUser(user));
	getApplog().info("Created User " + user.getId() + ":" + user.getUsername() + "; session:" + getSessionString());
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    public long getUserId() {
	return userId;
    }

    @Override
    public String toString() {
	return "CreateUserOP [user=" + user + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	FieldValidator.requireStringField("username", user, true);
    }

}
