package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

public class DeletePermissionSQL extends UpdateQuery {

    private final UserPermission perm;

    public DeletePermissionSQL(BaseJdbcDAO dao, UserPermission perm) {
	super(dao);
	this.perm = perm;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, perm.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM UserPermissions WHERE id = ?";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", perm);
    }

}
