package ro.gagarin;

import org.apache.log4j.Logger;

import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;

public class BasicAuthenticationManager implements AuthenticationManager {

	private static final transient Logger LOG = Logger.getLogger(BasicAuthenticationManager.class);

	private final Session session;

	public BasicAuthenticationManager(Session session) {
		this.session = session;
	}

	@Override
	public User userLogin(String username, String password, String[] extra)
			throws ItemNotFoundException {
		UserDAO userDAO = BasicManagerFactory.getInstance().getDAOManager()
				.getUserDAO(this.session);
		User user = userDAO.userLogin(username, password);

		// TODO: change it a bit to look more important
		this.session.setUser(user);
		LOG.info("User " + user.getId() + ":" + user.getUsername() + " was bound to session "
				+ session.getId());
		return user;
	}
}
