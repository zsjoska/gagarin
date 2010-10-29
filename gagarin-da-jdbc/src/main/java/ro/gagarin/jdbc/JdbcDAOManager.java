package ro.gagarin.jdbc;

import ro.gagarin.dao.ConfigDAO;
import ro.gagarin.dao.DAOManager;
import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.dao.UserExtraDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public class JdbcDAOManager implements DAOManager {

    public JdbcDAOManager() {
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

    @Override
    public UserExtraDAO getUserExtraDAO(Session session) throws OperationException {
	return new JdbcUserExtraDAO(session);
    }
}
