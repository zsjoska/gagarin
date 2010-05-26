package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthorizationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetUsersOP extends WebserviceOperation {

    private List<WSUser> users;

    private AuthorizationManager authManager;

    private UserDAO userDAO;

    public GetUsersOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authManager = FACTORY.getAuthorizationManager();
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	// the session user must have LIST_USERS permission
	authManager.requiresPermission(session, BaseControlEntity.getAdminEntity(), PermissionEnum.LIST);

	List<User> allUsers = userDAO.getAllUsers();
	this.users = WSConversionUtils.convertToWSUserList(allUsers);

    }

    public List<WSUser> getUsers() {
	return this.users;
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

}
