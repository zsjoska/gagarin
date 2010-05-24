/**
 * 
 */
package ro.gagarin.ws;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.ws.authentication.ActivateUserOP;
import ro.gagarin.ws.authentication.CreateSessionOP;
import ro.gagarin.ws.authentication.GetCurrentUserPermissionsOP;
import ro.gagarin.ws.authentication.LoginOP;
import ro.gagarin.ws.authentication.LogoutOP;
import ro.gagarin.ws.authentication.RegisterUserOP;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.executor.WebserviceExecutor;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSUser;

/**
 * @author zsjoska
 * 
 */
@WebService
public class Authentication {

    protected static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final transient Logger LOG = Logger.getLogger(Authentication.class);

    @WebMethod
    public String createSession(String language, String reason) throws WSException {

	CreateSessionOP createSession = new CreateSessionOP(language, reason);
	WebserviceExecutor.execute(createSession);
	return createSession.getSessionString();

    }

    @WebMethod
    public WSUser login(String sessionId, String username, String password, String[] extra) throws WSException {

	LOG.info("Login User " + username + "; extra:" + Arrays.toString(extra));
	LoginOP loginOP = new LoginOP(sessionId, username, password, extra);
	WebserviceExecutor.execute(loginOP);
	return loginOP.getLoginUser();
    }

    @WebMethod
    public void logout(String sessionId) throws WSException {
	WebserviceExecutor.execute(new LogoutOP(sessionId));
    }

    @WebMethod
    public HashMap<WSControlEntity, Set<PermissionEnum>> getCurrentUserPermissions(String sessionId) throws WSException {

	GetCurrentUserPermissionsOP getCurrentUserPermissions = new GetCurrentUserPermissionsOP(sessionId);
	WebserviceExecutor.execute(getCurrentUserPermissions);
	return getCurrentUserPermissions.getCurrentUserPermissions();

    }

    @WebMethod
    public String registerUser(String sessionId, WSUser user, String defGroup) throws WSException {
	RegisterUserOP registerUser = new RegisterUserOP(sessionId, user, defGroup);
	WebserviceExecutor.execute(registerUser);
	return registerUser.getConfirmationKey();
    }

    @WebMethod
    public WSUser activateUser(String activationKey) throws WSException {
	ActivateUserOP activateUser = new ActivateUserOP(activationKey);
	WebserviceExecutor.execute(activateUser);
	return activateUser.getUser();
    }

}
