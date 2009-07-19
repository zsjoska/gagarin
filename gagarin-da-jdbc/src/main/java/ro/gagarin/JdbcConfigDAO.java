package ro.gagarin;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.session.Session;

public class JdbcConfigDAO extends BaseJdbcDAO {

	private final ConfigurationManager localCfgMgr;

	public JdbcConfigDAO(Session session, ConfigurationManager localCfgMgr)
			throws OperationException {
		super(session);
		this.localCfgMgr = localCfgMgr;
	}
}
