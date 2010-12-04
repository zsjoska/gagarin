package ro.gagarin.manager;

import java.io.InputStream;
import java.util.List;

import ro.gagarin.config.ConfigEntry;
import ro.gagarin.config.SettingsChangeObserver;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;
import sun.security.krb5.Config;

/**
 * Base interface for the application to interact with the application
 * configuration.
 * 
 * @author zsjoska
 * 
 */
public interface ConfigurationManager extends BaseManager {

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
    void registerForChange(String config, SettingsChangeObserver observer);

    /**
     * Modifies a configuration value and notifies the change observers.
     * 
     * @param session
     *            an active session to be used for config change
     * @param config
     *            configuration key
     * @param value
     *            configuration value
     * @throws OperationException
     */
    void setConfigValue(Session session, String config, String value) throws OperationException;

    InputStream getConfigFileStream(String file) throws OperationException;

    boolean isDefined(String config);

    List<ConfigEntry> getConfigValues();

    void setConfigValue(Session session, ConfigEntry config) throws OperationException;

    void loadConfiguration(Object param);

}
