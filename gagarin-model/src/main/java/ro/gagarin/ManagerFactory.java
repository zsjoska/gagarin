package ro.gagarin;

import ro.gagarin.session.Session;

public interface ManagerFactory {
	ConfigurationManager getConfigurationManager(Session session);

	DAOManager getDAOManager();

	void releaseSession(Session session);

	SessionManager getSessionManager();

	AuthorizationManager getAuthorizationManager(Session session);
}
