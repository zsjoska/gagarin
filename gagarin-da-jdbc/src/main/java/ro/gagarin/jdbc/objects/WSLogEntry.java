package ro.gagarin.jdbc.objects;

import ro.gagarin.log.LogEntry;
import ro.gagarin.user.BaseEntity;
import ro.gagarin.user.User;

public class WSLogEntry extends BaseEntity implements LogEntry {

	private String message;
	private String logLevel;
	private Long date;
	private User user;

	@Override
	public String toString() {
		return "WSLogEntry [date=" + date + ", logLevel=" + logLevel
				+ ", user=" + user + ", getId()=" + getId() + ", message="
				+ message + "]";
	}

	public WSLogEntry(LogEntry logEntry) {
		this.message = logEntry.getMessage();
		this.logLevel = logEntry.getLogLevel();
		this.date = logEntry.getDate();
		this.user = logEntry.getUser();
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

}
