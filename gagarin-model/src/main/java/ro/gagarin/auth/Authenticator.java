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

    /**
     * Encrypts the password for storing in the database. Usually calculating a
     * hash.<br>
     * Authenticators with external authentication source may use this method to
     * return empty value to not store the password locally.
     * 
     * @param password
     *            unencrypted password
     * @return encrypted (hashed) password
     * @throws OperationException
     */
    String encryptPassword(String password) throws OperationException;
}
