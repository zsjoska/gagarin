package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.user.UserRole;

public class SelectRolesSQL extends SelectQuery {

    private List<UserRole> roles = null;

    public SelectRolesSQL(BaseJdbcDAO dao) {
	super(dao, UserRole.class);
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	roles = new ArrayList<UserRole>();
	while (rs.next()) {
	    DBUserRole role = new DBUserRole();
	    role.setId(rs.getLong("id"));
	    role.setRoleName(rs.getString("roleName"));
	    roles.add(role);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
    }

    @Override
    protected String getSQL() {
	return "SELECT id, roleName FROM UserRoles";
    }

    public static List<UserRole> execute(BaseJdbcDAO dao) throws OperationException {
	SelectRolesSQL q = new SelectRolesSQL(dao);
	q.execute();
	return q.roles;
    }
}
