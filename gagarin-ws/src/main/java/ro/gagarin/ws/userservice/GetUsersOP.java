package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.CommonControlEntities;
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

    private UserDAO userDAO;

    public GetUsersOP(String sessionId) {
	super(sessionId);
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	// no input
    }

    @Override
    protected void checkPermissions(Session session, AuthorizationManager authMgr) throws ExceptionBase {
	// the session user must have LIST permission
	authMgr.requiresPermission(session, CommonControlEntities.USER_CE, PermissionEnum.LIST);
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {

	List<User> allUsers = userDAO.getAllUsers();
	this.users = WSConversionUtils.convertToWSUserList(allUsers);
	getApplog().debug("Returning " + users.size() + " users");
    }

    public List<WSUser> getUsers() {
	return this.users;
    }
}
