package ro.gagarin.manager;

import ro.gagarin.ApplicationState;
import ro.gagarin.dao.DAOManager;
import ro.gagarin.session.Session;

// TODO:(2) rename to application
public interface ManagerFactory {
    ConfigurationManager getConfigurationManager();

    DAOManager getDAOManager();

    SessionManager getSessionManager();

    AuthorizationManager getAuthorizationManager();

    AuthenticationManager getAuthenticationManager(Session session);

    LogManager getLogManager();

    ScheduleManager getScheduleManager();

    ApplicationState getApplicationState();

    void setApplicationState(ApplicationState state);

    void releaseSession(Session session);

    void setConfigurationManager(ConfigurationManager configurationManager);

}
