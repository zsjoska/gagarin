package ro.gagarin;

import ro.gagarin.exceptions.OperationException;

public interface BaseDAO {

    void checkCreateDependencies(ConfigurationManager cfgManager) throws OperationException;

    void release() throws OperationException;
}
