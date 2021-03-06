package ro.gagarin.config;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public class ConfigHolder {

    private static final transient Logger LOG = Logger.getLogger(ConfigHolder.class);

    /**
     * The change observers, a single static instance
     */
    private static ArrayList<SettingsChangeObserver> changeObservers = new ArrayList<SettingsChangeObserver>();

    private ArrayList<String> configuration = new ArrayList<String>(Config.values().length);

    public ConfigHolder() {
	for (int i = 0; i < Config.values().length; i++) {
	    configuration.add(null);
	}
    }

    public void registerForChange(SettingsChangeObserver observer) {
	getChangeObservers().add(observer);
    }

    public void setConfigValue(Session session, Config config, String value) throws OperationException {
	configuration.add(config.ordinal(), value);
	notifyConfigChange(config, value);
    }

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

    public long getLong(Config config) {
	String strValue = getString(config);
	try {
	    long value = Long.valueOf(strValue);
	    return value;
	} catch (NumberFormatException e) {
	    LOG.error(config.name() + "=" + strValue + " is invalid", e);
	    throw e;
	}
    }

    public boolean isDefined(Config config) {
	return configuration.get(config.ordinal()) != null;
    }

    public synchronized void importConfig(String[] newCfg) {
	ArrayList<Integer> changed = new ArrayList<Integer>();
	for (int i = 0; i < newCfg.length; i++) {

	    String newValue = newCfg[i];
	    if (newValue == null)
		continue;

	    String oldValue = null;
	    oldValue = configuration.get(i);
	    if (oldValue != null) {
		if (!oldValue.equals(newValue)) {
		    changed.add(i);
		    configuration.set(i, newValue);
		}
	    } else {
		if (newValue != null) {
		    changed.add(i);
		    configuration.set(i, newValue);
		}
	    }
	}

	for (Integer i : changed) {
	    notifyConfigChange(Config.values()[i], configuration.get(i));
	}
    }

    private void notifyConfigChange(Config config, String value) {
	LOG.info("Config Change:" + config.name() + "=" + value + "; propagating...");
	for (SettingsChangeObserver observer : getChangeObservers()) {
	    try {
		observer.configChanged(config, value);
	    } catch (Exception e) {
		LOG.error("Config " + config.name() + "=" + value + " could not be applied by "
			+ observer.getClass().getName(), e);
	    }
	}
    }

    public String[] exportConfig() {
	String[] configs = new String[Config.values().length];
	int i = 0;
	for (String string : this.configuration) {
	    configs[i++] = string;
	}
	return configs;
    }

    protected static ArrayList<SettingsChangeObserver> getChangeObservers() {
	return changeObservers;
    }

    public void setConfigValue(Session session, ConfigEntry config) throws OperationException {
	Config cfg = Config.valueOf(config.getConfigName());
	setConfigValue(session, cfg, config.getConfigValue());
    }

    public void importConfig(Properties prop) {
	Set<Entry<Object, Object>> entrySet = prop.entrySet();
	String[] newCfg = new String[Config.values().length];
	for (Entry<Object, Object> entry : entrySet) {
	    try {
		newCfg[Config.valueOf(entry.getKey().toString()).ordinal()] = entry.getValue().toString();
	    } catch (IllegalArgumentException e) {
		LOG.error("Could not load config " + entry.getKey().toString() + "=" + entry.getValue().toString());
	    }
	}
	importConfig(newCfg);
    }
}
