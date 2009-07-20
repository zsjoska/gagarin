package ro.gagarin.testutil;

import ro.gagarin.BasicManagerFactory;
import ro.gagarin.ManagerFactory;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.session.Session;

public class TUtil {
	private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

	public static Session createTestSession() {
		Session session = FACTORY.getSessionManager().createSession(null, null, FACTORY);
		try {
			FACTORY.getSessionManager().acquireSession(session.getSessionString());
		} catch (SessionNotFoundException e) {
			// surprise
			throw new RuntimeException(e);
		}
		AppUser user = new AppUser();
		user.setUsername("tester");
		session.setUser(user);
		return session;
	}

}
