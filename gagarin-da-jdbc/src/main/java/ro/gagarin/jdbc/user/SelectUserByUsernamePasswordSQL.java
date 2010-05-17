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
public class SelectUserByUsernamePasswordSQL extends SelectQuery {

    private DBUser user;
    private String username;
    private final String password;

    public SelectUserByUsernamePasswordSQL(BaseJdbcDAO dao, String username, String password) {
	super(dao, User.class);
	this.username = username;
	this.password = password;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    user = JDBCRSConvert.convertRSToUserWithRole(rs);
	} else {
	    user = null;
	}

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setString(1, username);
	stmnt.setString(2, password);

    }

    @Override
    protected String getSQL() {
	return "SELECT Users.id, username, name, password, email, phone, roleid, roleName "
		+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id "
		+ "WHERE username = ? and password = ?";
    }

    public static User execute(BaseJdbcDAO dao, String username, String password) throws OperationException {

	SelectUserByUsernamePasswordSQL select = new SelectUserByUsernamePasswordSQL(dao, username, password);
	select.execute();
	return select.user;

    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	this.username = FieldValidator.checkStringValue(username, "username", 50);
	FieldValidator.checkStringValue(password, "password", 50, false);
    }

}
