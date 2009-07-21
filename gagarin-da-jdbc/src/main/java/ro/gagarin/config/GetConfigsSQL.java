package ro.gagarin.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ro.gagarin.JdbcConfigDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBConfig;

public class GetConfigsSQL extends SelectQuery {

	private ArrayList<ConfigEntry> configs;

	public GetConfigsSQL(BaseJdbcDAO dao) {
		super(dao, null);
	}

	@Override
	protected void useResult(ResultSet rs) throws SQLException {
		this.configs = new ArrayList<ConfigEntry>();
		while (rs.next()) {
			DBConfig config = new DBConfig();
			config.setId(rs.getLong("id"));
			config.setConfigName(rs.getString("configName"));
			config.setConfigValue(rs.getString("configValue"));
			this.configs.add(config);
		}
	}

	@Override
	protected void fillParameters(PreparedStatement stmnt) throws SQLException {
		// nothing to fill
	}

	@Override
	protected String getSQL() {
		return "SELECT id, configName, configValue FROM Config";
	}

	public static ArrayList<ConfigEntry> execute(JdbcConfigDAO jdbcConfigDAO)
			throws OperationException {
		GetConfigsSQL q = new GetConfigsSQL(jdbcConfigDAO);
		q.execute();
		return q.configs;
	}

}
