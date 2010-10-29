package ro.gagarin.jdbc;

import ro.gagarin.dao.UserExtraDAO;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.generictable.JdbcGenericTableDAO;
import ro.gagarin.jdbc.objects.DBUserExtraRecord;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserExtraRecord;

public class JdbcUserExtraDAO extends JdbcGenericTableDAO implements UserExtraDAO {

    public JdbcUserExtraDAO(Session session) throws OperationException {
	super(session, "UserExtra");
    }

    public UserExtraRecord getRecord(long id) throws OperationException {
	return new DBUserExtraRecord(super.getRecord(id));
    }

}
