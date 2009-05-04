package ro.gagarin;

import ro.gagarin.session.Session;

public interface DAOManager {

	RoleDAO getRoleDAO(Session session);

	UserDAO getUserDAO(Session session);

}
