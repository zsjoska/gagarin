package ro.gagarin;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.session.Session;

public interface DAOManager {

	RoleDAO getRoleDAO(Session session) throws OperationException;

	UserDAO getUserDAO(Session session) throws OperationException;

	ConfigDAO getConfigDAO(Session session) throws OperationException;

}
