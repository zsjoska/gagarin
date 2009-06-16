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

import org.apache.log4j.Logger;

import ro.gagarin.BaseDAO;
import ro.gagarin.ConfigurationManager;
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

	private static final transient Logger LOG = Logger.getLogger(BaseJdbcDAO.class);

	private static boolean DRIVER_LOADED;

	private boolean ourConnection = false;

	private Connection connection;

	private boolean rollback = false;

	public BaseJdbcDAO(Session session) {
		if (session == null) {
			CFG = null;
			APPLOG = null;
			throw new NullPointerException("attempt to initialize BaseJdbcDAO with null session");
		}

		CFG = session.getManagerFactory().getConfigurationManager(session);
		APPLOG = session.getManagerFactory().getLogManager(session);

		checkLoadDBDriver(CFG);

		synchronized (session) {
			session.setBusy(true);
			Object property = session.getProperty(BaseDAO.class);
			if (property instanceof BaseJdbcDAO) {

				Connection connection = ((BaseJdbcDAO) property).getConnection();
				this.connection = connection;
				this.ourConnection = false;
			} else {
				// null -- or wrong object
				if (property != null) {
					throw new RuntimeException("Wrong object type was found on session for "
							+ BaseJdbcDAO.class.getName() + "; found:"
							+ property.getClass().getName());
				}
				this.connection = createConnection(CFG);
				this.ourConnection = true;
				session.setProperty(BaseDAO.class, this);
				LOG.debug("Created Connection Instance " + connection.toString());
			}
		}
	}

	private Connection createConnection(ConfigurationManager cfgManager) {
		String url = cfgManager.getString(Config.JDBC_CONNECTION_URL);
		String user = cfgManager.getString(Config.JDBC_DB_USER);
		String password = cfgManager.getString(Config.JDBC_DB_PASSWORD);
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			return connection;
		} catch (SQLException e) {
			LOG.error("Could not get DB connection for url:" + url, e);
			throw new RuntimeException("Database is unavailable", e);
		}

	}

	protected Connection getConnection() {
		return this.connection;
	}

	private void checkLoadDBDriver(ConfigurationManager cfgManager) {
		if (DRIVER_LOADED)
			return;
		try {
			// Load the JDBC driver
			String driverName = cfgManager.getString(Config.JDBC_DB_DRIVER);
			LOG.info("Loading DB driver " + driverName);
			Class.forName(driverName);
			BaseJdbcDAO.DRIVER_LOADED = true;
		} catch (ClassNotFoundException e) {
			BaseJdbcDAO.DRIVER_LOADED = false;
			LOG.error("Could not load DB Driver class", e);
		}

	}

	public void release() throws OperationException {

		Connection tmpConn = this.connection;
		this.connection = null;

		if (this.ourConnection) {

			OperationException exception = null;
			if (!this.rollback) {
				LOG.debug("Committing connection " + tmpConn.toString());
				try {
					tmpConn.commit();
					tmpConn.close();
					LOG.debug("Released connection " + tmpConn.toString());
					return;
				} catch (SQLException e) {
					// this is the most relevant exception, so keep it then
					// throw it
					exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
					LOG.error("Exception on commit:", e);
				}
			}
			LOG.debug("Rollback connection " + tmpConn.toString());
			try {
				tmpConn.rollback();
			} catch (SQLException e) {
				if (exception == null)
					exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
				LOG.error("Exception on rollback:", e);
			}
			try {
				tmpConn.close();
			} catch (SQLException e) {
				if (exception == null)
					exception = new OperationException(ErrorCodes.DB_OP_ERROR, e);
				LOG.error("Exception on close:", e);
			}
			if (exception != null)
				throw exception;
		}

	}

	public void markRollback() {
		this.rollback = true;
	}

	public void checkCreateDependencies(ConfigurationManager cfgManager) throws OperationException {

		try {
			ArrayList<Triple<String, String, String>> parseDBSQLFile = parseDBSQLFile(cfgManager,
					Config.DB_INIT_SQL_FILE);
			for (Triple<String, String, String> tuple : parseDBSQLFile) {
				LOG.info("CHECK:" + tuple.s1);
				LOG.info("CREATE:" + tuple.s2);
			}

			for (Triple<String, String, String> t : parseDBSQLFile) {
				PreparedStatement prepareStatement;
				try {
					prepareStatement = getConnection().prepareStatement(t.s1);
					prepareStatement.execute();
					continue;
				} catch (SQLException e) {
					LOG.info("Test " + t.s3 + " failed; Creating...");
				}
				try {
					prepareStatement = getConnection().prepareStatement(t.s2);
					prepareStatement.execute();
					LOG.info(t.s3 + " Created.");
				} catch (SQLException e) {
					LOG.error(t.s3 + " not created.", e);
					throw e;
				}

			}
		} catch (SQLException e) {
			LOG.error("Unexpected exception during the table verification", e);
			throw new OperationException(ErrorCodes.SQL_ERROR, e);
		}
	}

	private ArrayList<Triple<String, String, String>> parseDBSQLFile(
			ConfigurationManager cfgManager, Config config) throws OperationException {

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
						throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, END
								+ " mark was not found.");
					}
					sb = new StringBuilder();
					tuple = new Triple<String, String, String>(null, null, line.substring(
							CHECK.length()).trim());
				} else if (line.startsWith(CREATE)) {
					if (tuple == null || sb == null) {
						throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, CHECK
								+ " mark was missing");
					}
					tuple.s1 = sb.toString();
					sb = new StringBuilder();
				} else if (line.startsWith(END)) {
					if (tuple == null || sb == null) {
						throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR, CREATE
								+ " mark was missing");
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
				throw new OperationException(ErrorCodes.FILE_SYNTAX_ERROR,
						"Unexpected content after " + END);
			}
		} catch (IOException e) {
			LOG.error("Exception reading file", e);
			throw new OperationException(ErrorCodes.ERROR_READING_FILE, e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// log, and ignore
				LOG.error("Exception on closing file stream for " + config.name(), e);
			}
		}

		return list;

	}
}
