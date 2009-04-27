package ro.gagarin.config;

import java.util.ArrayList;

public class DefaultConfiguration {

	private static ArrayList<String> defConfigs = new ArrayList<String>();

	static {
		addValue(Config.USER_SESSION_TIMEOUT, "" + 1000 * 60 * 5);
		addValue(Config.SESSION_CHECK_PERIOD, "" + 1000 * 30);
	}

	private static void addValue(Config config, String object) {
		defConfigs.add(config.ordinal(), object);
	}

	public static String getValue(Config config) {
		return defConfigs.get(config.ordinal());
	}

}
