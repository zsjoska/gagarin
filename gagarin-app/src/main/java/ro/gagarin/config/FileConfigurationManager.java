package ro.gagarin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ro.gagarin.ConfigurationManager;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;

public class FileConfigurationManager extends ConfigHolder implements ConfigurationManager {

	private static final ConfigurationManager INSTANCE = new FileConfigurationManager();

	public static ConfigurationManager getInstance() {
		return INSTANCE;
	}

	private FileConfigurationManager() {

		// TODO: load from file

		String[] newCfg = new String[Config.values().length];
		// newCfg[Config.USER_SESSION_TIMEOUT.ordinal()] = "100000";
		newCfg[Config.SESSION_CHECK_PERIOD.ordinal()] = "10000";
		newCfg[Config.ADMIN_ROLE_NAME.ordinal()] = "ADMIN_ROLE";
		newCfg[Config.ADMIN_USER_NAME.ordinal()] = "admin";
		newCfg[Config.ADMIN_PASSWORD.ordinal()] = "password";
		super.importConfig(newCfg);
	}

	@Override
	public InputStream getConfigFileStream(Config fileConfig) throws OperationException {
		String filename = getString(fileConfig);
		File file = new File(filename);
		try {
			InputStream is = new FileInputStream(file);
			LOG.info("Loaded file " + file.getAbsolutePath());
			return is;
		} catch (FileNotFoundException e) {
			// ignore it as for now
		}
		InputStream is = this.getClass().getResourceAsStream(filename);
		if (is != null) {
			LOG.info("Loaded classpath file " + filename);
			return is;
		}
		is = this.getClass().getResourceAsStream('/' + filename);
		if (is != null) {
			LOG.info("Loaded classpath file /" + filename);
			return is;
		}

		LOG.error("Could not load file for config " + fileConfig.name() + " tried to load from "
				+ file.getAbsolutePath() + " and " + filename + " in classpath");
		throw new OperationException(ErrorCodes.ERROR_READING_FILE, "File not found:" + filename);
	}

}
