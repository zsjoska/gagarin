package ro.gagarin.testutil;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.SessionManager;
import ro.gagarin.session.Session;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class TUtil {
    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final ConfigurationManager CFG_MANAGER = FACTORY.getConfigurationManager();

    private static volatile long sequencence = System.currentTimeMillis();

    public static Session createTestSession() {
	Session session = FACTORY.getSessionManager().createSession(null, "TEST", FACTORY);
	try {
	    FACTORY.getSessionManager().acquireSession(session.getSessionString());
	} catch (SessionNotFoundException e) {
	    // surprise
	    throw new RuntimeException(e);
	}
	AppUser user = new AppUser();
	user.setUsername("tester");
	session.setUser(user);
	return session;
    }

    public static void waitDBImportToHappen() throws InterruptedException {
	ConfigurationManager cfgMgr = FACTORY.getConfigurationManager();
	if (cfgMgr instanceof DBConfigManager) {
	    DBConfigManager dbCfgMgr = (DBConfigManager) cfgMgr;
	    dbCfgMgr.waitForDBImport();
	}
    }

    public static void setDBImportRate(int i) {
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	// trigger a config change to increase the DB check rate
	dbCfgMgr.configChanged(Config.DB_CONFIG_CHECK_PERIOD, String.valueOf(i));
    }

    public static void resetDBImportRate() {
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	long chk = dbCfgMgr.getLong(Config.DB_CONFIG_CHECK_PERIOD);
	dbCfgMgr.configChanged(Config.DB_CONFIG_CHECK_PERIOD, "" + chk);
    }

    public static UserRole getAdminRole() throws ExceptionBase {
	Session session = createTestSession();
	RoleDAO roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	UserRole role = roleDAO.getRoleByName(CFG_MANAGER.getString(Config.ADMIN_ROLE_NAME));
	FACTORY.releaseSession(session);
	return role;
    }

    public static String generateID(String prefix) {
	return prefix + "_" + String.valueOf(++sequencence);
    }

    public static User getAdminUser() throws OperationException {
	Session session = createTestSession();
	UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(session);
	User user = userDAO.getUserByUsername(CFG_MANAGER.getString(Config.ADMIN_USER_NAME));
	FACTORY.releaseSession(session);
	return user;
    }

    public static Session createAdminSession() throws OperationException, ItemNotFoundException,
	    SessionNotFoundException {
	Session session = FACTORY.getSessionManager().createSession(null, "TEST", FACTORY);
	FACTORY.getSessionManager().acquireSession(session.getSessionString());
	UserDAO userDAO = FACTORY.getDAOManager().getUserDAO(session);
	User adminUser = userDAO.getUserByUsername(CFG_MANAGER.getString(Config.ADMIN_USER_NAME));
	SessionManager sessionManager = FACTORY.getSessionManager();
	sessionManager.assignUserToSession(adminUser, session);
	sessionManager.releaseSession(session);
	return session;
    }

    public static Group getAdminGroup(Session session) throws ExceptionBase {
	String adminGroupName = FACTORY.getConfigurationManager().getString(Config.ADMIN_GROUP_NAME);
	return FACTORY.getDAOManager().getUserDAO(session).getGroupByName(adminGroupName);

    }
}
