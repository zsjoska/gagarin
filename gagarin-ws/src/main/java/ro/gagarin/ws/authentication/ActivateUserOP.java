package ro.gagarin.ws.authentication;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserStatus;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class ActivateUserOP extends WebserviceOperation {

    private UserDAO userDAO;
    private WSUser user;

    public ActivateUserOP(String sessionId) {
	super(false, sessionId);
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	user = new WSUser(getSession().getUser());
    }

    @Override
    public void execute() throws ExceptionBase {

	WSUser wsUser = new WSUser();
	wsUser.setId(user.getId());
	wsUser.setStatus(UserStatus.ACTIVE);
	userDAO.updateUser(wsUser);

	// TODO:(3) Add notification call

	// ensure that the user on the session has the status ACTIVE
	user.setStatus(UserStatus.ACTIVE);
	getSession().setUser(user);
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	userDAO = FACTORY.getDAOManager().getUserDAO(getSession());
    }

    public WSUser getUser() {
	return user;
    }

}
