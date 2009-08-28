package ro.gagarin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import ro.gagarin.ConfigurationManager;
import ro.gagarin.application.objects.AppConfig;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;

public class FileConfigurationManager extends ConfigHolder implements ConfigurationManager, FileChangeObserver {

    private static final String CONFIG_DIR = "CONFIG_DIR";
    private static final transient Logger LOG = Logger.getLogger(FileConfigurationManager.class);
    private static final ConfigurationManager INSTANCE = new FileConfigurationManager();
    private String cfgDir;
    private MonitoredFile cfgFile;

    public static ConfigurationManager getInstance() {
	return INSTANCE;
    }

    private FileConfigurationManager() {

	// try to guess the config directory
	cfgDir = System.getProperty(CONFIG_DIR);
	if (cfgDir == null) {
	    cfgDir = System.getenv(CONFIG_DIR);
	}

	// project build directory
	if (cfgDir == null) {
	    cfgDir = "../";
	}

	LOG.info("Config dir is set to " + new File(cfgDir).getAbsolutePath());

	this.cfgFile = new MonitoredFile(new File(cfgDir + "config.properties"), this);
	fileChanged(this.cfgFile.getFile());

	// for test import this config
	String[] newCfg = new String[Config.values().length];
	newCfg[Config._TEST_LOCAL_ONLY_.ordinal()] = Config._TEST_LOCAL_ONLY_.getDefaultValue();
	super.importConfig(newCfg);
    }

    @Override
    public InputStream getConfigFileStream(Config fileConfig) throws OperationException {
	String filename = getString(fileConfig);
	return getConfigFileStream(filename);
    }

    @Override
    public InputStream getConfigFileStream(String filename) throws OperationException {
	File file = new File(this.cfgDir + filename);
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

	LOG.error("Could not load file for config " + filename + " tried to load from " + file.getAbsolutePath()
		+ " and " + filename + " in classpath");
	throw new OperationException(ErrorCodes.ERROR_READING_FILE, "File not found:" + filename);
    }

    public List<ConfigEntry> getConfigValues() {
	ArrayList<ConfigEntry> cfgList = new ArrayList<ConfigEntry>();
	for (Config cfg : Config.values()) {
	    // do not export internal config controls
	    if (cfg.name().startsWith("_"))
		continue;
	    AppConfig cfgObj = new AppConfig();
	    cfgObj.setConfigName(cfg.name());
	    cfgObj.setConfigValue(getString(cfg));
	    if (isDefined(cfg)) {
		cfgObj.setConfigScope(ConfigScope.LOCAL);
	    } else {
		cfgObj.setConfigScope(ConfigScope.DEFAULT);
	    }

	    cfgList.add(cfgObj);
	}
	return cfgList;
    }

    @Override
    public void fileChanged(File file) {
	LOG.info("File change detected on " + file.getAbsolutePath());
	Properties prop = new Properties();
	FileInputStream fis;
	try {
	    fis = new FileInputStream(file);
	} catch (FileNotFoundException e) {
	    LOG.error("The file was not found:" + file.getName(), e);
	    return;
	}
	try {
	    prop.load(fis);
	    super.importConfig(prop);
	} catch (IOException e) {
	    LOG.error("IOException while reading the file " + file.getName(), e);
	}

	try {
	    fis.close();
	} catch (IOException e) {
	    LOG.error("IOException while closing the file " + file.getName(), e);
	}

    }
}
