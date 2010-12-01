package ro.gagarin.auth;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public interface Authenticator {
    String getName();

    boolean isLazyCreationSupported();

    boolean verifyCredentials(Session session, String username, String password, String[] extra)
	    throws OperationException;

    /**
     * Creates a system user object based on the given credentials
     * 
     * @param username
     * @param password
     * @param extra
     * @return
     */
    User fillUserDetails(String username, String password, String[] extra);
}
