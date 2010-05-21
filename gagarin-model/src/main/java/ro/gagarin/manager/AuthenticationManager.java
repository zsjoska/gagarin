package ro.gagarin.manager;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.User;

public interface AuthenticationManager extends BaseManager {

    /**
     * Performs a login operation with username, password and other parameters
     * defined by the implementation.<br>
     * Upon successful credential verfication, the implementation of this method
     * should call the
     * {@link SessionManager#assignUserToSession(User, ro.gagarin.session.Session)}
     * 
     * @param username
     * @param password
     * @param extra
     * @return
     * @throws ItemNotFoundException
     * @throws OperationException
     */
    User userLogin(String username, String password, String[] extra) throws ItemNotFoundException, OperationException;

}
