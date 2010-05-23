package ro.gagarin.manager;

import ro.gagarin.ApplicationState;
import ro.gagarin.dao.DAOManager;
import ro.gagarin.session.Session;

public interface ManagerFactory {
    ConfigurationManager getConfigurationManager();

    DAOManager getDAOManager();

    void releaseSession(Session session);

    SessionManager getSessionManager();

    AuthorizationManager getAuthorizationManager();

    ApplicationState getApplicationState();

    void setApplicationState(ApplicationState state);

    AuthenticationManager getAuthenticationManager(Session session);

    LogManager getLogManager();

    ScheduleManager getScheduleManager();

    void setConfigurationManager(ConfigurationManager configurationManager);

}