package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserPermission;

public class CreatePermissionSQL extends UpdateQuery {

    private final UserPermission perm;

    public CreatePermissionSQL(BaseJdbcDAO dao, UserPermission perm) {
	super(dao, UserPermission.class);
	this.perm = perm;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, perm.getId());
	stmnt.setString(2, perm.getPermissionName());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO UserPermissions( id, permissionName) VALUES (?,?)";
    }

}
