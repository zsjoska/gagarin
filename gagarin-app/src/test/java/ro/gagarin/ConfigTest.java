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

	private static final ManagerFactory FACTORY = BasicManagerFactory
			.getInstance();
	private static final transient Logger LOG = Logger
			.getLogger(ConfigTest.class);

	@BeforeClass
	public static void startup() {
		DBConfigManager dbCfgMgr = DBConfigManager.getInstance();

		// trigger a config change to increase the DB check rate
		dbCfgMgr.configChanged(Config.DB_CONFIG_CHECK_PERIOD, "100");
	}

	@AfterClass
	public static void shutdown() {
		DBConfigManager dbCfgMgr = DBConfigManager.getInstance();
		long chk = dbCfgMgr.getLong(Config.DB_CONFIG_CHECK_PERIOD);
		dbCfgMgr.configChanged(Config.DB_CONFIG_CHECK_PERIOD, "" + chk);
	}

	@Test
	public void testLocalConfigPriority() throws Exception {

		Session session = TUtil.createTestSession();
		DBConfigManager dbCfgMgr = DBConfigManager.getInstance();

		try {
			dbCfgMgr.setConfigValue(session, Config.SESSION_CHECK_PERIOD,
					"1000");
		} finally {
			FACTORY.releaseSession(session);
		}
		assertEquals("The config should not change", FileConfigurationManager
				.getInstance().getLong(Config.SESSION_CHECK_PERIOD), dbCfgMgr
				.getLong(Config.SESSION_CHECK_PERIOD));
		long sleeptime = 150; // dbCfgMgr.getLong(Config.DB_CONFIG_CHECK_PERIOD);
		LOG.info("Waiting for DB Import " + sleeptime);
		Thread.sleep(sleeptime);
		assertEquals(
				"The config should not change because this config is defined locally",
				FileConfigurationManager.getInstance().getLong(
						Config.SESSION_CHECK_PERIOD), dbCfgMgr
						.getLong(Config.SESSION_CHECK_PERIOD));
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

		assertEquals("The config should not change", oldValue, dbCfgMgr
				.getString(Config._TEST_DB_ONLY_));
		long sleeptime = 150; //dbCfgMgr.getLong(Config.DB_CONFIG_CHECK_PERIOD);
		LOG.info("Waiting for DB Import " + sleeptime);
		Thread.sleep(sleeptime);
		assertEquals("The config should change", aNewValue, dbCfgMgr
				.getString(Config._TEST_DB_ONLY_));
	}
}
