package ro.gagarin;

import java.util.ArrayList;

import ro.gagarin.config.ConfigEntry;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.OperationException;

public interface ConfigDAO extends BaseDAO {

	long getLastUpdateTime() throws OperationException, DataConstraintException;

	ArrayList<ConfigEntry> listConfigurations() throws OperationException;

}
