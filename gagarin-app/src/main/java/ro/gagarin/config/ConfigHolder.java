package ro.gagarin.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public class ConfigHolder {

    private static final transient Logger LOG = Logger.getLogger(ConfigHolder.class);
    private final HashSet<String> definedConfig = new HashSet<String>();

    /**
     * The change observers, a single static instance
     */
    private static ArrayList<SettingsChangeObserver> changeObservers = new ArrayList<SettingsChangeObserver>();

    public void registerForChange(SettingsChangeObserver observer) {
	getChangeObservers().add(observer);
    }

    public void setConfigValue(Session session, String key, String value) throws OperationException {
	Configuration.setConfig(key, value);
	definedConfig.add(key);
	notifyConfigChange(key, value);
    }

    public boolean isDefined(String config) {
	return definedConfig.contains(config);
    }

    private void notifyConfigChange(String config, String value) {
	LOG.info("Config Change:" + config + "=" + value + "; propagating...");
	for (SettingsChangeObserver observer : getChangeObservers()) {
	    try {
		observer.configChanged(config, value);
	    } catch (Exception e) {
		LOG.error("Config " + config + "=" + value + " could not be applied by "
			+ observer.getClass().getName(), e);
	    }
	}
    }

    protected static ArrayList<SettingsChangeObserver> getChangeObservers() {
	return changeObservers;
    }

    public void setConfigValue(Session session, ConfigEntry config) throws OperationException {
	setConfigValue(session, config.getConfigName(), config.getConfigValue());
    }

    public void importConfig(Properties prop) {
	definedConfig.clear();
	Set<Entry<Object, Object>> entrySet = prop.entrySet();
	for (Entry<Object, Object> entry : entrySet) {
	    String key = entry.getKey().toString();
	    String value = entry.getValue().toString();
	    try {
		definedConfig.add(key);
		boolean changed = Configuration.setConfig(key, value);
		if (changed) {
		    notifyConfigChange(key, value);
		}
	    } catch (IllegalArgumentException e) {
		LOG.error("Could not load config " + key + "=" + value);
	    } catch (OperationException e) {
		LOG.error("Could not load config " + key + "=" + value);
	    }
	}
    }
}
