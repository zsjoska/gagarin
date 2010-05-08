package ro.gagarin.application.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.log.LogEntry;
import ro.gagarin.user.User;
import ro.gagarin.utils.ConversionUtils;

public class AppLogEntry extends BaseEntity implements LogEntry {

    private String message;
    private String logLevel;
    private Long date;
    private User user;

    public AppLogEntry(LogEntry logEntry) {
	this.message = logEntry.getMessage();
	this.logLevel = logEntry.getLogLevel();
	this.date = logEntry.getDate();
	this.user = logEntry.getUser();
    }

    public AppLogEntry() {
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
	return this.getSessionID();
    }

    @Override
    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @Override
    public String toString() {
	return ConversionUtils.logEntry2String(this);
    }

}
