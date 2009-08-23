package ro.gagarin.log;

import ro.gagarin.user.User;

public interface LogEntry {
    Long getId();

    User getUser();

    String getSessionID();

    String getLogLevel();

    Long getDate();

    String getMessage();
}
