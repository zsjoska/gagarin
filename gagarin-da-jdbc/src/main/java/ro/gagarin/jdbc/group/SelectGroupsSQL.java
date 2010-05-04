package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBGroup;
import ro.gagarin.user.Group;

public class SelectGroupsSQL extends SelectQuery {

    private ArrayList<Group> groups;

    public SelectGroupsSQL(BaseJdbcDAO dao) {
	super(dao, Group.class);
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
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
    }

    @Override
    protected String getSQL() {
	return "SELECT id, name, description FROM Groups";
    }

    public static ArrayList<Group> execute(BaseJdbcDAO dao) throws OperationException {
	SelectGroupsSQL select = new SelectGroupsSQL(dao);
	select.execute();
	return select.groups;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// no input
    }
}
