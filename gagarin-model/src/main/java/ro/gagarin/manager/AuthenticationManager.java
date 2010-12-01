package ro.gagarin.manager;

import java.util.List;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public interface AuthenticationManager extends BaseManager {

    /**
     * Performs a login operation with username, password and other parameters
     * defined by the implementation.<br>
     * Upon successful credential verfication, the implementation of this method
     * should call the
     * {@link SessionManager#assignUserToSession(User, ro.gagarin.session.Session)}
     * 
     * @param session
     * @param username
     * @param password
     * @param extra
     * @return
     * @throws ItemNotFoundException
     * @throws OperationException
     */
    User userLogin(Session session, String username, String password, String[] extra) throws ItemNotFoundException,
	    OperationException;

    List<String> getAuthenticatorNames();

    /**
     * Encrypts the password to be stored in the database.<br>
     * The password encryption method depends on the implementation of the given
     * authentication type.
     * 
     * @param authentication
     *            authentication type string
     * @param password
     *            the original cleartext password
     * @return the encrypted password
     * @throws OperationException
     */
    String encryptPasswordForStorage(String authentication, String password) throws OperationException;

}
