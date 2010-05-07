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
import ro.gagarin.jdbc.objects.DBGroup;
import ro.gagarin.user.Group;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

public class GetUserGroupsSQL extends SelectQuery {

    private final User user;
    private ArrayList<Group> groups = null;

    public GetUserGroupsSQL(BaseJdbcDAO dao, User user) {
	super(dao, User.class);
	this.user = user;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.groups = new ArrayList<Group>();
	while (rs.next()) {
	    DBGroup group = new DBGroup();
	    group.setId(rs.getLong("id"));
	    group.setDescription(rs.getString("description"));
	    group.setName(rs.getString("name"));
	    this.groups.add(group);
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", user);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, user.getId());
    }

    @Override
    protected String getSQL() {
	return "SELECT id, name, description FROM Groups INNER JOIN UserGroupAssignment ON Groups.id = UserGroupAssignment.group_id "
		+ "WHERE UserGroupAssignment.user_id = ?";
    }

    public static List<Group> execute(BaseJdbcDAO dao, User usr) throws OperationException {
	GetUserGroupsSQL getUserGroupsSQL = new GetUserGroupsSQL(dao, usr);
	getUserGroupsSQL.execute();
	return getUserGroupsSQL.getGroups();
    }

    private List<Group> getGroups() {
	return this.groups;
    }

}
