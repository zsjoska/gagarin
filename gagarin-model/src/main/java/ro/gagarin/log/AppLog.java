package ro.gagarin.log;

import java.util.List;

public interface AppLog {

	String SUCCESS = "SUCCESS";
	String FAILED = "FAILED";

	void debug(String message);

	void debug(String message, Throwable t);

	void info(String message);

	void info(String message, Throwable t);

	void error(String message, Throwable t);

	void error(String message);

	void warn(String message);

	void action(AppLogAction action, Class<?> classInAction, String id, String detail);

	List<LogEntry> getLogEntries(String user);

}
