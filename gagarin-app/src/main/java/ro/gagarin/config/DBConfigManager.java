package ro.gagarin.config;

import java.io.InputStream;
import java.util.ArrayList;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ConfigDAO;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.ManagerFactory;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
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
		public void execute(Session session, AppLog log) throws Exception {
			ConfigDAO configDAO = FACTORY.getDAOManager().getConfigDAO(session);
			long lastUpdateTime = configDAO.getLastUpdateTime();
			if (lastUpdateTime > INSTANCE.getLastUpdateTime()) {
				long lastQuery = System.currentTimeMillis();
				ArrayList<ConfigEntry> cfgValues = configDAO.listConfigurations();
				INSTANCE.importConfigMap(cfgValues, log);
				INSTANCE.setLastUpdateTime(lastQuery);
			}
		}
	}

	private final ConfigurationManager localConfig;
	private long lastUpdateTime = 0;

	private DBConfigManager(ConfigurationManager localCfg) {
		this.localConfig = localCfg;
	}

	public void importConfigMap(ArrayList<ConfigEntry> cfgValues, AppLog log) {
		ArrayList<String> cfgs = new ArrayList<String>(Config.values().length);
		for (ConfigEntry configEntry : cfgValues) {
			System.err.println(configEntry.getConfigName() + "=" + configEntry.getConfigValue());
			try {
				Config cfg = Config.valueOf(configEntry.getConfigName());
				cfgs.add(cfg.ordinal(), configEntry.getConfigValue());
			} catch (Exception e) {
				log.error("Could not interpret config " + configEntry.getConfigName() + "="
						+ configEntry.getConfigValue());
			}
		}
		importConfig(cfgs);
	}

	public void setLastUpdateTime(long lastQuery) {
		this.lastUpdateTime = lastQuery;

	}

	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	@Override
	public String getString(Config config) {
		// local config has precedence
		if (localConfig.isDefined(config)) {
			return localConfig.getString(config);
		}
		if (super.isDefined(config)) {
			return super.getString(config);
		}

		// this will return the default value
		return localConfig.getString(config);
	}

	@Override
	public InputStream getConfigFileStream(Config file) throws OperationException {
		return localConfig.getConfigFileStream(file);
	}

	public static DBConfigManager getInstance() {
		return INSTANCE;
	}

}
