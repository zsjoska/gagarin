package org.csovessoft.contabil.config;

public interface SettingsChangeObserver {
	boolean configChanged(Config config, String value);
}
