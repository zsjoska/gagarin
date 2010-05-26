/**
 * 
 */
package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.util.JDBCRSConvert;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

/**
 * @author zsido
 * 
 */
public class SelectUserByUsernameSQL extends SelectQuery {

    private DBUser user;
    private final String username;

    public SelectUserByUsernameSQL(BaseJdbcDAO dao, String username) {
	super(dao, User.class);
	this.username = username;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    user = JDBCRSConvert.convertRSToUser(rs);
	} else {
	    user = null;
	}

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setString(1, username);

    }

    @Override
    protected String getSQL() {
	return "SELECT Users.id, username, name, email, phone, password, status, authentication, created "
		+ "FROM Users WHERE username = ?";
    }

    public static User execute(BaseJdbcDAO dao, String username) throws OperationException {
	SelectUserByUsernameSQL select = new SelectUserByUsernameSQL(dao, username);
	select.execute();
	return select.user;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireStringValue(this.username, "username", 50);
    }
}
