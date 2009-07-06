package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class GetUsersWithRoleSQL extends SelectQuery {

	private final UserRole role;
	private ArrayList<User> users = null;

	public GetUsersWithRoleSQL(BaseJdbcDAO dao, UserRole role) {
		super(dao, UserRole.class);
		this.role = role;
	}

	@Override
	protected void useResult(ResultSet rs) throws SQLException {
		this.users = new ArrayList<User>();
		while (rs.next()) {
			DBUser user = new DBUser();
			user.setId(rs.getLong("id"));
			user.setName(rs.getString("name"));
			user.setUsername(rs.getString("userName"));
			user.setRole(role);
			users.add(user);
		}
	}

	@Override
	protected void fillParameters(PreparedStatement stmnt) throws SQLException {
		stmnt.setLong(1, role.getId());
	}

	@Override
	protected String getSQL() {
		return "SELECT id, name, userName, roleid FROM Users WHERE roleid = ?";
	}

	public static ArrayList<User> execute(BaseJdbcDAO dao, UserRole role)
			throws OperationException, DataConstraintException {
		GetUsersWithRoleSQL query = new GetUsersWithRoleSQL(dao, role);
		query.execute();

		return query.users;
	}
}
