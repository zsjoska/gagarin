package ro.gagarin.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
import ro.gagarin.utils.Statistic;

public abstract class UpdateQuery {

    private final BaseJdbcDAO dao;
    private final Class<?> objectClass;
    protected final AppLog LOG;

    public UpdateQuery(BaseJdbcDAO dao, Class<?> objectClass) {
	this.dao = dao;
	this.objectClass = objectClass;
	this.LOG = dao.getLogger();
    }

    protected abstract String getSQL();

    protected abstract void fillParameters(PreparedStatement stmnt) throws SQLException;

    public void execute() throws OperationException, DataConstraintException {
	PreparedStatement stmnt = null;
	boolean success = false;
	try {
	    String sqlString = getSQL();
	    stmnt = this.dao.getConnection().prepareStatement(sqlString);
	    fillParameters(stmnt);
	    LOG.debug("Executing SQL:" + sqlString);
	    doExecute(stmnt);
	    this.dao.markChangePending();
	    success = true;
	} catch (OperationException e) {
	    LOG.error("Error executing the query", e);
	    throw e;
	} catch (DataConstraintException e) {
	    // catching just to not be caught by the generic exception clause
	    LOG.error("Error executing the query", e);
	    throw e;
	} catch (SQLException e) {

	    // this exception could occur for syntax error in SQL, or other
	    // error in fillParameters

	    LOG.error("Error prepairing the query", e);
	    throw new OperationException(ErrorCodes.SQL_ERROR, e);
	} catch (Exception e) {

	    // for other error, e.g. null pointer access, etc
	    // during the data processing
	    LOG.error("Fatal error executing the query:", e);
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
    }

    protected void doExecute(PreparedStatement stmnt) throws DataConstraintException {
	try {
	    long start = System.currentTimeMillis();
	    stmnt.executeUpdate();
	    Statistic.getByName("db.update." + getSQL()).add(start);
	} catch (SQLException e) {
	    // this exception should be converted to our nice exceptions
	    LOG.error("Error executing the query", e);
	    throw DataConstraintException.createException(e, objectClass);
	}
    }

    protected Connection getConnection() throws OperationException {
	return dao.getConnection();
    }
}
