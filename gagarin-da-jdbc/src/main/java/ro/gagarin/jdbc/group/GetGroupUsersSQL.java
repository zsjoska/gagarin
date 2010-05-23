package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.util.JDBCRSConvert;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

public class GetGroupUsersSQL extends SelectQuery {

    private final Group group;
    private ArrayList<User> users;

    public GetGroupUsersSQL(BaseJdbcDAO dao, Group group) {
	super(dao, Group.class);
	this.group = group;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.users = new ArrayList<User>();
	while (rs.next()) {
	    DBUser user = JDBCRSConvert.convertRSToUser(rs);
	    users.add(user);
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", group);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, group.getId());
    }

    @Override
    protected String getSQL() {
	return "SELECT Users.id, username, name, email, phone, password, authentication, status, created "
		+ "FROM Users INNER JOIN UserGroupAssignment ON Users.id = UserGroupAssignment.user_id "
		+ "WHERE UserGroupAssignment.group_id = ?";
    }

    public static List<User> execute(BaseJdbcDAO dao, Group group) throws OperationException {

	GetGroupUsersSQL getGroupUsersSQL = new GetGroupUsersSQL(dao, group);
	getGroupUsersSQL.execute();
	return getGroupUsersSQL.getUsers();
    }

    private List<User> getUsers() {
	return this.users;
    }
}
