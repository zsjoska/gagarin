package ro.gagarin.hibernate;

import ro.gagarin.DAOManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.UserDAO;
import ro.gagarin.session.Session;

public class HibernateDAOManager implements DAOManager {

	private static HibernateDAOManager INSTANCE = null;

	private HibernateDAOManager() {
	}

	public static synchronized HibernateDAOManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HibernateDAOManager();
		return INSTANCE;
	}

	public RoleDAO getRoleDAO(Session session) {
		return new HibernateRoleDAO(session);
	}

	/**
	 * This method returns the {@link UserDAO} implementation selected.
	 * 
	 * @return the configured {@link UserDAO} implementation
	 */
	public UserDAO getUserDAO(Session session) {
		return new HibernateUserDAO(session);
	}

}
