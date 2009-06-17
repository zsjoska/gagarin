package ro.gagarin.log;

public interface AppLog {

	void debug(String message);

	void debug(String message, Throwable t);

	void info(String message);

	void info(String message, Throwable t);

	void error(String message, Throwable t);

	void error(String message);

	void action(AppLogAction action, Class<?> classInAction, String id, String detail);

}
