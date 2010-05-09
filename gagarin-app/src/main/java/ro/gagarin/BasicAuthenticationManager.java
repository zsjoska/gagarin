package ro.gagarin;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public class BasicAuthenticationManager implements AuthenticationManager {

    private static final transient Logger LOG = Logger.getLogger(BasicAuthenticationManager.class);

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

	// TODO: change it a bit to look more important
	this.session.setUser(user);
	LOG.info("User " + user.getId() + ":" + user.getUsername() + " was bound to session " + session.getId());
	return user;
    }

    @Override
    public void initializeManager() {
	// nothing to initialize
    }
}
