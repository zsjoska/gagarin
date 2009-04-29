package ro.gagarin;

import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.dummyimpl.DummyAuthorizationManager;
import ro.gagarin.hibernate.BaseHibernateManager;
import ro.gagarin.hibernate.HibernateRoleManager;
import ro.gagarin.hibernate.HibernateUserManager;
import ro.gagarin.session.BasicSessionManager;
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
		return new HibernateRoleManager(session);
	}

	/**
	 * This method returns the {@link UserManager} implementation selected.
	 * 
	 * @return the configured {@link UserManager} implementation
	 */
	public static UserManager getUserManager(Session session) {
		return new HibernateUserManager(session);
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

	public static AuthorizationManager getAuthorizationManager(Session session) {
		return new DummyAuthorizationManager();
	}

	public static void releaseSession(Session session) {

		// TODO: find a better logic for release the manager in order that this
		// code to not depend on BaseHibernateManager.class

		// TODO: find a way to move this code to the sessionManager; see
		// BasicSessionManager.destroySession(Session session)

		synchronized (session) {
			Class<?> key = BaseHibernateManager.class;
			Object property = session.getProperty(key);
			session.setProperty(key, null);
			if (property instanceof BaseManager) {
				((BaseManager) property).release();
			}
		}
	}

}
