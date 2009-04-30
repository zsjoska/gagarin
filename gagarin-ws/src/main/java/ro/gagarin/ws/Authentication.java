/**
 * 
 */
package ro.gagarin.ws;

import java.util.Arrays;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.ModelFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.UserManager;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserNotFoundException;
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

		SessionManager sessionManager = ModelFactory.getSessionManager();
		try {

			if (language == null) {
				language = "en_us";
			}

			Session session = sessionManager.createSession(language, reason);
			LOG.info("Session created:" + session.getId() + "; reason:" + session.getReason()
					+ "; language:" + session.getLanguage());

			return session.getSessionString();

		} finally {
			sessionManager.release();
		}
	}

	@WebMethod
	public boolean login(String sessionID, String username, String password, String[] extra)
			throws SessionNotFoundException, UserNotFoundException {

		LOG.info("Login User " + username + "; extra:" + Arrays.toString(extra));

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.acquireSession(sessionID);
		if (session == null)
			throw new SessionNotFoundException(sessionID);

		try {

			UserManager userManager = ModelFactory.getUserManager(session);
			User user = userManager.userLogin(username, password);

			session.setUser(user);
			LOG.info("User " + user.getId() + ":" + user.getUsername() + " was bound to session "
					+ session.getId());
			return true;
		} finally {
			ModelFactory.releaseSession(session);
		}
	}

	@WebMethod
	public void logout(String sessionId) {
		LOG.info("Session logout " + sessionId);
		SessionManager sessionManager = ModelFactory.getSessionManager();
		try {
			sessionManager.logout(sessionId);
		} finally {
			sessionManager.release();
		}
	}
}
