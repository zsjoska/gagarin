package ro.gagarin.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
import ro.gagarin.utils.Statistic;

public abstract class UpdateQuery {

    private final BaseJdbcDAO dao;
    protected final AppLog LOG;
    private int updatedRowCount = 0;

    public UpdateQuery(BaseJdbcDAO dao) {
	this.dao = dao;
	this.LOG = dao.getLogger();
    }

    protected abstract String getSQL();

    protected abstract void fillParameters(PreparedStatement stmnt) throws SQLException;

    protected abstract void checkInput() throws FieldRequiredException;

    public int execute() throws OperationException, DataConstraintException {
	PreparedStatement stmnt = null;
	boolean success = false;
	try {
	    checkInput();
	    String sqlString = getSQL();
	    LOG.debug("Got SQL:" + sqlString);
	    Connection connection = this.dao.getConnection();
	    stmnt = connection.prepareStatement(sqlString);
	    fillParameters(stmnt);
	    LOG.debug("Executing SQL:" + sqlString);
	    doExecute(stmnt);
	    stmnt.close();
	    success = true;
	} catch (OperationException e) {
	    throw e;
	} catch (DataConstraintException e) {
	    // catching just to not be caught by the generic exception clause
	    throw e;
	} catch (SQLException e) {

	    // this exception could occur for syntax error in SQL, or other
	    // error in fillParameters
	    throw new OperationException(ErrorCodes.SQL_ERROR, e);
	} catch (Exception e) {

	    // for other error, e.g. null pointer access, etc
	    // during the data processing
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, e);
	} finally {
	    if (!success) {
		dao.markRollback();
	    }
	    if (stmnt != null) {
		try {
		    stmnt.close();
		} catch (SQLException e) {
		    LOG.error("Error while releasing the statement:", e);
		}
	    }
	}
	return this.updatedRowCount;
    }

    protected void doExecute(PreparedStatement stmnt) throws DataConstraintException, OperationException {
	try {
	    long start = System.currentTimeMillis();
	    updatedRowCount = stmnt.executeUpdate();
	    this.dao.markChangePending();
	    LOG.debug("Updated " + updatedRowCount + " rows");
	    Statistic.getByName("db.update." + getSQL()).add(start);
	} catch (SQLException e) {
	    // this exception should be converted to our nice exceptions
	    LOG.error("Error executing the query", e);
	    throw DataConstraintException.createException(e, getClass());
	}
    }

    protected Connection getConnection() throws OperationException {
	return dao.getConnection();
    }
}
