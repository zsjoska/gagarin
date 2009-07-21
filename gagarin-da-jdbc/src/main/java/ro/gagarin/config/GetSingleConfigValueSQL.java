package ro.gagarin.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;

public class GetSingleConfigValueSQL extends SelectQuery {

	private final String config;
	private String lastUpdateTime = null;

	public GetSingleConfigValueSQL(BaseJdbcDAO dao, String config) {
		super(dao, null);
		this.config = config;
	}

	@Override
	protected void useResult(ResultSet rs) throws SQLException {
		this.lastUpdateTime = null;
		if (rs.next()) {
			this.lastUpdateTime = rs.getString("configValue");
		}
	}

	@Override
	protected void fillParameters(PreparedStatement stmnt) throws SQLException {
		stmnt.setString(1, this.config);
	}

	@Override
	protected String getSQL() {
		return "SELECT configValue FROM Config WHERE configName = ?";
	}

	public static String execute(BaseJdbcDAO dao, Config lastUpdateTime) throws OperationException {
		GetSingleConfigValueSQL q = new GetSingleConfigValueSQL(dao, lastUpdateTime.name());
		q.execute();
		return q.lastUpdateTime;
	}
}
