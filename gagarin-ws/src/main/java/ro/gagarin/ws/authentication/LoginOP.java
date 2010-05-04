/**
 * 
 */
package ro.gagarin.ws.authentication;

import java.util.Arrays;

import ro.gagarin.AuthenticationManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

public class LoginOP extends WebserviceOperation {

    private static final Statistic STAT_LOGIN = Statistic.getByName("ws.auth.login");
    private WSUser loginUser = null;
    private String username;
    private String password;
    private final String[] extra;
    private AuthenticationManager authenticationManager;

    public LoginOP(String sessionID, String username, String password, String[] extra) {
	super(false, sessionID, LoginOP.class);
	this.username = username;
	this.password = password;
	this.extra = extra;
    }

    @Override
    public void prepareManagers(Session session) throws ExceptionBase {
	authenticationManager = FACTORY.getAuthenticationManager(getSession());
    }

    @Override
    public void execute() throws ExceptionBase {
	User user = authenticationManager.userLogin(username, password, extra);
	this.loginUser = new WSUser(user);
	getApplog().info("Login completed for user " + this.username);
    }

    @Override
    public Statistic getStatistic() {
	return STAT_LOGIN;
    }

    public WSUser getLoginUser() {
	return this.loginUser;
    }

    @Override
    public String toString() {
	return "LoginOP [extra=" + Arrays.toString(extra) + ", password=" + password + ", username=" + username + "]";
    }

    @Override
    public void checkInput(Session session) throws ExceptionBase {
	this.username = FieldValidator.checkStringValue(username, "username", 50);
	this.password = FieldValidator.checkStringValue(password, "password", 50);
    }
}
