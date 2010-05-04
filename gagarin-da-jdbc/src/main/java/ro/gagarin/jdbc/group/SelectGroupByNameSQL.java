package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBGroup;
import ro.gagarin.user.Group;

public class SelectGroupByNameSQL extends SelectQuery {

    private final String groupname;
    private DBGroup group;

    public SelectGroupByNameSQL(BaseJdbcDAO dao, String groupname) {
	super(dao, Group.class);
	this.groupname = groupname;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    group = new DBGroup();
	    group.setId(rs.getLong("id"));
	    group.setDescription(rs.getString("description"));
	    group.setName(rs.getString("name"));
	} else {
	    group = null;
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setString(1, this.groupname);
    }

    @Override
    protected String getSQL() {
	return "SELECT id, name, description FROM Groups where name = ?";
    }

    public static Group execute(BaseJdbcDAO dao, String groupname) throws OperationException {
	SelectGroupByNameSQL select = new SelectGroupByNameSQL(dao, groupname);
	select.execute();
	return select.group;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// TODO check input
    }

}
