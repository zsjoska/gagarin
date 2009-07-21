package ro.gagarin;

import java.util.HashMap;

import ro.gagarin.exceptions.OperationException;

public interface ConfigDAO extends BaseDAO {

	long getLastUpdateTime() throws OperationException;

	HashMap<String, String> listConfigurations();

}
