package ro.gagarin.jdbc.objects;

import ro.gagarin.config.ConfigEntry;
import ro.gagarin.user.BaseEntity;

public class DBConfig extends BaseEntity implements ConfigEntry {

	private String configName;
	private String configValue;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
