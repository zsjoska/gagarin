package ro.gagarin.jdbc;

import java.util.ArrayList;

import ro.gagarin.config.Config;
import ro.gagarin.config.ConfigEntry;
import ro.gagarin.config.GetConfigValueSQL;
import ro.gagarin.config.GetConfigsSQL;
import ro.gagarin.config.InsertConfigValueSQL;
import ro.gagarin.config.UpdateConfigValueSQL;
import ro.gagarin.dao.ConfigDAO;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.objects.DBConfig;
import ro.gagarin.session.Session;

public class JdbcConfigDAO extends BaseJdbcDAO implements ConfigDAO {

    public JdbcConfigDAO(Session session) throws OperationException {
	super(session);
    }

    @Override
    public long getLastUpdateTime() throws OperationException, DataConstraintException {
	String value = GetConfigValueSQL.execute(this, Config._LAST_UPDATE_TIME_.name());
	if (value == null) {
	    long updateTime = System.currentTimeMillis();
	    DBConfig config = new DBConfig();
	    config.setId(DBConfig.getNextId());
	    config.setConfigName(Config._LAST_UPDATE_TIME_.name());
	    config.setConfigValue("" + updateTime);
	    new InsertConfigValueSQL(this, config).execute();
	    return updateTime;
	}
	try {
	    Long longValue = Long.valueOf(value);
	    return longValue;
	} catch (Exception e) {
	    throw new OperationException(ErrorCodes.INTERNAL_ERROR, e);
	}

    }

    @Override
    public ArrayList<ConfigEntry> listConfigurations() throws OperationException {

	return GetConfigsSQL.execute(this);
    }

    @Override
    public void setConfigValue(ConfigEntry cfg) throws OperationException, DataConstraintException {
	DBConfig config = new DBConfig(cfg);
	config.setId(DBConfig.getNextId());
	String value = GetConfigValueSQL.execute(this, cfg.getConfigName());
	if (value == null) {
	    new InsertConfigValueSQL(this, config).execute();
	} else if (!value.equals(cfg.getConfigValue())) {
	    new UpdateConfigValueSQL(this, config).execute();
	}
	config = new DBConfig();
	config.setConfigName(Config._LAST_UPDATE_TIME_.name());
	config.setConfigValue("" + System.currentTimeMillis());
	config.setId(DBConfig.getNextId());
	new UpdateConfigValueSQL(this, config).execute();
    }
}
