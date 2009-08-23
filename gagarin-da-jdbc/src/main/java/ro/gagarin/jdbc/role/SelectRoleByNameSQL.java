package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.user.UserRole;

public class SelectRoleByNameSQL extends SelectQuery {

    private final String roleName;
    private DBUserRole role = null;

    public SelectRoleByNameSQL(BaseJdbcDAO dao, String roleName) {
	super(dao, UserRole.class);
	this.roleName = roleName;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    role = new DBUserRole();
	    role.setId(rs.getLong("id"));
	    role.setRoleName(rs.getString("roleName"));
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setString(1, roleName);
    }

    @Override
    protected String getSQL() {
	return "SELECT id, roleName FROM UserRoles WHERE roleName = ?";
    }

    public static UserRole execute(BaseJdbcDAO dao, String roleName) throws OperationException {
	SelectRoleByNameSQL q = new SelectRoleByNameSQL(dao, roleName);
	q.execute();
	return q.role;
    }

}
