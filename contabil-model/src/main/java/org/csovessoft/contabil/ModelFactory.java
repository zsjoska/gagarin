package org.csovessoft.contabil;

import org.csovessoft.contabil.config.FileConfigurationManager;
import org.csovessoft.contabil.session.DummySessionManager;
import org.csovessoft.contabil.user.DummyUserManager;

public class ModelFactory {

	public static SessionManager getSessionManager() {
		return DummySessionManager.getInstance();
		// TODO Auto-generated method stub

	}

	public static UserManager getUserManager() {
		return DummyUserManager.getInstance();
		// TODO Auto-generated method stub

	}

	public static ConfigurationManager getConfigurationManager() {
		// TODO Auto-generated method stub
		return FileConfigurationManager.getInstance();
	}
}
