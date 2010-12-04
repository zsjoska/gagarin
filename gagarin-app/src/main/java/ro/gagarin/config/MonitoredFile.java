package ro.gagarin.config;

import java.io.File;

import org.apache.log4j.Logger;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.log.AppLog;
import ro.gagarin.manager.ConfigurationManager;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.ScheduleManager;
import ro.gagarin.scheduler.DefaultScheduleManager;
import ro.gagarin.scheduler.JobController;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.session.Session;

public class MonitoredFile implements SettingsChangeObserver {

    private static final transient Logger LOG = Logger.getLogger(MonitoredFile.class);

    public class StartupJob extends ScheduledJob {

	private final MonitoredFile monitoredFile;
	private final long fileCheckInterval;

	public StartupJob(String name, MonitoredFile monitoredFile, long fileCheckInterval) {
	    super(name, 10, 300);
	    this.monitoredFile = monitoredFile;
	    this.fileCheckInterval = fileCheckInterval;
	}

	@Override
	public void execute(Session session, AppLog log, JobController jobController) throws Exception {
	    ManagerFactory factory = BasicManagerFactory.getInstance();
	    if (factory == null) {
		return;
	    }
	    ScheduleManager scheduleManager = factory.getScheduleManager();
	    if (scheduleManager == null)
		return;

	    ConfigurationManager cfgMgr = factory.getConfigurationManager();
	    if (cfgMgr == null) {
		return;
	    }

	    job = new MonitoredFile.MonitorJob("FILE_MONITOR:" + file.getName(), this.monitoredFile, fileCheckInterval);
	    scheduleManager.scheduleJob(job, false);

	    // no more execution
	    jobController.markDone();
	}
    }

    private final File file;
    private final FileChangeObserver observer;
    private MonitorJob job;
    private long lastModified = 0;

    public File getFile() {
	return file;
    }

    public FileChangeObserver getObserver() {
	return observer;
    }

    public MonitoredFile(File file, FileChangeObserver observer) {
	this.file = file;
	this.observer = observer;

	LOG.info("Creating file monitor for file " + file.getAbsolutePath());

	lastModified = file.lastModified();

	// the file configuration manager calls this first which is
	// uninitialized
	// we have to be careful here
	long fileCheckInterval = Configuration.FILE_CHECK_INTERVAL;
	DefaultScheduleManager mgr = new DefaultScheduleManager();

	ScheduledJob statupjob = new MonitoredFile.StartupJob("FILE_MONITOR_STARTUP:" + file.getName(), this,
		fileCheckInterval);
	mgr.scheduleJob(statupjob, false);
    }

    public class MonitorJob extends ScheduledJob {

	private final MonitoredFile monitoredFile;

	public MonitorJob(String name, MonitoredFile monitoredFile, long fileCheckInterval) {
	    super(name, 10, fileCheckInterval);
	    this.monitoredFile = monitoredFile;

	}

	@Override
	public void execute(Session session, AppLog log, JobController jc) throws Exception {
	    long fileModification = monitoredFile.getFile().lastModified();
	    if (monitoredFile.lastModified != fileModification) {
		try {
		    monitoredFile.observer.fileChanged(file);
		} finally {
		    monitoredFile.lastModified = fileModification;
		}
	    }

	}
    }

    @Override
    public boolean configChanged(String config, String value) {
	if ("FILE_CHECK_INTERVAL".equals(config)) {
	    BasicManagerFactory.getInstance().getScheduleManager().updateJobRate(job.getId(), Long.valueOf(value));
	    return true;
	}
	return false;
    }
}
