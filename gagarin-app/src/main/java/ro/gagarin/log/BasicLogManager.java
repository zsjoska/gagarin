package ro.gagarin.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import ro.gagarin.application.objects.AppLogEntry;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public class BasicLogManager implements AppLog {

	/**
	 * The fully qualified name of the Category class.
	 */
	private static final String FQCN = BasicLogManager.class.getName();

	private final Session session;
	private final String si;
	private final Logger logger;
	private User user;

	private static ArrayList<LogEntry> logList = new ArrayList<LogEntry>();

	public BasicLogManager(Session session, Class<?> aClass) {
		logger = Logger.getLogger(aClass);
		this.session = session;
		this.si = session.toString() + "# ";
		this.user = session.getUser();
	}

	@Override
	public void debug(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.DEBUG_INT))
			return;
		if (Level.DEBUG.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, si
					+ message, null));
		}
		addLogEntryIfPermitted(Level.DEBUG, message);
	}

	private void addLogEntryIfPermitted(Level level, String message) {
		if (persistLevelEnabled(level)) {
			AppLogEntry logEntry = new AppLogEntry();
			logEntry.setDate(System.currentTimeMillis());
			logEntry.setId(AppLogEntry.getNextId());
			logEntry.setLogLevel(level.toString());
			logEntry.setUser(this.user);
			logEntry.setMessage(message);
			logEntry.setId(AppLogEntry.getNextId());
			logList.add(logEntry);
		}
	}

	private boolean persistLevelEnabled(Level debug) {
		return true;
	}

	@Override
	public void debug(String message, Throwable t) {
		if (logger.getLoggerRepository().isDisabled(Level.DEBUG_INT))
			return;
		if (Level.DEBUG.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, si
					+ message, t));
		}
		addLogEntryIfPermitted(Level.DEBUG, message + " Exception:"
				+ t.getMessage());
	}

	@Override
	public void info(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.INFO_INT))
			return;
		if (Level.INFO.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.INFO, si
					+ message, null));
		}
		addLogEntryIfPermitted(Level.INFO, message);
	}

	@Override
	public void info(String message, Throwable t) {
		if (logger.getLoggerRepository().isDisabled(Level.INFO_INT))
			return;
		if (Level.INFO.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.INFO, si
					+ message, t));
		}
		addLogEntryIfPermitted(Level.INFO, message + " Exception:"
				+ t.getMessage());
	}

	@Override
	public void error(String message, Throwable t) {
		if (logger.getLoggerRepository().isDisabled(Level.ERROR_INT))
			return;
		if (Level.ERROR.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.ERROR, si
					+ message, t));
		}
		addLogEntryIfPermitted(Level.ERROR, message + " Exception:"
				+ t.getMessage());
	}

	@Override
	public void error(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.ERROR_INT))
			return;
		if (Level.ERROR.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.ERROR, si
					+ message, null));
		}
		addLogEntryIfPermitted(Level.ERROR, message);
	}

	@Override
	public void warn(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.WARN_INT))
			return;
		if (Level.WARN.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.WARN, si
					+ message, null));
		}
		addLogEntryIfPermitted(Level.WARN, message);
	}

	@Override
	public void action(AppLogAction action, Class<?> classInAction, String id,
			String detail) {
		System.err.println(session + "@ " + action.name() + "# " + id + ": "
				+ classInAction.getSimpleName() + ": " + detail);
	}

	@Override
	public List<LogEntry> getLogEntries(String user) {
		return logList;
	}

}
