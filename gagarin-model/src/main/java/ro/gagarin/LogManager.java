package ro.gagarin;

import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;

public interface LogManager {

    AppLog getLoggingSession(Session session, Class<?> clazz);

}
