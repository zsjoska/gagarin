package ro.gagarin.ws.userservice;

import java.util.List;

import ro.gagarin.AuthorizationManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.util.WSConversionUtils;

public class GetUsersOP extends WebserviceOperation {

    private static final Statistic STAT_GET_USERS = new Statistic("ws.userserservice.getUsers");

    private List<WSUser> users;

    public GetUsersOP(String sessionId) {
	super(sessionId, GetUsersOP.class);
    }

    @Override
    public void execute() throws ExceptionBase {
	AuthorizationManager authManager = FACTORY.getAuthorizationManager(getSession());

	UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(getSession());

	// the session user must have LIST_USERS permission
	authManager.requiresPermission(getSession(), PermissionEnum.LIST_USERS);

	List<User> allUsers = userDAO.getAllUsers();
	this.users = WSConversionUtils.convertToWSUserList(allUsers);

    }

    @Override
    public Statistic getStatistic() {
	return STAT_GET_USERS;
    }

    public List<WSUser> getUsers() {
	return this.users;
    }

}
