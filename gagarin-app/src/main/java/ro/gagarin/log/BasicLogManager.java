package ro.gagarin.log;

import ro.gagarin.session.Session;

public class BasicLogManager implements AppLog {

	private final Session session;

	public BasicLogManager(Session session) {
		this.session = session;

	}

	@Override
	public void debug(String string) {

	}

	@Override
	public void action(AppLogAction action, Class<?> classInAction, String id, String detail) {
		System.err.println(session + "@ " + action.name() + "# " + id + ": "
				+ classInAction.getSimpleName() + ": " + detail);
	}

	@Override
	public void error(String string, Throwable t) {
		System.err.println(string + ":" + t.getMessage());
		t.printStackTrace();

	}

}
