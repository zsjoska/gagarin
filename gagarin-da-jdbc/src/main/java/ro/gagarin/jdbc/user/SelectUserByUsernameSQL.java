/**
 * 
 */
package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.user.User;

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
			user = new DBUser();
			user.setId(rs.getLong("id"));
			user.setUsername(rs.getString("username"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setPhone(rs.getString("phone"));
			DBUserRole role = new DBUserRole();
			role.setId(rs.getLong("roleid"));
			role.setRoleName(rs.getString("roleName"));
			user.setRole(role);
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
		return "SELECT Users.id, username, name, email, phone, password, roleid, roleName "
				+ "FROM Users INNER JOIN UserRoles ON Users.roleid = UserRoles.id "
				+ "WHERE username = ?";
	}

	public static User execute(BaseJdbcDAO dao, String username) throws OperationException {
		SelectUserByUsernameSQL select = new SelectUserByUsernameSQL(dao, username);
		select.execute();
		return select.user;
	}
}
