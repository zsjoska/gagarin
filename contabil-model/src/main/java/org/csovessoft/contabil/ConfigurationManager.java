package org.csovessoft.contabil;

import org.csovessoft.contabil.config.Config;

public interface ConfigurationManager {

	long getLong(Config config);

	void registerForChange(SettingsChangeObserver observer);

	void setConfigValue(Config config, String value);

}
