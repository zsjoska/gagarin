package ro.gagarin.ws.objects;

import ro.gagarin.log.LogEntry;
import ro.gagarin.user.BaseEntity;

public class WSLogEntry extends BaseEntity implements LogEntry {

    private String message;
    private String logLevel;
    private Long date;
    private WSUser user;
    private String sessionID;

    public WSLogEntry() {
    }

    @Override
    public String toString() {
	return "WSLogEntry [date=" + date + ", logLevel=" + logLevel + ", user=" + user + ", getId()=" + getId()
		+ ", message=" + message + "]";
    }

    public WSLogEntry(LogEntry logEntry) {
	this.message = logEntry.getMessage();
	this.logLevel = logEntry.getLogLevel();
	this.date = logEntry.getDate();
	if (logEntry.getUser() != null)
	    this.user = new WSUser(logEntry.getUser());
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public void setLogLevel(String logLevel) {
	this.logLevel = logLevel;
    }

    public void setDate(Long date) {
	this.date = date;
    }

    @Override
    public Long getDate() {
	return this.date;
    }

    @Override
    public String getLogLevel() {
	return this.logLevel;
    }

    @Override
    public String getMessage() {
	return this.message;
    }

    @Override
    public String getSessionID() {
	return this.sessionID;
    }

    public void setSessionID(String sessionID) {
	this.sessionID = sessionID;
    }

    @Override
    public WSUser getUser() {
	return this.user;
    }

    public void setUser(WSUser user) {
	this.user = user;
    }

}
