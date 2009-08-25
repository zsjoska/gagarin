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
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.LoginRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.util.WSConversionUtils;

/**
 * @author zsjoska
 * 
 */
@WebService
public class Authentication {

    private static final transient Logger LOG = Logger.getLogger(Authentication.class);
    private static final transient ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    @WebMethod
    public String createSession(String language, String reason) {

	SessionManager sessionManager = FACTORY.getSessionManager();

	if (language == null) {
	    // TODO move this to the configuration
	    language = "en_us";
	}

	Session session = sessionManager.createSession(language, reason, FACTORY);
	LOG.info("Session created:" + session.getId() + "; reason:" + session.getReason() + "; language:"
		+ session.getLanguage());

	return session.getSessionString();

    }

    @WebMethod
    public WSUser login(String sessionID, String username, String password, String[] extra)
	    throws SessionNotFoundException, ItemNotFoundException, OperationException {

	LOG.info("Login User " + username + "; extra:" + Arrays.toString(extra));

	SessionManager sessionManager = FACTORY.getSessionManager();
	Session session = sessionManager.acquireSession(sessionID);
	if (session == null)
	    throw new SessionNotFoundException(sessionID);

	try {

	    User user = FACTORY.getAuthenticationManager(session).userLogin(username, password, extra);
	    return new WSUser(user);
	} finally {
	    FACTORY.releaseSession(session);
	}
    }

    @WebMethod
    public void logout(String sessionId) {
	LOG.info("Session logout " + sessionId);
	SessionManager sessionManager = FACTORY.getSessionManager();
	sessionManager.logout(sessionId);
    }

    @WebMethod
    public Set<WSUserPermission> getCurrentUserPermissions(String sessionId) throws SessionNotFoundException,
	    OperationException, LoginRequiredException {

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
	}
    }
}
