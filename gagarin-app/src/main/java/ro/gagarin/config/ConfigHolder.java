package ro.gagarin.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public class ConfigHolder {

    private static final transient Logger LOG = Logger.getLogger(ConfigHolder.class);
    private final HashSet<String> definedConfig = new HashSet<String>();

    /**
     * The change observers, a single static instance
     */
    private static ConcurrentHashMap<String, List<SettingsChangeObserver>> changeObservers = new ConcurrentHashMap<String, List<SettingsChangeObserver>>();

    public void registerForChange(String config, SettingsChangeObserver observer) {
	getChangeObservers(config).add(observer);
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
	for (SettingsChangeObserver observer : getChangeObservers(config)) {
	    try {
		observer.configChanged(config, value);
	    } catch (Exception e) {
		LOG.error("Config " + config + "=" + value + " could not be applied by "
			+ observer.getClass().getName(), e);
	    }
	}
    }

    protected static List<SettingsChangeObserver> getChangeObservers(String config) {
	List<SettingsChangeObserver> list = changeObservers.get(config);
	if (list == null) {
	    list = new ArrayList<SettingsChangeObserver>();
	    changeObservers.put(config, list);
	}
	return list;
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
