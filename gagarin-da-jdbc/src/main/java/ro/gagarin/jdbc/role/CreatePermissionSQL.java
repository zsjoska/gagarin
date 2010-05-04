package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

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

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", perm);
	FieldValidator.requireStringField("permissionName", perm, true);
    }

}
