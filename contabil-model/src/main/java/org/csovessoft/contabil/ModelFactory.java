package org.csovessoft.contabil;

import org.csovessoft.contabil.session.DummySessionManager;
import org.csovessoft.contabil.session.SessionManager;
import org.csovessoft.contabil.user.DummyUserManager;
import org.csovessoft.contabil.user.UserManager;

public class ModelFactory {

	public static SessionManager getSessionManager() {
		return DummySessionManager.getInstance();
		// TODO Auto-generated method stub
		
	}

	public static UserManager getUserManager() {
		return DummyUserManager.getInstance();
		// TODO Auto-generated method stub
		
	}
}
