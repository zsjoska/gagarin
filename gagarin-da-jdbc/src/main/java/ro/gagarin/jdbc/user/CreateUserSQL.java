/**
 * 
 */
package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

/**
 * @author zsjoska
 * 
 */
public class CreateUserSQL extends UpdateQuery {

    private final User user;

    public CreateUserSQL(BaseJdbcDAO dao, User user) {
	super(dao);
	this.user = user;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, user.getId());
	stmnt.setString(2, user.getUsername());
	stmnt.setString(3, user.getName());
	stmnt.setString(4, user.getEmail());
	stmnt.setString(5, user.getPhone());
	stmnt.setString(6, user.getPassword());
	stmnt.setLong(7, user.getStatus().ordinal());
	stmnt.setLong(8, user.getCreated());
	stmnt.setString(9, user.getAuthentication());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO Users( id, username, name, email, phone, password, status, created, authentication) "
		+ "VALUES (?,?,?,?,?,?,?,?,?)";

    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireStringField("username", user, true);
	FieldValidator.requireField("status", user);
	FieldValidator.requireField("authentication", user);
	FieldValidator.requireField("created", user);
    }

}
