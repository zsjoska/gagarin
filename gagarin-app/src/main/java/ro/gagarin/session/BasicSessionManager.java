package ro.gagarin.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ControlEntity;
import ro.gagarin.Person;
import ro.gagarin.config.Config;
import ro.gagarin.config.SettingsChangeObserver;
import ro.gagarin.dao.BaseDAO;
import ro.gagarin.dao.DAOManager;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.log.AppLog;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.LogManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.user.User;
import ro.gagarin.user.UserPermission;
import ro.gagarin.util.Utils;

public class BasicSessionManager implements SessionManager, SettingsChangeObserver {

    private static final transient Logger LOG = Logger.getLogger(BasicSessionManager.class);

    private long USER_SESSION_TIMEOUT;
    private long SESSION_CHECK_PERIOD;

    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>(
	    new HashMap<String, Session>());

    private SessionCheckerThread chkSession = null;

    public BasicSessionManager() {
	LOG.debug("Creating BasicSessionManager");

	ConfigurationManager cfgManager;
	cfgManager = BasicManagerFactory.getInstance().getConfigurationManager();
	cfgManager.registerForChange(this);
	USER_SESSION_TIMEOUT = cfgManager.getLong(Config.USER_SESSION_TIMEOUT);
	SESSION_CHECK_PERIOD = cfgManager.getLong(Config.SESSION_CHECK_PERIOD);
	chkSession = new SessionCheckerThread(this);
	chkSession.start();

    }

    @Override
    public Session createSession(String language, String reason, ManagerFactory factory) {
	Session session = new Session();
	session.setSessiontimeout(USER_SESSION_TIMEOUT);
	session.setLanguage(language);
	session.setReason(reason);
	session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
	session.setSessionString(System.currentTimeMillis() + "-" + System.nanoTime());
	session.setManagerFactory(factory);
	this.sessions.put(session.getSessionString(), session);
	LOG.info("Created " + reason + " session " + session.getSessionString());
	return session;
    }

    @Override
    public Session getSessionById(String sessionId) {
	Session session = this.sessions.get(sessionId);
	return session;
    }

    @Override
    public void logout(String id) {
	Session session = this.sessions.get(id);
	if (session != null) {
	    // TODO: do something useful here
	}
	this.sessions.remove(id);
    }

    @Override
    public ArrayList<Session> getExpiredSessions() {
	ArrayList<Session> expiredSessions = new ArrayList<Session>();
	for (Session session : this.sessions.values()) {
	    if (session.isExpired()) {
		expiredSessions.add(session);
	    }
	}
	return expiredSessions;
    }

    @Override
    public void destroySession(Session session) {
	LOG.info("Destroy session " + session.getId());
	synchronized (session) {
	    if (session.isBusy()) {
		LOG.error("Unreleased session being destroyed:", session.getCreationStacktrace());
	    }
	    session.getManagerFactory().releaseSession(session);
	    this.sessions.remove(session.getSessionString());
	}
    }

    @Override
    public boolean configChanged(Config config, String value) {
	switch (config) {
	case USER_SESSION_TIMEOUT:
	    USER_SESSION_TIMEOUT = Long.valueOf(value);
	    return true;
	case SESSION_CHECK_PERIOD:
	    SESSION_CHECK_PERIOD = Long.valueOf(value);
	    return true;
	}
	return false;
    }

    public long getSessionCheckPeriod() {
	return SESSION_CHECK_PERIOD;
    }

    @Override
    public Session acquireSession(String sessionId) throws SessionNotFoundException {
	Session session = getSessionById(sessionId);
	if (session == null) {
	    LOG.error("The requested session was not found:" + sessionId);
	    throw new SessionNotFoundException(sessionId);
	}

	if (session.isExpired()) {
	    LOG.error("The requested session expired:" + sessionId);
	    throw new SessionNotFoundException(session);
	}

	synchronized (session) {
	    if (session.isBusy()) {
		LOG.error("The requested session is busy:" + sessionId);
		throw new SessionNotFoundException(session);
	    }
	    session.setBusy(true, new Exception("Session Creation"));
	    session.setExpires(System.currentTimeMillis() + session.getSessionTimeout());
	}

	return session;
    }

    @Override
    public void releaseSession(Session session) {
	synchronized (session) {
	    Class<?> key = BaseDAO.class;
	    Object property = session.getProperty(key);
	    session.setProperty(key, null);
	    if (property instanceof BaseDAO) {
		try {
		    ((BaseDAO) property).release();
		} catch (OperationException e) {
		    LOG.error("Exception releasing the session", e);
		}
	    }
	    session.setBusy(false, null);
	}
    }

    @Override
    public List<Session> getSessionList() {
	ArrayList<Session> sessions = new ArrayList<Session>();
	for (Session session : this.sessions.values()) {
	    sessions.add(session);
	}
	return sessions;
    }

    @Override
    public void initializeManager() {
	// nothing to initialize
    }

    @Override
    public void assignUserToSession(User user, Session session) throws OperationException, ItemNotFoundException {

	// TODO: check that the session has no user assignment
	// may interfere with the register user implementation

	// get the required managers
	ManagerFactory managerFactory = session.getManagerFactory();
	DAOManager daoManager = managerFactory.getDAOManager();
	RoleDAO roleDAO = daoManager.getRoleDAO(session);
	UserDAO userDAO = daoManager.getUserDAO(session);
	LogManager logManager = managerFactory.getLogManager();
	AppLog appLog = logManager.getLoggingSession(session, getClass());

	// get the user groups
	List<Group> userGroups = userDAO.getUserGroups(user);
	Person[] persons = new Person[userGroups.size() + 1];
	int index = 0;
	for (Group group : userGroups) {
	    persons[index++] = group;
	}
	persons[index] = user;

	// get the user effective permissions
	Map<ControlEntity, Set<UserPermission>> effectivePermissions = roleDAO.getEffectivePermissions(persons);

	// TODO: move this to an utility
	// transform the effective permissions to a more friendly format
	Map<ControlEntity, Set<PermissionEnum>> permMap = Utils
		.convertUserPermissionSetToPermissionEnumSet(effectivePermissions);

	session.assignUser(user, permMap);
	appLog.info("User " + user.getId() + ":" + user.getUsername() + " was bound to session " + session.getId());
    }
}
