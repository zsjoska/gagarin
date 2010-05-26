/**
 * 
 */
package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

/**
 * @author zsjoska
 * 
 */
public class CreateRoleSQL extends UpdateQuery {

    private final UserRole role;

    public CreateRoleSQL(BaseJdbcDAO dao, UserRole role) {
	super(dao);
	this.role = role;

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, role.getId());
	stmnt.setString(2, role.getRoleName());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO UserRoles( id, roleName) VALUES (?,?)";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", role);
	FieldValidator.requireStringField("roleName", role, true);
    }

}
