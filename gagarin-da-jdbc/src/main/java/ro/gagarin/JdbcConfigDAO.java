package ro.gagarin;

import java.util.ArrayList;

import ro.gagarin.config.Config;
import ro.gagarin.config.ConfigEntry;
import ro.gagarin.config.GetConfigValueSQL;
import ro.gagarin.config.GetConfigsSQL;
import ro.gagarin.config.InsertConfigValueSQL;
import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.objects.DBConfig;
import ro.gagarin.session.Session;

public class JdbcConfigDAO extends BaseJdbcDAO implements ConfigDAO {

	public JdbcConfigDAO(Session session) throws OperationException {
		super(session);
	}

	@Override
	public long getLastUpdateTime() throws OperationException, DataConstraintException {
		String value = GetConfigValueSQL.execute(this, Config._LAST_UPDATE_TIME_);
		if (value == null) {
			long updateTime = System.currentTimeMillis();
			DBConfig config = new DBConfig();
			config.setConfigName(Config._LAST_UPDATE_TIME_.name());
			config.setConfigValue("" + updateTime);
			new InsertConfigValueSQL(this, config).execute();
			return updateTime;
		}
		return 0;
	}

	@Override
	public ArrayList<ConfigEntry> listConfigurations() throws OperationException {

		return GetConfigsSQL.execute(this);
	}
}
