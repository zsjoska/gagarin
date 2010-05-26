package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

// TODO:(1) delete Permission assignments
public class DeleteRoleSQL extends UpdateQuery {

    private final UserRole role;

    public DeleteRoleSQL(BaseJdbcDAO dao, UserRole role) {
	super(dao, UserRole.class);
	this.role = role;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, role.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM UserRoles WHERE id = ?";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", role);
    }

}
