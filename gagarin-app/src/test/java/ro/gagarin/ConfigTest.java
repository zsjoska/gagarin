package ro.gagarin;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.config.SettingsChangeObserver;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;

public class ConfigTest {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final transient Logger LOG = Logger.getLogger(ConfigTest.class);

    @BeforeClass
    public static void startup() {
	TUtil.setDBImportRate(100);
    }

    @AfterClass
    public static void shutdown() {
	TUtil.resetDBImportRate();
    }

    /**
     * 1. Ensures that we set the value in local config.<br>
     * 2. Changes the config value in the DB<br>
     * 3. Verifies that the local config was changed, thus the two values are in
     * sync.
     * 
     * @throws Exception
     */
    @Test
    public void testLocalConfigPriority() throws Exception {

	Session session = TUtil.createTestSession();

	ConfigurationManager localCfgMgr = FileConfigurationManager.getInstance();
	localCfgMgr.setConfigValue(session, Config._TEST_LOCAL_ONLY_, "A_LOCAL_VALUE");

	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	try {
	    dbCfgMgr.setConfigValue(session, Config._TEST_LOCAL_ONLY_, "1000");
	} finally {
	    FACTORY.releaseSession(session);
	}
	assertEquals("The config should not change", FileConfigurationManager.getInstance().getString(
		Config._TEST_LOCAL_ONLY_), dbCfgMgr.getString(Config._TEST_LOCAL_ONLY_));
	long sleeptime = 150; // dbCfgMgr.getLong(Config.DB_CONFIG_CHECK_PERIOD);
	LOG.info("Waiting for DB Import " + sleeptime);
	Thread.sleep(sleeptime);
	assertEquals("The config should not change because this config is defined locally", FileConfigurationManager
		.getInstance().getString(Config._TEST_LOCAL_ONLY_), dbCfgMgr.getString(Config._TEST_LOCAL_ONLY_));
    }

    @Test
    public void testDBConfigChange() throws Exception {

	Session session = TUtil.createTestSession();
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();

	String aNewValue = String.valueOf(System.nanoTime());
	String oldValue = dbCfgMgr.getString(Config._TEST_DB_ONLY_);
	try {

	    dbCfgMgr.setConfigValue(session, Config._TEST_DB_ONLY_, aNewValue);
	} finally {
	    FACTORY.releaseSession(session);
	}

	dbCfgMgr.waitForDBImport();
	assertEquals("The config should change", aNewValue, dbCfgMgr.getString(Config._TEST_DB_ONLY_));
    }

    /**
     * A combined scenario where we add a configuration to the DB and later we
     * add the same configuration to the file.<br>
     * Since file configuration has priority, the DBCOnfig manager also should
     * notice the change.<br>
     * Getting the value through DBConfigManager should be changed<br>
     * DBConfigManager notifiers should be called.
     * 
     * 
     * @throws Exception
     */
    @Test
    public void configNotificationCombined() throws Exception {
	TUtil.setDBImportRate(2000);
	final ArrayList<String> values = new ArrayList<String>();
	Session session = TUtil.createTestSession();
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	dbCfgMgr.registerForChange(new SettingsChangeObserver() {

	    @Override
	    public boolean configChanged(Config config, String value) {
		if (config == Config._TEST_DB_AND_FILE_) {
		    values.add(value);
		    return true;
		}
		return false;
	    }
	});

	try {
	    dbCfgMgr.setConfigValue(session, Config._TEST_DB_AND_FILE_, "DB");

	} finally {
	    FACTORY.releaseSession(session);
	}
	dbCfgMgr.waitForDBImport();
	assertEquals("We had to be notified", 1, values.size());
	assertEquals("Wrong value in config", "DB", values.get(0));
	assertEquals("The config should change", "DB", dbCfgMgr.getString(Config._TEST_DB_AND_FILE_));

	// now we change the file config and we expect that DB config listeners
	// will also be notified
	session = TUtil.createTestSession();
	ConfigurationManager cfgManager = FileConfigurationManager.getInstance();
	try {
	    cfgManager.setConfigValue(session, Config._TEST_DB_AND_FILE_, "FILE");
	} finally {
	    FACTORY.releaseSession(session);
	}

	assertEquals("The config should change", "FILE", cfgManager.getString(Config._TEST_DB_AND_FILE_));
	assertEquals("The config should change also for DB", "FILE", dbCfgMgr.getString(Config._TEST_DB_AND_FILE_));
	assertEquals("We had to be notified", 2, values.size());
	assertEquals("Wrong value notified", "FILE", values.get(1));

    }
}
