package ro.gagarin;

import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.session.BasicSessionManager;
import ro.gagarin.user.DummyUserManager;
import ro.gagarin.session.Session;

/**
 * Factory class for business-logic implementation. All main sections of the
 * business logic has it's own method for retrieving the concrete implementation
 * dealing with that part of the application.
 * 
 * @author zsjoska
 * 
 */
public class ModelFactory {

	static {
		ApplicationInitializer.init();
	}

	/**
	 * This method returns the {@link SessionManager} implementation selected.
	 * 
	 * @return the configured {@link SessionManager} implementation
	 */
	public static SessionManager getSessionManager() {
		return BasicSessionManager.getInstance();
	}

	public static RoleManager getRoleManager(Session session) {
		return null;
	}

	/**
	 * This method returns the {@link UserManager} implementation selected.
	 * 
	 * @return the configured {@link UserManager} implementation
	 */
	public static UserManager getUserManager(Session session) {
		return DummyUserManager.getInstance();
	}

	@Deprecated
	public static ConfigurationManager getConfigurationManager(BaseManager userManager) {
		return FileConfigurationManager.getInstance();
	}

	/**
	 * This method returns the {@link ConfigurationManager} implementation
	 * selected.
	 * 
	 * @return the configured {@link ConfigurationManager} implementation
	 */
	public static ConfigurationManager getConfigurationManager(Session session) {
		return FileConfigurationManager.getInstance();
	}
}
