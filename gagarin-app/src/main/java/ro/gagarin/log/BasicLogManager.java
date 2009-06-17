package ro.gagarin.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import ro.gagarin.session.Session;

public class BasicLogManager implements AppLog {

	/**
	 * The fully qualified name of the Category class.
	 */
	private static final String FQCN = BasicLogManager.class.getName();

	private final Session session;
	private final String si;
	private final Logger logger;

	public BasicLogManager(Session session, Class<?> aClass) {
		logger = Logger.getLogger(aClass);
		this.session = session;
		this.si = session.toString() + "# ";

	}

	@Override
	public void debug(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.DEBUG_INT))
			return;
		if (Level.DEBUG.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, si + message, null));
		}
	}

	@Override
	public void debug(String message, Throwable t) {
		if (logger.getLoggerRepository().isDisabled(Level.DEBUG_INT))
			return;
		if (Level.DEBUG.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, si + message, t));
		}
	}

	@Override
	public void info(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.INFO_INT))
			return;
		if (Level.INFO.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.INFO, si + message, null));
		}
	}

	@Override
	public void info(String message, Throwable t) {
		if (logger.getLoggerRepository().isDisabled(Level.INFO_INT))
			return;
		if (Level.INFO.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.INFO, si + message, t));
		}
	}

	@Override
	public void error(String message, Throwable t) {
		if (logger.getLoggerRepository().isDisabled(Level.ERROR_INT))
			return;
		if (Level.ERROR.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.ERROR, si + message, t));
		}
	}

	@Override
	public void error(String message) {
		if (logger.getLoggerRepository().isDisabled(Level.ERROR_INT))
			return;
		if (Level.ERROR.isGreaterOrEqual(logger.getEffectiveLevel())) {
			logger.callAppenders(new LoggingEvent(FQCN, logger, Level.ERROR, si + message, null));
		}
	}

	@Override
	public void action(AppLogAction action, Class<?> classInAction, String id, String detail) {
		System.err.println(session + "@ " + action.name() + "# " + id + ": "
				+ classInAction.getSimpleName() + ": " + detail);
	}

}
