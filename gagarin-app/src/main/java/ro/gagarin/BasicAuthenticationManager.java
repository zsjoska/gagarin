package ro.gagarin;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.AuthenticationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public class BasicAuthenticationManager implements AuthenticationManager {

    private final Session session;

    private ManagerFactory factory;

    public BasicAuthenticationManager(Session session) {
	this.session = session;
	this.factory = session.getManagerFactory();
    }

    @Override
    public User userLogin(String username, String password, String[] extra) throws ItemNotFoundException,
	    OperationException {
	UserDAO userDAO = factory.getDAOManager().getUserDAO(this.session);
	User user = userDAO.userLogin(username, password);
	factory.getSessionManager().assignUserToSession(user, session);
	return user;
    }

    @Override
    public void initializeManager() {
	// nothing to initialize
    }
}
