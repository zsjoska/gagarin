package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.JdbcRoleDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class UpdateRoleSQL extends UpdateQuery {

    private final UserRole role;

    public UpdateRoleSQL(JdbcRoleDAO jdbcRoleDAO, UserRole role) {
	super(jdbcRoleDAO);
	this.role = role;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// id is required for identification
	FieldValidator.requireIdField(role);

	// TODO:(2) check that at least one field is not null
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	int index = 1;
	if (role.getRoleName() != null) {
	    stmnt.setString(index++, role.getRoleName());
	}
	stmnt.setLong(index, role.getId());
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("UPDATE UserRoles SET ");
	if (role.getRoleName() != null) {
	    sb.append(" roleName = ?,");
	}
	sb.deleteCharAt(sb.length() - 1);
	sb.append(" WHERE id = ?");

	return sb.toString();
    }
}
