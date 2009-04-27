package ro.gagarin.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ro.gagarin.ModelFactory;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.SessionNotFoundException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

@WebService
public class UserService {

	private static final transient Logger LOG = Logger.getLogger(UserService.class);

	@WebMethod
	public Long createUser(Long sessionId, User user) throws SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException {
		LOG.info("createUser " + user.getUsername());
		Session session = ModelFactory.getSessionManager().getSessionById(sessionId);
		if (session == null)
			throw new SessionNotFoundException(sessionId);

		// TODO: permission check

		long userId = ModelFactory.getUserManager().createUser(user);
		LOG.info("Created User" + user.getId() + ":" + user.getUsername() + "; session:"
				+ sessionId);
		return userId;
	}
}
