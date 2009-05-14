package ro.gagarin.dummyimpl;

import ro.gagarin.DAOManager;
import ro.gagarin.RoleDAO;
import ro.gagarin.UserDAO;
import ro.gagarin.session.Session;

public class DummyDAOManager implements DAOManager {

	private static final DAOManager INSTANCE = new DummyDAOManager();

	public static DAOManager getInstance() {

		return INSTANCE;
	}

	@Override
	public RoleDAO getRoleDAO(Session session) {

		return new DummyRoleDAO();
	}

	@Override
	public UserDAO getUserDAO(Session session) {

		return new DummyUserDAO();
	}

}
