package ro.gagarin.jdbc;

import ro.gagarin.JdbcConfigDAO;
import ro.gagarin.dao.ConfigDAO;
import ro.gagarin.dao.DAOManager;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public class JdbcDAOManager implements DAOManager {

    private static JdbcDAOManager INSTANCE = null;

    private JdbcDAOManager() {
    }

    // TODO :(1) get rid of this singleton
    public static synchronized JdbcDAOManager getInstance() {
	if (INSTANCE == null)
	    INSTANCE = new JdbcDAOManager();
	return INSTANCE;
    }

    public RoleDAO getRoleDAO(Session session) throws OperationException {
	return new JdbcRoleDAO(session);
    }

    /**
     * This method returns the {@link UserDAO} implementation selected.
     * 
     * @return the configured {@link UserDAO} implementation
     * @throws OperationException
     */
    public UserDAO getUserDAO(Session session) throws OperationException {
	return new JdbcUserDAO(session);
    }

    @Override
    public ConfigDAO getConfigDAO(Session session) throws OperationException {
	return new JdbcConfigDAO(session);
    }

    @Override
    public void initializeManager() {
	// nothing to initialize here
    }
}
