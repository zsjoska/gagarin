package ro.gagarin;

import org.junit.Test;

import ro.gagarin.config.Config;
import ro.gagarin.config.DBConfigManager;

public class ConfigTest {
	@Test
	public void testDBConfig() throws Exception {
		DBConfigManager instance = DBConfigManager.getInstance();
		String string;
		string = instance.getString(Config.JDBC_CONNECTION_URL);
		System.err.println(string);
		Thread.sleep(20000);
		string = instance.getString(Config.JDBC_CONNECTION_URL);
		System.err.println(string);
	}
}
