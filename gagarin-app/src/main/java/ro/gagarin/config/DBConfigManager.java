package ro.gagarin.config;

import java.io.InputStream;
import java.util.ArrayList;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ConfigDAO;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.ManagerFactory;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBConfig;
import ro.gagarin.log.AppLog;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.session.Session;

public class DBConfigManager extends ConfigHolder implements ConfigurationManager,
		SettingsChangeObserver {

	// TODO: there is a problem with the configuration implementation in the
	// following scenario:
	// the config is only in the DB
	// runtime, the config is added to the file
	// Problem: DBConfig observers are not notified

	private static ManagerFactory FACTORY = BasicManagerFactory.getInstance();
	private static final DBConfigManager INSTANCE = new DBConfigManager(FileConfigurationManager
			.getInstance());

	static {
		ConfigurationManager cfgManager = FACTORY.getConfigurationManager();
		long period = cfgManager.getLong(Config.DB_CONFIG_CHECK_PERIOD);

		INSTANCE.registerForChange(INSTANCE);
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
			log.debug("DBLUT = " + lastUpdateTime + " CacheLUT=" + INSTANCE.getLastUpdateTime());
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

	private void importConfigMap(ArrayList<ConfigEntry> cfgValues, AppLog log) {
		String cfgs[] = new String[Config.values().length];

		for (ConfigEntry configEntry : cfgValues) {

			try {
				Config cfg = Config.valueOf(configEntry.getConfigName());

				// don't import entries defined locally
				if (!localConfig.isDefined(cfg)) {
					cfgs[cfg.ordinal()] = configEntry.getConfigValue();
				}
			} catch (Exception e) {
				log.error("Could not interpret config " + configEntry.getConfigName() + "="
						+ configEntry.getConfigValue(), e);
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

	@Override
	public void setConfigValue(Session session, Config config, String value)
			throws OperationException {

		// local config has precedence...
		if (localConfig.isDefined(config)) {
			AppLog log = session.getManagerFactory().getLogManager(session, DBConfigManager.class);
			log.error("Changing the local config will not be persisted! " + config.name() + "="
					+ value);
			localConfig.setConfigValue(session, config, value);
			return;
		}

		ConfigDAO configDAO = FACTORY.getDAOManager().getConfigDAO(session);
		DBConfig cfg = new DBConfig();
		cfg.setConfigName(config.name());
		cfg.setConfigValue(value);
		try {
			configDAO.setConfigValue(cfg);
		} catch (DataConstraintException e) {
			LOG.error("Error setting config " + config + "=" + value, e);
			throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
		}
	}

	@Override
	public boolean configChanged(Config config, String value) {
		// TODO handle changes of Config.DB_CONFIG_CHECK_PERIOD and update the
		// timer execution rate
		return false;
	}

}
