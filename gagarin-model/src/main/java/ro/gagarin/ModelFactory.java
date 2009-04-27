package ro.gagarin;

import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.hibernate.HibernateRoleManager;
import ro.gagarin.hibernate.HibernateUserManager;
import ro.gagarin.session.BasicSessionManager;


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
		// try {
		// ApplicationInitializer.init();
		// } catch (RuntimeException e) {
		// e.printStackTrace();
		// throw e;
		// }
	}

	/**
	 * This method returns the {@link SessionManager} implementation selected.
	 * 
	 * @return the configured {@link SessionManager} implementation
	 */
	public static SessionManager getSessionManager() {
		return BasicSessionManager.getInstance();
	}

	public static RoleManager getRoleManager() {
		return new HibernateRoleManager();
	}

	/**
	 * This method returns the {@link UserManager} implementation selected.
	 * 
	 * @return the configured {@link UserManager} implementation
	 */
	public static UserManager getUserManager() {
		return HibernateUserManager.getInstance();
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
