package ro.gagarin.log;

public interface AppLog {

	void debug(String string);

	void action(AppLogAction action, Class<?> classInAction, String id, String detail);

	void error(String string, Throwable t);

}
