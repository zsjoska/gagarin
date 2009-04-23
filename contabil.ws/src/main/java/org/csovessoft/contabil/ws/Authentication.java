/**
 * 
 */
package org.csovessoft.contabil.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

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

	@WebMethod
	public Session createSession(String language, String reason) {
		if (language == null) {
			language = "en_us";
		}
		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.createSession(language, reason);
		return session;

	}

	@WebMethod
	public boolean login(String sessionID, String username, String password, String[] extra)
			throws SessionNotFoundException, UserNotFoundException {

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.getSessionById(sessionID);
		if (session == null)
			throw new SessionNotFoundException(sessionID);

		UserManager userManager = ModelFactory.getUserManager();
		User user = userManager.userLogin(username, password);

		session.setUser(user);
		return true;
	}

	@WebMethod
	public void logout(String sessionId) {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		sessionManager.logout(sessionId);
	}

}
