package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.util.JDBCRSConvert;
import ro.gagarin.user.User;

public class SelectUsersSQL extends SelectQuery {

    private ArrayList<User> users = null;

    public SelectUsersSQL(BaseJdbcDAO dao) {
	super(dao, User.class);
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.users = new ArrayList<User>();
	while (rs.next()) {
	    DBUser user = JDBCRSConvert.convertRSToUserWithRole(rs);
	    users.add(user);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
    }

    @Override
    protected String getSQL() {
	return "SELECT Users.id, username, name, email, phone, password, roleid, roleName "
		+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id";
    }

    public static ArrayList<User> execute(BaseJdbcDAO dao) throws OperationException {
	SelectUsersSQL q = new SelectUsersSQL(dao);
	q.execute();
	return q.users;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// no input
    }

}
