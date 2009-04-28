package ro.gagarin;

import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.hibernate.HibernateRoleManager;
import ro.gagarin.hibernate.HibernateUserManager;
import ro.gagarin.session.BasicSessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.DummyAuthorizationManager;

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
		synchronized (session) {
			BaseManager manager = session.getManager();
			if (manager == null) {
				HibernateRoleManager roleManager = new HibernateRoleManager();
				session.setManager(roleManager);
				return roleManager;
			} else {
				return new HibernateRoleManager(manager);
			}
		}
	}

	/**
	 * This method returns the {@link UserManager} implementation selected.
	 * 
	 * @return the configured {@link UserManager} implementation
	 */
	public static UserManager getUserManager(Session session) {
		synchronized (session) {
			BaseManager manager = session.getManager();
			if (manager == null) {
				HibernateUserManager userManager = new HibernateUserManager();
				session.setManager(userManager);
				return userManager;
			} else {
				return new HibernateUserManager(manager);
			}
		}
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

	public static AuthorizationManager getAuthorizationManager(Session session) {
		return new DummyAuthorizationManager();
	}

	public static void releaseManagers(Session session, BaseManager... baseManagers) {

		if (baseManagers == null)
			return;

		synchronized (session) {
			for (BaseManager baseManager : baseManagers) {
				baseManager.release();
			}
			session.setManager(null);
		}
	}

}
