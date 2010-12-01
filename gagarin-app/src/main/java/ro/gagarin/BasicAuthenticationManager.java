package ro.gagarin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.gagarin.application.objects.AppUser;
import ro.gagarin.auth.Authenticator;
import ro.gagarin.auth.AuthenticatorPool;
import ro.gagarin.auth.BaseAuthenticator;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
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
	AppLog log = factory.getLogManager().getLoggingSession(session, getClass());

	User foundUser = userDAO.getUserByUsername(username);
	if (foundUser != null) {
	    String userAuthType = foundUser.getAuthentication();
	    Authenticator authenticator = AuthenticatorPool.getAuthenticatorForName(userAuthType);

	    boolean pass = authenticator.verifyCredentials(session, username, password, extra);
	    if (!pass) {
		log.warn("Authentication of type " + userAuthType + " failed for user " + username);
		throw new ItemNotFoundException(User.class, username + " with password");
	    } else {
		log.info("Authentication of type " + userAuthType + " succeeded for user " + username);
	    }

	} else {

	    Collection<Authenticator> authenticators = AuthenticatorPool.getAuthenticators();
	    boolean pass = false;
	    for (Authenticator authenticator : authenticators) {
		if (authenticator.isLazyCreationSupported()) {
		    log.info("Trying to authenticate user " + username + " with authenticator "
			    + authenticator.getName());
		    pass = authenticator.verifyCredentials(session, username, password, extra);
		    if (pass) {
			log.info("Authentication of type " + authenticator.getName() + " succeeded for user "
				+ username);
			User user = authenticator.fillUserDetails(username, password, extra);
			try {
			    long id = userDAO.createUser(user);
			    AppUser tempUser = new AppUser(user);
			    tempUser.setId(id);
			    foundUser = tempUser;

			} catch (DataConstraintException e) {
			    log.error("Could not lazy-create authenticated user:" + username, e);
			    throw new OperationException(e.getErrorCode(), e);
			}
			break;
		    }
		}
	    }
	    if (!pass) {
		log.warn("Authentication discovery failed for user " + username);
		throw new ItemNotFoundException(User.class, username + " with password");
	    }

	}

	factory.getSessionManager().assignUserToSession(foundUser, session);
	return foundUser;
    }

    @Override
    public void initializeManager() {
	new InternalAuthenticator();
    }

    @Override
    public List<String> getAuthenticatorNames() {
	ArrayList<String> list = new ArrayList<String>();
	Collection<Authenticator> authenticators = AuthenticatorPool.getAuthenticators();
	for (Authenticator authenticator : authenticators) {
	    list.add(authenticator.getName());
	}
	return list;
    }

    private class InternalAuthenticator extends BaseAuthenticator {
	public InternalAuthenticator() {
	    super("INTERNAL");
	}

	@Override
	public boolean isLazyCreationSupported() {
	    return false;
	}

	@Override
	public boolean verifyCredentials(Session session, String username, String password, String[] extra)
		throws OperationException {
	    UserDAO userDAO = session.getManagerFactory().getDAOManager().getUserDAO(session);
	    try {
		userDAO.userLogin(username, password);
	    } catch (ItemNotFoundException e) {
		return false;
	    }
	    return true;
	}

	@Override
	public User fillUserDetails(String username, String password, String[] extra) {
	    throw new RuntimeException("Authenticator " + getName() + " does not support lazy creation");
	}
    }

}
