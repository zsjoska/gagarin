/**
 * 
 */
package org.csovessoft.contabil.ws;

import java.util.Arrays;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.SessionManager;
import org.csovessoft.contabil.UserManager;
import org.csovessoft.contabil.exceptions.SessionNotFoundException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.user.User;

/**
 * @author zsjoska
 * 
 */
@WebService
public class Authentication {

	private static final transient Logger LOG = Logger.getLogger(Authentication.class);

	@WebMethod
	public Session createSession(String language, String reason) {
		if (language == null) {
			language = "en_us";
		}
		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.createSession(language, reason);
		LOG.info("Session created:" + session.getId() + "; reason:" + session.getReason()
				+ "; language:" + session.getLanguage());
		return session;

	}

	@WebMethod
	public boolean login(Long sessionID, String username, String password, String[] extra)
			throws SessionNotFoundException, UserNotFoundException {

		LOG.info("Login User " + username + "; extra:" + Arrays.toString(extra));

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.getSessionById(sessionID);
		if (session == null)
			throw new SessionNotFoundException(sessionID);

		UserManager userManager = ModelFactory.getUserManager();
		User user = userManager.userLogin(username, password);

		session.setUser(user);
		LOG.info("User " + user.getId() + ":" + user.getUsername() + " was bound to session "
				+ session.getId());
		return true;
	}

	@WebMethod
	public void logout(Long sessionId) {
		LOG.info("Session logout " + sessionId);
		SessionManager sessionManager = ModelFactory.getSessionManager();
		sessionManager.logout(sessionId);
	}

}
