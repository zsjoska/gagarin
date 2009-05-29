package ro.gagarin.jdbc;

import ro.gagarin.DAOManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.UserDAO;
import ro.gagarin.session.Session;

public class JdbcDAOManager implements DAOManager {

	private static JdbcDAOManager INSTANCE = null;

	private JdbcDAOManager() {
	}

	public static synchronized JdbcDAOManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JdbcDAOManager();
		return INSTANCE;
	}

	public RoleDAO getRoleDAO(Session session) {
		return new JdbcRoleDAO(session);
	}

	/**
	 * This method returns the {@link UserDAO} implementation selected.
	 * 
	 * @return the configured {@link UserDAO} implementation
	 */
	public UserDAO getUserDAO(Session session) {
		return new JdbcUserDAO(session);
	}

}
