package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.config.ConfigEntry;
import ro.gagarin.config.Configuration;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.config.FileConfigurationManager;
import ro.gagarin.config.SettingsChangeObserver;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;

public class ConfigTest {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private static final transient Logger LOG = Logger.getLogger(ConfigTest.class);
    private static long oldDBImportRate;

    @BeforeClass
    public static void startup() {
	oldDBImportRate = TUtil.setDBImportRate(100);
    }

    @AfterClass
    public static void shutdown() {
	TUtil.setDBImportRate(oldDBImportRate);
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
	localCfgMgr.setConfigValue(session, "_TEST_LOCAL_ONLY_", "A_LOCAL_VALUE");

	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	try {
	    dbCfgMgr.setConfigValue(session, "_TEST_LOCAL_ONLY_", "1000");
	} finally {
	    FACTORY.releaseSession(session);
	}
	assertEquals("The config should not change", "A_LOCAL_VALUE", Configuration._TEST_LOCAL_ONLY_);
    }

    @Test
    public void testDBConfigChange() throws Exception {

	Session session = TUtil.createTestSession();
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();

	String aNewValue = String.valueOf(System.nanoTime());
	try {

	    dbCfgMgr.setConfigValue(session, "_TEST_DB_ONLY_", aNewValue);
	} finally {
	    FACTORY.releaseSession(session);
	}

	dbCfgMgr.waitForDBImport();
	assertEquals("The config should change", aNewValue, Configuration._TEST_DB_ONLY_);
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
	String configValue1 = String.valueOf(System.nanoTime());
	String configValue2 = String.valueOf(System.nanoTime());
	assertNotSame(configValue1, configValue2);
	final ArrayList<String> values = new ArrayList<String>();
	Session session = TUtil.createTestSession();
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	dbCfgMgr.registerForChange(new SettingsChangeObserver() {

	    @Override
	    public boolean configChanged(String config, String value) {
		if ("_TEST_DB_AND_FILE_".equalsIgnoreCase(config)) {
		    values.add(value);
		    return true;
		}
		return false;
	    }
	});

	try {
	    dbCfgMgr.setConfigValue(session, "_TEST_DB_AND_FILE_", configValue1);

	} finally {
	    FACTORY.releaseSession(session);
	}
	dbCfgMgr.waitForDBImport();
	assertEquals("We had to be notified", 1, values.size());
	assertEquals("Wrong value in config", configValue1, values.get(0));
	assertEquals("The config should change", configValue1, Configuration._TEST_DB_AND_FILE_);

	// now we change the file config and we expect that DB config listeners
	// will also be notified
	session = TUtil.createTestSession();
	ConfigurationManager cfgManager = FileConfigurationManager.getInstance();
	try {
	    cfgManager.setConfigValue(session, "_TEST_DB_AND_FILE_", configValue2);
	} finally {
	    FACTORY.releaseSession(session);
	}

	assertEquals("The config should change", configValue2, Configuration._TEST_DB_AND_FILE_);
	assertEquals("The config should change also for DB", configValue2, Configuration._TEST_DB_AND_FILE_);
	assertEquals("We had to be notified", 2, values.size());
	assertEquals("Wrong value notified", configValue2, values.get(1));

    }

    @Test
    public void testConfigurationScope() throws Exception {
	Session session = TUtil.createTestSession();
	ConfigurationManager localCfgMgr = FileConfigurationManager.getInstance();
	DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
	try {
	    localCfgMgr.setConfigValue(session, "REGISTRATION_VALIDITY", "123");
	    localCfgMgr.setConfigValue(session, "LOCAL_CUSTOM", "123");

	    dbCfgMgr.setConfigValue(session, "ALLOW_USER_REGISTRATION", "true");
	    dbCfgMgr.setConfigValue(session, "DB_CUSTOM", "true");
	} finally {
	    FACTORY.releaseSession(session);
	}
	dbCfgMgr.waitForDBImport();

	List<ConfigEntry> configValues = dbCfgMgr.getConfigValues();
	for (ConfigEntry entry : configValues) {
	    if (entry.getConfigName().equals("REGISTRATION_VALIDITY")) {
		assertEquals("LOCAL", entry.getConfigScope().name());
	    } else if (entry.getConfigName().equals("LOCAL_CUSTOM")) {
		assertEquals("LOCAL", entry.getConfigScope().name());
	    } else if (entry.getConfigName().equals("ALLOW_USER_REGISTRATION")) {
		assertEquals("DB", entry.getConfigScope().name());
	    } else if (entry.getConfigName().equals("DB_CUSTOM")) {
		assertEquals("DB", entry.getConfigScope().name());
	    } else if (entry.getConfigName().equals("ADMIN_PASSWORD")) {
		assertEquals("DEFAULT", entry.getConfigScope().name());
	    }
	}
    }
}
