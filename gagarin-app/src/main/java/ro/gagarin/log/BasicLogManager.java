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
		// super(aClass.getName());
		// setLevel(Level.ALL);
		logger = Logger.getLogger(aClass);
		this.session = session;
		this.si = session.toString() + "# ";

	}

	@Override
	public void debug(String message) {
		logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, si + message, null));
	}

	@Override
	public void action(AppLogAction action, Class<?> classInAction, String id, String detail) {
		System.err.println(session + "@ " + action.name() + "# " + id + ": "
				+ classInAction.getSimpleName() + ": " + detail);
	}

	@Override
	public void error(String string, Throwable t) {
		// super.error(string, t);
	}

}
