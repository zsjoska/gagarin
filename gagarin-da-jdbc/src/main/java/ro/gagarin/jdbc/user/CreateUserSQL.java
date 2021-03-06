/**
 * 
 */
package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.User;

/**
 * @author zsjoska
 * 
 */
public class CreateUserSQL extends UpdateQuery {

    private final User user;

    public CreateUserSQL(BaseJdbcDAO dao, User user) {
	super(dao, User.class);
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
	stmnt.setLong(7, user.getRole().getId());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO Users( id, username, name, email, phone, password, roleid) VALUES (?,?,?,?,?,?,?)";
    }

}
