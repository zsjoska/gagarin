package ro.gagarin;

import ro.gagarin.config.Config;
import ro.gagarin.config.SettingsChangeObserver;

/**
 * Base interface for the application to interact with the application
 * configuration.
 * 
 * @author zsjoska
 * 
 */
public interface ConfigurationManager {

	/**
	 * Returns a configuration value converted to long
	 * 
	 * @param config
	 *            the requested configuration
	 * @return the value of configuration
	 */
	long getLong(Config config);

	/**
	 * An object implementing {@link SettingsChangeObserver} interface could
	 * register with this method for change notification when a configuration
	 * entry is modified. When a configuration entry changes, the configuration
	 * manager will call the
	 * {@link SettingsChangeObserver#configChanged(Config, String)} method for
	 * all observers.
	 * 
	 * @param observer
	 *            the object requesting change notification
	 */
	void registerForChange(SettingsChangeObserver observer);

	/**
	 * Modifies a configuration value and notifies the change observers.
	 * 
	 * @param config
	 *            configuration key
	 * @param value
	 *            configuration value
	 */
	void setConfigValue(Config config, String value);

}
