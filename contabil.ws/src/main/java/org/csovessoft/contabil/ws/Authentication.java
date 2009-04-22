/**
 * 
 */
package org.csovessoft.contabil.ws;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.exceptions.SessionNotFoundException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.session.SessionManager;
import org.csovessoft.contabil.user.User;
import org.csovessoft.contabil.user.UserManager;

/**
 * @author zsjoska
 * 
 */
@WebService
public class Authentication {

	@WebMethod
	public String testMethod() {
		return "testMethod " + new Date();
	}

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

		UserManager userManager = ModelFactory.getUserManager();
		SessionManager sessionManager = ModelFactory.getSessionManager();
		User user = userManager.userLogin(username, password);
		Session session = sessionManager.getSessionByID(sessionID);

		session.setUser(user);
		return true;
	}

	@WebMethod
	public void logout(String sessionId) {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		sessionManager.logout(sessionId);
	}

}
