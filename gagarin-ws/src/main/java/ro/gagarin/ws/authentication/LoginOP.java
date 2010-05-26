/**
 * 
 */
package ro.gagarin.ws.authentication;

import java.util.Arrays;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.manager.AuthenticationManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class LoginOP extends WebserviceOperation {

    private WSUser loginUser = null;
    private String username;
    private String password;
    private final String[] extra;
    private AuthenticationManager authenticationManager;

    public LoginOP(String sessionID, String username, String password, String[] extra) {
	super(false, sessionID);
	this.username = username;
	this.password = password;
	this.extra = extra;
    }

    @Override
    protected void prepareManagers(Session session) throws ExceptionBase {
	authenticationManager = FACTORY.getAuthenticationManager(getSession());
    }

    @Override
    protected void execute(Session session) throws ExceptionBase {
	User user = authenticationManager.userLogin(username, password, extra);
	// TODO:(2) check if user is active
	this.loginUser = new WSUser(user);
	getApplog().info("Login completed for user " + this.username);
    }

    public WSUser getLoginUser() {
	return this.loginUser;
    }

    @Override
    public String toString() {
	return "LoginOP [extra=" + Arrays.toString(extra) + ", password=" + password + ", username=" + username + "]";
    }

    @Override
    protected void checkInput(Session session) throws ExceptionBase {
	this.username = FieldValidator.requireStringValue(username, "username", 50);
	this.password = FieldValidator.requireStringValue(password, "password", 50);
    }
}
