/**
 * 
 */
package ro.gagarin.ws;

import java.util.Arrays;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.RoleDAO;
import ro.gagarin.SessionManager;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.Statistic;
import ro.gagarin.ws.authentication.CreateSessionOP;
import ro.gagarin.ws.authentication.LoginOP;
import ro.gagarin.ws.executor.WebserviceExecutor;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

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
    public WSUser login(String sessionID, String username, String password, String[] extra) throws WSException {

	LOG.info("Login User " + username + "; extra:" + Arrays.toString(extra));
	LoginOP loginOP = new LoginOP(sessionID, username, password, extra);
	WebserviceExecutor.execute(loginOP);
	return loginOP.getLoginUser();
    }

    private static final Statistic STAT_LOGOUT = new Statistic("ws.auth.logout");

    @WebMethod
    public void logout(String sessionId) {
	long start = System.currentTimeMillis();
	LOG.info("Session logout " + sessionId);
	SessionManager sessionManager = FACTORY.getSessionManager();
	sessionManager.logout(sessionId);
	STAT_LOGOUT.addDuration(System.currentTimeMillis() - start);
    }

    private static final Statistic STAT_CURRENT_USER_PERMISSION = new Statistic("ws.auth.getCurrentUserPermissions");

    @WebMethod
    public Set<WSUserPermission> getCurrentUserPermissions(String sessionId) throws SessionNotFoundException,
	    OperationException, LoginRequiredException {

	long start = System.currentTimeMillis();

	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionId);
	if (session == null)
	    throw new SessionNotFoundException(sessionId);

	try {

	    FACTORY.getAuthorizationManager(session).requireLogin(session);

	    RoleDAO roleDAO = session.getManagerFactory().getDAOManager().getRoleDAO(session);

	    Set<UserPermission> perm = roleDAO.getRolePermissions(session.getUser().getRole());
	    return WSConversionUtils.convertToWSPermissionSet(perm);
	} finally {
	    FACTORY.releaseSession(session);
	    STAT_CURRENT_USER_PERMISSION.addDuration(System.currentTimeMillis() - start);
	}
    }
}
