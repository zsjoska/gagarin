package ro.gagarin.dao;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.BaseManager;
import ro.gagarin.session.Session;

public interface DAOManager extends BaseManager {

    RoleDAO getRoleDAO(Session session) throws OperationException;

    UserDAO getUserDAO(Session session) throws OperationException;

    ConfigDAO getConfigDAO(Session session) throws OperationException;

}
