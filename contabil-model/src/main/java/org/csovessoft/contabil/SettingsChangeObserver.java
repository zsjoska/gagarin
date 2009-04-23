package org.csovessoft.contabil;

import org.csovessoft.contabil.config.Config;

public interface SettingsChangeObserver {
	void configChanged(Config config, String value);
}
