package ro.gagarin.config;

import java.io.InputStream;
import java.util.HashMap;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ConfigDAO;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.ManagerFactory;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.session.Session;

public class DBConfigManager extends ConfigHolder implements ConfigurationManager {
	private static ManagerFactory FACTORY = BasicManagerFactory.getInstance();
	private static final DBConfigManager INSTANCE = new DBConfigManager(FileConfigurationManager
			.getInstance());

	static {
		ConfigurationManager cfgManager = FACTORY.getConfigurationManager(null);
		long period = cfgManager.getLong(Config.DB_CONFIG_CHECK_PERIOD);
		FACTORY.getScheduleManager().scheduleJob(
				new DBConfigManager.ConfigImportJob("DB_CONFIG_IMPORT", period, period));
	}

	public static class ConfigImportJob extends ScheduledJob {
		public ConfigImportJob(String name, long initialWait, long period) {
			super(name, initialWait, period);
		}

		@Override
		public void execute(Session session) throws Exception {
			ConfigDAO configDAO = FACTORY.getDAOManager().getConfigDAO(session);
			long lastUpdateTime = configDAO.getLastUpdateTime();
			if (lastUpdateTime > INSTANCE.getLastUpdateTime()) {
				long lastQuery = System.currentTimeMillis();
				HashMap<String, String> cfgValues = configDAO.listConfigurations();
				INSTANCE.importConfigMap(cfgValues);
				INSTANCE.setLastUpdateTime(lastQuery);
			}
		}
	}

	private final ConfigurationManager localConfig;
	private long lastUpdateTime = 0;

	private DBConfigManager(ConfigurationManager localCfg) {
		this.localConfig = localCfg;
	}

	public void importConfigMap(HashMap<String, String> cfgValues) {
		// TODO Auto-generated method stub
	}

	public void setLastUpdateTime(long lastQuery) {
		this.lastUpdateTime = lastQuery;

	}

	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	@Override
	public InputStream getConfigFileStream(Config file) throws OperationException {
		return localConfig.getConfigFileStream(file);
	}

}
