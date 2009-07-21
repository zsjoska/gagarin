package ro.gagarin.config;

import java.io.InputStream;

import ro.gagarin.ConfigurationManager;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public class DBConfigManager extends ConfigHolder implements ConfigurationManager {

	private final ConfigurationManager localConfig;
	private final Session session;

	public DBConfigManager(Session session, ConfigurationManager instance) {
		this.session = session;
		this.localConfig = instance;
	}

	@Override
	public InputStream getConfigFileStream(Config file) throws OperationException {
		return localConfig.getConfigFileStream(file);
	}

}
