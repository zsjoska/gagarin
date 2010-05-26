package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

public class SelectPermissionByNameSQL extends SelectQuery {

    private DBUserPermission permission;
    private final String permissionName;

    public SelectPermissionByNameSQL(BaseJdbcDAO dao, String permissionName) {
	super(dao, UserPermission.class);
	this.permissionName = permissionName;
    }

    public static UserPermission execute(BaseJdbcDAO dao, String permissionName) throws OperationException {
	SelectPermissionByNameSQL q = new SelectPermissionByNameSQL(dao, permissionName);
	q.execute();
	return q.permission;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    this.permission = new DBUserPermission();
	    permission.setId(rs.getLong("id"));
	    permission.setPermissionName(rs.getString("permissionName"));
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setString(1, permissionName);
    }

    @Override
    protected String getSQL() {
	return "SELECT id, permissionName FROM UserPermissions WHERE permissionName = ?";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireStringValue(this.permissionName, "permissionName", 50);
    }
}
