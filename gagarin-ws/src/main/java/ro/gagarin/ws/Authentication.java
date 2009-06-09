/**
 * 
 */
package ro.gagarin.ws;

import java.util.Arrays;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

/**
 * @author zsjoska
 * 
 */
@WebService
public class Authentication {

	private static final transient Logger LOG = Logger.getLogger(Authentication.class);

	@WebMethod
	public String createSession(String language, String reason) {

		SessionManager sessionManager = BasicManagerFactory.getInstance().getSessionManager();

		if (language == null) {
			// TODO move this to the configuration
			language = "en_us";
		}

		Session session = sessionManager.createSession(language, reason, BasicManagerFactory
				.getInstance());
		LOG.info("Session created:" + session.getId() + "; reason:" + session.getReason()
				+ "; language:" + session.getLanguage());

		return session.getSessionString();

	}

	@WebMethod
	public boolean login(String sessionID, String username, String password, String[] extra)
			throws SessionNotFoundException, ItemNotFoundException {

		LOG.info("Login User " + username + "; extra:" + Arrays.toString(extra));

		ManagerFactory factory = BasicManagerFactory.getInstance();

		SessionManager sessionManager = factory.getSessionManager();
		Session session = sessionManager.acquireSession(sessionID);
		if (session == null)
			throw new SessionNotFoundException(sessionID);

		try {

			UserDAO userManager = factory.getDAOManager().getUserDAO(session);
			User user = userManager.userLogin(username, password);

			session.setUser(user);
			LOG.info("User " + user.getId() + ":" + user.getUsername() + " was bound to session "
					+ session.getId());
			return true;
		} finally {
			factory.releaseSession(session);
		}
	}

	@WebMethod
	public void logout(String sessionId) {
		LOG.info("Session logout " + sessionId);
		SessionManager sessionManager = BasicManagerFactory.getInstance().getSessionManager();
		sessionManager.logout(sessionId);
	}
}
