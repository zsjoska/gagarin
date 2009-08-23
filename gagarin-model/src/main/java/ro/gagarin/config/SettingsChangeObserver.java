package ro.gagarin.config;

public interface SettingsChangeObserver {
    boolean configChanged(Config config, String value);
}
