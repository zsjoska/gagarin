package ro.gagarin.config;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ro.gagarin.ConfigurationManager;

public class FileConfigurationManager implements ConfigurationManager {

	private static final transient Logger LOG = Logger.getLogger(FileConfigurationManager.class);

	private static final ConfigurationManager INSTANCE = new FileConfigurationManager();

	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	ArrayList<SettingsChangeObserver> changeObservers = new ArrayList<SettingsChangeObserver>();

	ArrayList<String> configuration = new ArrayList<String>();

	private FileConfigurationManager() {
		// TODO: load from file
		configuration.add(Config.USER_SESSION_TIMEOUT.ordinal(), "100000");
		configuration.add(Config.SESSION_CHECK_PERIOD.ordinal(), "10000");
		configuration.add(Config.ADMIN_ROLE_NAME.ordinal(), "ADMIN_ROLE");
		configuration.add(Config.ADMIN_USER_NAME.ordinal(), "admin");
		configuration.add(Config.ADMIN_PASSWORD.ordinal(), "password");
	}

	@Override
	public long getLong(Config config) {
		String strValue = null;
		if (this.configuration.size() > config.ordinal()) {
			strValue = this.configuration.get(config.ordinal());
		}
		if (strValue == null) {
			LOG.warn(config.name() + " config value was not found, getting the default value");
			strValue = config.getDefaultValue();
		}
		try {
			long value = Long.valueOf(strValue);
			return value;
		} catch (NumberFormatException e) {
			LOG.error(config.name() + "=" + strValue + " is invalid", e);
			throw e;
		}
	}

	@Override
	public void registerForChange(SettingsChangeObserver observer) {
		this.changeObservers.add(observer);
	}

	@Override
	public void setConfigValue(Config config, String value) {
		configuration.add(config.ordinal(), value);
		LOG.info("Config Change:" + config.name() + "=" + value + "; propagating...");
		for (SettingsChangeObserver observer : changeObservers) {
			try {
				observer.configChanged(config, value);
			} catch (Exception e) {
				LOG.error("Config " + config.name() + "=" + value + " could not be applied by "
						+ observer.getClass().getName(), e);
			}
		}
	}

	@Override
	public String getString(Config config) {
		String strValue = null;
		if (this.configuration.size() > config.ordinal()) {
			strValue = this.configuration.get(config.ordinal());
		}
		if (strValue == null) {
			LOG.warn(config.name() + " config value was not found, getting the default value");
			strValue = config.getDefaultValue();
		}
		return strValue;
	}

}
