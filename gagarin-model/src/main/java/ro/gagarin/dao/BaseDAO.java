package ro.gagarin.dao;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.manager.ConfigurationManager;

public interface BaseDAO {

    void checkCreateDependencies(ConfigurationManager cfgManager) throws OperationException;

    void markRollback();

    void release() throws OperationException;
}
