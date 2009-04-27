package org.csovessoft.contabil;

import org.csovessoft.contabil.config.FileConfigurationManager;
import org.csovessoft.contabil.session.BasicSessionManager;
import org.csovessoft.contabil.user.DummyUserManager;

/**
 * Factory class for business-logic implementation. All main sections of the
 * business logic has it's own method for retrieving the concrete implementation
 * dealing with that part of the application.
 * 
 * @author zsjoska
 * 
 */
public class ModelFactory {

	/**
	 * This method returns the {@link SessionManager} implementation selected.
	 * 
	 * @return the configured {@link SessionManager} implementation
	 */
	public static SessionManager getSessionManager() {
		return BasicSessionManager.getInstance();
	}

	/**
	 * This method returns the {@link UserManager} implementation selected.
	 * 
	 * @return the configured {@link UserManager} implementation
	 */
	public static UserManager getUserManager() {
		return DummyUserManager.getInstance();
	}

	/**
	 * This method returns the {@link ConfigurationManager} implementation
	 * selected.
	 * 
	 * @return the configured {@link ConfigurationManager} implementation
	 */
	public static ConfigurationManager getConfigurationManager() {
		return FileConfigurationManager.getInstance();
	}
}
