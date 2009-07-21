package ro.gagarin;

import java.util.HashMap;

import ro.gagarin.config.Config;
import ro.gagarin.config.GetSingleConfigValueSQL;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.session.Session;

public class JdbcConfigDAO extends BaseJdbcDAO implements ConfigDAO {

	public JdbcConfigDAO(Session session) throws OperationException {
		super(session);
	}

	@Override
	public long getLastUpdateTime() throws OperationException {
		String value = GetSingleConfigValueSQL.execute(this, Config._LAST_UPDATE_TIME_);
		return 0;
	}

	@Override
	public HashMap<String, String> listConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}
}
