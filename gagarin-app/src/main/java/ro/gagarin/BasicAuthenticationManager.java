package ro.gagarin;

import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.AuthenticationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public class BasicAuthenticationManager implements AuthenticationManager {

    public BasicAuthenticationManager() {
    }

    @Override
    public User userLogin(Session session, String username, String password, String[] extra)
	    throws ItemNotFoundException, OperationException {
	ManagerFactory factory = session.getManagerFactory();
	UserDAO userDAO = factory.getDAOManager().getUserDAO(session);

	// TODO:(2) rewrite to get the user first, then authenticate in a
	// pluggable way
	User user = userDAO.userLogin(username, password);
	factory.getSessionManager().assignUserToSession(user, session);
	return user;
    }

    @Override
    public void initializeManager() {
	// nothing to initialize
    }
}
