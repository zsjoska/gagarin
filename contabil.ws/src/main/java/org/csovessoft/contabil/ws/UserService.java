package org.csovessoft.contabil.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.csovessoft.contabil.ModelFactory;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.SessionNotFoundException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.session.Session;
import org.csovessoft.contabil.user.User;

@WebService
public class UserService {

	@WebMethod
	public String createUser(String sessionId, User user) throws SessionNotFoundException,
			FieldRequiredException, UserAlreadyExistsException {
		Session session = ModelFactory.getSessionManager().getSessionById(sessionId);
		if (session == null)
			throw new SessionNotFoundException(sessionId);

		// TODO: permission check

		ModelFactory.getUserManager().createUser(user);

		return user.getId();
	}
}
