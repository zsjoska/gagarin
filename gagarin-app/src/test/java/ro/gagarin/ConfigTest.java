package ro.gagarin;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;
import ro.gagarin.config.FileConfigurationManager;
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

    @Test
    public void testLocalConfigPriority() throws Exception {

	Session session = TUtil.createTestSession();
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
}
