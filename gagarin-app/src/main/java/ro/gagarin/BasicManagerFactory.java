package ro.gagarin;

import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.JdbcDAOManager;
import ro.gagarin.log.BasicLogManager;
import ro.gagarin.scheduler.DefaultScheduleManager;
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
public class BasicManagerFactory implements ManagerFactory {

    private static final BasicManagerFactory INSTANCE = new BasicManagerFactory();

    private ApplicationState state = ApplicationState.INIT;

    private ConfigurationManager configurationManager;
    private ScheduleManager scheduleManager;
    private LogManager logManager;
    private DAOManager daoManager;
    private SessionManager sessionManager;

    static {
	try {
	    if (ApplicationInitializer.init(INSTANCE)) {
		INSTANCE.setApplicationState(ApplicationState.READY);
	    }
	} catch (OperationException e) {
	    INSTANCE.setApplicationState(ApplicationState.OFFLINE);
	}
    }

    public static ManagerFactory getInstance() {
	return INSTANCE;
    }

    @Override
    public ApplicationState getApplicationState() {
	return this.state;
    }

    @Override
    public void setApplicationState(ApplicationState state) {
	this.state = state;
    }

    /**
     * This method returns the {@link SessionManager} implementation selected.
     * 
     * @return the configured {@link SessionManager} implementation
     */
    public SessionManager getSessionManager() {
	return this.sessionManager;
    }

    public DAOManager getDAOManager() {
	return this.daoManager;
    }

    /**
     * This method returns the {@link ConfigurationManager} implementation
     * selected.
     * 
     * @return the configured {@link ConfigurationManager} implementation
     */
    public ConfigurationManager getConfigurationManager() {
	return this.configurationManager;
    }

    public AuthorizationManager getAuthorizationManager() {
	return new BasicAuthorizationManager();
    }

    @Override
    public AuthenticationManager getAuthenticationManager(Session session) {
	// TODO: remove session parameter
	return new BasicAuthenticationManager(session);
    }

    @Override
    public LogManager getLogManager() {
	return this.logManager;
    }

    @Override
    public ScheduleManager getScheduleManager() {
	return this.scheduleManager;
    }

    public void initializeManagers() {
	this.configurationManager = FileConfigurationManager.getInstance();
	this.daoManager = JdbcDAOManager.getInstance();
	this.scheduleManager = new DefaultScheduleManager();
	this.logManager = new BasicLogManager();
	this.sessionManager = BasicSessionManager.getInstance();
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
	this.configurationManager = configurationManager;
    }

    public void releaseSession(Session session) {
	getSessionManager().releaseSession(session);
    }
}
