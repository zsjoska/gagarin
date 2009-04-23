package org.csovessoft.contabil.config;


public interface SettingsChangeObserver {
	void configChanged(Config config, String value);
}
