package ro.gagarin.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.ModelFactory;
import ro.gagarin.SessionManager;
import ro.gagarin.UserManager;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

@WebService
public class UserService {

	private static final transient Logger LOG = Logger.getLogger(UserService.class);

	@WebMethod
	public Long createUser(String sessionId, User user) throws SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException {
		LOG.info("createUser " + user.getUsername());

		SessionManager sessionManager = ModelFactory.getSessionManager();
		Session session = sessionManager.getSessionById(sessionId);
		UserManager userManager = ModelFactory.getUserManager(session);

		try {
			if (session == null)
				throw new SessionNotFoundException(sessionId);

			// TODO: permission check

			long userId = userManager.createUser(user);
			LOG.info("Created User" + user.getId() + ":" + user.getUsername() + "; session:"
					+ sessionId);
			return userId;
		} finally {
			sessionManager.release();
		}
	}
}
