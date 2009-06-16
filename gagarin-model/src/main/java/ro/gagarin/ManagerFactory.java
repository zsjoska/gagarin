package ro.gagarin;

import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

public interface ManagerFactory {
	ConfigurationManager getConfigurationManager(Session session);

	DAOManager getDAOManager();

	void releaseSession(Session session);

	SessionManager getSessionManager();

	AuthorizationManager getAuthorizationManager(Session session);

	ApplicationState getApplicationState();

	void setApplicationState(ApplicationState state);

	AuthenticationManager getAuthenticationManager(Session session);

	AppLog getLogManager(Session session);
}
