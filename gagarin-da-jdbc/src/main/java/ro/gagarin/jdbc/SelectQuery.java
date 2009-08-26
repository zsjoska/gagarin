package ro.gagarin.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.utils.Statistic;

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

    @Override
    public void execute() throws OperationException {
	try {
	    long start = System.currentTimeMillis();
	    super.execute();
	    Statistic.getByName("db.query." + getSQL()).add(start);
	} catch (DataConstraintException e) {
	    // select queries shouldn't throw DataConstraintException
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	}
    };

    protected abstract void useResult(ResultSet rs) throws SQLException;
}
