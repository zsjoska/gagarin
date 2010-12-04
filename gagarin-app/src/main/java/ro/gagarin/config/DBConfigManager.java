package ro.gagarin.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.application.objects.AppConfig;
import ro.gagarin.dao.ConfigDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBConfig;
import ro.gagarin.log.AppLog;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.scheduler.JobController;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.session.Session;

public class DBConfigManager extends ConfigHolder implements ConfigurationManager, SettingsChangeObserver {

    private static final transient Logger LOG = Logger.getLogger(DBConfigManager.class);

    private static ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private static final DBConfigManager INSTANCE = new DBConfigManager(FACTORY.getConfigurationManager());
    private ConfigImportJob configImportJob;

    static {
	long period = Configuration.DB_CONFIG_CHECK_PERIOD;

	INSTANCE.registerForChange("DB_CONFIG_CHECK_PERIOD", INSTANCE);
	INSTANCE.configImportJob = new DBConfigManager.ConfigImportJob("DB_CONFIG_IMPORT", 0, period);
	FACTORY.getScheduleManager().scheduleJob(INSTANCE.configImportJob, true);
    }

    public static class ConfigImportJob extends ScheduledJob {
	public ConfigImportJob(String name, long initialWait, long period) {
	    super(name, initialWait, period);
	}

	@Override
	public void execute(Session session, AppLog log, JobController jc) throws Exception {
	    ConfigDAO configDAO = FACTORY.getDAOManager().getConfigDAO(session);
	    long lastUpdateTime = configDAO.getLastUpdateTime();
	    log.debug("DBLUT = " + lastUpdateTime + " CacheLUT=" + INSTANCE.getLastUpdateTime());
	    if (lastUpdateTime > INSTANCE.getLastUpdateTime()) {

		// TODO:(3) use TB generated timestamp
		synchronized (INSTANCE) {
		    long lastQuery = System.currentTimeMillis();
		    ArrayList<ConfigEntry> cfgValues = configDAO.listConfigurations();
		    INSTANCE.importConfigMap(cfgValues, log);
		    INSTANCE.setLastUpdateTime(lastQuery);
		    INSTANCE.notify();
		    LOG.debug("DB import done");
		}
	    }
	}
    }

    private final ConfigurationManager localConfig;
    private long lastUpdateTime = 0;
    private long lastChangeRequest = System.currentTimeMillis();

    private DBConfigManager(ConfigurationManager localCfg) {
	this.localConfig = localCfg;
    }

    private void importConfigMap(ArrayList<ConfigEntry> cfgValues, AppLog log) {

	Properties cfgs = new Properties();
	for (ConfigEntry configEntry : cfgValues) {
	    // don't import entries defined locally
	    if (!localConfig.isDefined(configEntry.getConfigName())) {
		cfgs.put(configEntry.getConfigName(), configEntry.getConfigValue());
	    }
	}
	importConfig(cfgs);
    }

    public void setLastUpdateTime(long lastQuery) {
	this.lastUpdateTime = lastQuery;

    }

    public long getLastUpdateTime() {
	return this.lastUpdateTime;
    }

    @Override
    public InputStream getConfigFileStream(String file) throws OperationException {
	return localConfig.getConfigFileStream(file);
    }

    public static DBConfigManager getInstance() {
	return INSTANCE;
    }

    @Override
    public synchronized void setConfigValue(Session session, String config, String value) throws OperationException {

	AppLog log = session.getManagerFactory().getLogManager().getLoggingSession(session, DBConfigManager.class);
	log.info("Config change requested: " + config + "=" + value + "(" + Configuration.getAsString(config) + ")");
	// local config has precedence...
	if (localConfig.isDefined(config)) {
	    // TODO: review if exception should be thrown
	    log.error("This is a local config. Modify the file instead" + config + "=" + value);
	    return;
	}

	// if (value.equalsIgnoreCase(getString(config)))
	// return;

	this.lastChangeRequest = System.currentTimeMillis();

	ConfigDAO configDAO = FACTORY.getDAOManager().getConfigDAO(session);
	DBConfig cfg = new DBConfig();
	cfg.setConfigName(config);
	cfg.setConfigValue(value);
	try {
	    configDAO.setConfigValue(cfg);
	} catch (DataConstraintException e) {
	    LOG.error("Error setting config " + config + "=" + value, e);
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}
	FACTORY.getScheduleManager().triggerExecution(configImportJob);
    }

    @Override
    public boolean configChanged(String config, String value) {
	FACTORY.getScheduleManager().updateJobRate(configImportJob.getId(), Long.valueOf(value));
	return true;
    }

    public List<ConfigEntry> getConfigValues() {
	Properties props = Configuration.exportProperies();
	ArrayList<ConfigEntry> cfgList = new ArrayList<ConfigEntry>();
	for (Entry<Object, Object> cfg : props.entrySet()) {
	    // do not export internal config controls
	    String key = cfg.getKey().toString();
	    String value = cfg.getValue().toString();
	    if (key.startsWith("_")) {
		continue;
	    }

	    AppConfig cfgObj = new AppConfig();
	    cfgObj.setConfigName(key);
	    cfgObj.setConfigValue(value);
	    if (isDefined(key)) {
		cfgObj.setConfigScope(ConfigScope.DB);
	    } else if (localConfig.isDefined(key)) {
		cfgObj.setConfigScope(ConfigScope.LOCAL);
	    } else {
		cfgObj.setConfigScope(ConfigScope.DEFAULT);
	    }
	    cfgList.add(cfgObj);
	}
	return cfgList;
    }

    /**
     * Wait until the last requested change to actually happen. For testing
     * purposes only.
     * 
     * @throws InterruptedException
     */
    public void waitForDBImport() throws InterruptedException {
	LOG.info("Waiting for DB import...");
	synchronized (this) {
	    while (lastChangeRequest > lastUpdateTime) {
		this.wait();
	    }
	}
	LOG.info("DB import done.");
    }

    @Override
    public void loadConfiguration(Object param) {

    }

    @Override
    public void initializeManager() {
    }
}
