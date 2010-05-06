package ro.gagarin.testutil;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.ManagerFactory;
import ro.gagarin.RoleDAO;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.exceptions.ExceptionBase;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserRole;

public class TUtil {
    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final ConfigurationManager CFG_MANAGER = FACTORY.getConfigurationManager();

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
	RoleDAO roleManager = FACTORY.getDAOManager().getRoleDAO(session);
	UserRole role = roleManager.getRoleByName(CFG_MANAGER.getString(Config.ADMIN_ROLE_NAME));
	FACTORY.releaseSession(session);
	return role;
    }

}
