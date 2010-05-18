package ro.gagarin.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import ro.gagarin.BaseDAO;
import ro.gagarin.ConfigurationManager;
import ro.gagarin.DAOManager;
import ro.gagarin.config.Config;
import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.utils.Triple;

public class BaseJdbcDAO implements BaseDAO {

    protected final AppLog APPLOG;
    protected final ConfigurationManager CFG;

    private static final String END = "--END";
    private static final String CREATE = "--CREATE:";
    private static final String CHECK = "--CHECK:";

    private static boolean DRIVER_LOADED;

    private boolean ourConnection = false;

    private Connection connection;

    private boolean rollback = false;
    private final Session session;
    private boolean changePending = false;
    private final DAOManager daoManager;

    public BaseJdbcDAO(Session session) throws OperationException {
	if (session == null) {
	    CFG = null;
	    APPLOG = null;
	    throw new NullPointerException("attempt to initialize BaseJdbcDAO with null session");
	}
	if (!session.isBusy()) {
	    throw new OperationException(ErrorCodes.ERROR_WILD_SESSION, "The session was not marked busy");
	}

	this.session = session;

	CFG = session.getManagerFactory().getConfigurationManager();
	APPLOG = session.getManagerFactory().getLogManager().getLoggingSession(session, getClass());
	daoManager = session.getManagerFactory().getDAOManager();

	checkLoadDBDriver(CFG);

	synchronized (session) {
	    Object property = session.getProperty(BaseDAO.class);
	    if (property instanceof BaseJdbcDAO) {

		Connection connection = ((BaseJdbcDAO) property).getConnection();
		this.connection = connection;
		this.ourConnection = false;
	    } else {
		// null -- or wrong object
		if (property != null) {
		    throw new RuntimeException("Wrong object type was found on session for "
			    + BaseJdbcDAO.class.getName() + "; found:" + property.getClass().getName());
		}
		this.connection = createConnection(CFG);
		this.ourConnection = true;
		session.setProperty(BaseDAO.class, this);
		APPLOG.debug("Created Connection Instance " + connection.toString());
	    }
	}
    }

    private Connection createConnection(ConfigurationManager cfgManager) {
	String url = cfgManager.getString(Config.JDBC_CONNECTION_URL);
	String user = cfgManager.getString(Config.JDBC_DB_USER);
	String password = cfgManager.getString(Config.JDBC_DB_PASSWORD);
	try {
	    // TODO: use a connection pool for the DB
	    Connection connection = DriverManager.getConnection(url, user, password);
	    connection.setAutoCommit(false);
	    return connection;
	} catch (SQLException e) {
	    APPLOG.error("Could not get DB connection for url:" + url, e);
	    throw new RuntimeException("Database is unavailable", e);
	}

    }

    protected Connection getConnection() throws OperationException {
	if (this.rollback) {
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, "The connection was marked to rollback");
	}
	if (this.connection == null) {
	    throw new OperationException(ErrorCodes.DB_OP_ERROR, "The DAO session was released.");
	}
	return this.connection;
    }

    private void checkLoadDBDriver(ConfigurationManager cfgManager) {
	if (DRIVER_LOADED)
	    return;
	try {
	    // Load the JDBC driver
	    String driverName = cfgManager.getString(Config.JDBC_DB_DRIVER);
	    APPLOG.info("Loading DB driver " + driverName);
	    Class.forName(driverName);
	    BaseJdbcDAO.DRIVER_LOADED = true;
	} catch (ClassNotFoundException e) {
	    BaseJdbcDAO.DRIVER_LOADED = false;
	    APPLOG.error("Could not load DB Driver class", e);
	}

    }

    public void release() throws OperationException {

	Connection tmpConn = this.connection;
	this.connection = null;

	if (!this.ourConnection) {
	    // not our connection, nothing to do
	    return;
	}

	OperationException exception = null;
	if (!this.rollback) {

	    if (!this.changePending) {
		try {
		    // TODO: dig more here why this commit is required:
		    // AppInit + gerAdminUser + sessionClose = exception
		    tmpConn.commit();
		    tmpConn.close();
		    APPLOG.debug("Released connection " + tmpConn.toString());
		    return;
		} catch (SQLException e) {
		    exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
		    APPLOG.error("Exception on connection close", e);
		}
	    } else {

		APPLOG.debug("Committing connection " + tmpConn.toString());
		try {
		    tmpConn.commit();
		    tmpConn.close();
		    APPLOG.debug("Released connection " + tmpConn.toString());
		    return;
		} catch (SQLException e) {
		    // this is the most relevant exception, so keep it then
		    // throw it
		    exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
		    APPLOG.error("Exception on commit:", e);
		}
	    }
	}
	APPLOG.debug("Rollback connection " + tmpConn.toString());
	try {
	    tmpConn.rollback();
	} catch (SQLException e) {
	    if (exception == null)
		exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
	    APPLOG.error("Exception on rollback:", e);
	}
	try {
	    tmpConn.close();
	} catch (SQLException e) {
	    if (exception == null)
		exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
	    APPLOG.error("Exception on close:", e);
	}
	if (exception != null)
	    throw exception;

    }

    public void markRollback() {
	APPLOG.error("Marking the DB Session to Rollback!");
	this.rollback = true;
    }

    public void checkCreateDependencies(ConfigurationManager cfgManager) throws OperationException {

	try {
	    ArrayList<Triple<String, String, String>> parseDBSQLFile = parseDBSQLFile(cfgManager,
		    Config.DB_INIT_SQL_FILE);
	    // for (Triple<String, String, String> tuple : parseDBSQLFile) {
	    // APPLOG.debug("CHECK:" + tuple.s1);
	    // APPLOG.debug("CREATE:" + tuple.s2);
	    // }

	    for (Triple<String, String, String> t : parseDBSQLFile) {
		PreparedStatement prepareStatement;
		try {
		    prepareStatement = getConnection().prepareStatement(t.s1);
		    prepareStatement.execute();
		    continue;
		} catch (SQLException e) {
		    APPLOG.info("Test " + t.s3 + " failed; Creating...");
		}
		try {
		    prepareStatement = getConnection().prepareStatement(t.s2);
		    prepareStatement.execute();
		    APPLOG.info(t.s3 + " Created.");
		} catch (SQLException e) {
		    APPLOG.error(t.s3 + " not created.", e);
		    throw e;
		}

	    }
	} catch (SQLException e) {
	    APPLOG.error("Unexpected exception during the table verification", e);
	    throw new OperationException(ErrorCodes.SQL_ERROR, e);
	}
    }

    private ArrayList<Triple<String, String, String>> parseDBSQLFile(ConfigurationManager cfgManager, Config config)
	    throws OperationException {

	ArrayList<Triple<String, String, String>> list = new ArrayList<Triple<String, String, String>>();

	InputStream is = cfgManager.getConfigFileStream(Config.DB_INIT_SQL_FILE);

	BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	String line = null;
	try {
	    StringBuilder sb = null;
	    Triple<String, String, String> tuple = null;
	    while ((line = reader.readLine()) != null) {

		if (line.startsWith(CHECK)) {
		    if (sb != null || tuple != null) {
			throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, END + " mark was not found.");
		    }
		    sb = new StringBuilder();
		    tuple = new Triple<String, String, String>(null, null, line.substring(CHECK.length()).trim());
		} else if (line.startsWith(CREATE)) {
		    if (tuple == null || sb == null) {
			throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, CHECK + " mark was missing");
		    }
		    tuple.s1 = sb.toString();
		    sb = new StringBuilder();
		} else if (line.startsWith(END)) {
		    if (tuple == null || sb == null) {
			throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, CREATE + " mark was missing");
		    }
		    tuple.s2 = sb.toString();
		    list.add(tuple);
		    sb = null;
		    tuple = null;
		} else {
		    if (line.trim().length() == 0)
			continue;
		    if (line.startsWith("--"))
			continue;
		    sb.append(line);
		    sb.append("\r\n");
		}
	    }
	    if (sb != null || tuple != null) {
		throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, "Unexpected content after " + END);
	    }
	} catch (IOException e) {
	    APPLOG.error("Exception reading file", e);
	    throw new OperationException(ErrorCodes.ERROR_READING_FILE, e);
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		// log, and ignore
		APPLOG.error("Exception on closing file stream for " + config.name(), e);
	    }
	}

	return list;

    }

    public Session getSession() {
	return session;
    }

    public AppLog getLogger() {
	return APPLOG;
    }

    public void markChangePending() {
	this.changePending = true;
    }

    protected DAOManager getDaoManager() {
	return daoManager;
    }
}
