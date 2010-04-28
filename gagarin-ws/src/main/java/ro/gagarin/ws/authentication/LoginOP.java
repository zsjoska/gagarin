/**
 * 
 */
package ro.gagarin.ws.authentication;

import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.user.User;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.executor.WebserviceOperation;
import ro.gagarin.ws.objects.WSUser;

/**
 * @author zsjoska
 * 
 */
public class LoginOP extends WebserviceOperation {

    private static final Statistic STAT_LOGIN = new Statistic("ws.auth.login");
    private WSUser loginUser = null;
    private final String username;
    private final String password;
    private final String[] extra;

    public LoginOP(String sessionID, String username, String password, String[] extra) {
	super(sessionID, LoginOP.class);
	this.username = username;
	this.password = password;
	this.extra = extra;
    }

    @Override
    public void execute() throws ExceptionBase {
	User user = FACTORY.getAuthenticationManager(getSession()).userLogin(username, password, extra);
	this.loginUser = new WSUser(user);
    }

    @Override
    public Statistic getStatistic() {
	return STAT_LOGIN;
    }

    public WSUser getLoginUser() {
	return this.loginUser;
    }

}
