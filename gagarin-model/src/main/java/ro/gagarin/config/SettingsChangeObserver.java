package ro.gagarin.config;

public interface SettingsChangeObserver {

    boolean configChanged(String config, String value);

}
