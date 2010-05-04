package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;

public class SelectPermissionsSQL extends SelectQuery {

    private List<UserPermission> permissions = null;

    public SelectPermissionsSQL(BaseJdbcDAO dao) {
	super(dao, UserPermission.class);
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.permissions = new ArrayList<UserPermission>();
	while (rs.next()) {
	    DBUserPermission permission = new DBUserPermission();
	    permission.setId(rs.getLong("id"));
	    permission.setPermissionName(rs.getString("permissionName"));
	    permissions.add(permission);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
    }

    @Override
    protected String getSQL() {
	return "SELECT id, permissionName FROM UserPermissions";
    }

    public static List<UserPermission> execute(BaseJdbcDAO dao) throws OperationException {
	SelectPermissionsSQL sql = new SelectPermissionsSQL(dao);
	sql.execute();
	return sql.permissions;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// no input
    }

}
