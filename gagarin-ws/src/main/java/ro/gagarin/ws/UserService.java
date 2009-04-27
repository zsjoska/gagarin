package org.csovessoft.contabil.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.SessionNotFoundException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.user.User;

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
