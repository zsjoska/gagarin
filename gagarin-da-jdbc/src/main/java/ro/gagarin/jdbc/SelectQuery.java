package ro.gagarin.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SelectQuery extends UpdateQuery {

	public SelectQuery(BaseJdbcDAO dao, Class<?> objectClass) {
		super(dao, objectClass);
	}

	protected void doExecute(PreparedStatement stmnt) {
		ResultSet result;
		try {
			result = stmnt.executeQuery();
			useResult(result);
		} catch (SQLException e) {

		}
	}

	protected abstract void useResult(ResultSet resultSet) throws SQLException;
}
