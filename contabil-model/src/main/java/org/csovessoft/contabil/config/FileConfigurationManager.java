package org.csovessoft.contabil.config;

import java.util.ArrayList;

import org.csovessoft.contabil.ConfigurationManager;
import org.csovessoft.contabil.SettingsChangeObserver;

public class FileConfigurationManager implements ConfigurationManager {

	private static final ConfigurationManager INSTANCE = new FileConfigurationManager();

	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	ArrayList<SettingsChangeObserver> changeObservers = new ArrayList<SettingsChangeObserver>();

	ArrayList<String> configuration = new ArrayList<String>();

	private FileConfigurationManager() {
		configuration.add(Config.USER_SESSION_TIMEOUT.ordinal(), "5000");
		configuration.add(Config.SESSION_CHECK_PERIOD.ordinal(), "3000");
	}

	@Override
	public long getLong(Config config) {
		String object = null;
		if (this.configuration.size() > config.ordinal()) {
			object = this.configuration.get(config.ordinal());
		}
		if (object == null) {
			object = DefaultConfiguration.getValue(config);
		}
		long value = Long.valueOf(object);
		return value;
	}

	@Override
	public void registerForChange(SettingsChangeObserver observer) {
		this.changeObservers.add(observer);
	}

	@Override
	public void setConfigValue(Config config, String value) {
		configuration.add(config.ordinal(), value);
		for (SettingsChangeObserver observer : changeObservers) {
			observer.configChanged(config, value);
		}
	}

}
