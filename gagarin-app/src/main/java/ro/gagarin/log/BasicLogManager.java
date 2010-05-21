package ro.gagarin.log;

import ro.gagarin.manager.LogManager;
import ro.gagarin.session.Session;

public class BasicLogManager implements LogManager {

    @Override
    public AppLog getLoggingSession(Session session, Class<?> clazz) {
	return new BasicApplicationLogger(session, clazz);
    }

}
