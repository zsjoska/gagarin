package ro.gagarin.ws.objects;

import ro.gagarin.config.ConfigEntry;
import ro.gagarin.config.ConfigScope;
import ro.gagarin.user.BaseEntity;

public class WSConfig extends BaseEntity implements ConfigEntry {

	private String configName;
	private String configValue;
	private ConfigScope configScope;

	public WSConfig() {
	}

	public WSConfig(ConfigEntry cfg) {
		this.setId(cfg.getId());
		this.configName = cfg.getConfigName();
		this.configValue = cfg.getConfigValue();
		this.configScope = cfg.getConfigScope();
	}

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

	@Override
	public ConfigScope getConfigScope() {
		return this.configScope;
	}

	public void setConfigScope(ConfigScope configScope) {
		this.configScope = configScope;
	}
}
