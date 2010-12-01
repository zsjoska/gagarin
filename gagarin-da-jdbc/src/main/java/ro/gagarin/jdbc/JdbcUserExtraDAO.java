package ro.gagarin.jdbc;

import ro.gagarin.dao.UserExtraDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.generictable.JdbcGenericTableDAO;
import ro.gagarin.jdbc.objects.DBUserExtraRecord;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserExtraRecord;

public class JdbcUserExtraDAO extends JdbcGenericTableDAO implements UserExtraDAO {

    private static final String USERS_EXTRA_TABLE_NAME = "UsersExtra";

    public JdbcUserExtraDAO(Session session) throws OperationException {
	super(session, USERS_EXTRA_TABLE_NAME);
    }

    public UserExtraRecord getRecord(long id) throws OperationException {
	GenericRecord record = super.getRecord(id);
	if (record == null) {
	    return null;
	}
	return new DBUserExtraRecord(record);
    }

}