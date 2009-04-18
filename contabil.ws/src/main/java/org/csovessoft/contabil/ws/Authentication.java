/**
 * 
 */
package org.csovessoft.contabil.ws;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.results.MLoginResult;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.session.SessionManager;
import org.csovessoft.contabil.user.UserManager;

/**
 * @author zsjoska
 * 
 */
@WebService
public class Authentication {

	@WebMethod
	public String testMethod() {
		SessionManager sessionManager = ModelFactory.getSessionManager();
		return "testMethod " + new Date();
	}
	
	@WebMethod
	public String login(String username, String password, String reason) {
		
		UserManager userManager = ModelFactory.getUserManager();
		MLoginResult login = userManager.login(username,password);
		if( login.suceeded()){
			SessionManager sessionManager = ModelFactory.getSessionManager();
			Session storeSession = sessionManager.storeNewSession(username,reason);
		}
		
		

		return null;
		
	}

}
