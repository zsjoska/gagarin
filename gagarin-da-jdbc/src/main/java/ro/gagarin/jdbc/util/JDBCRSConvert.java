package ro.gagarin.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.jdbc.objects.DBUserRole;

public class JDBCRSConvert {

    public static DBUser convertRSToUser(ResultSet rs) throws SQLException {
	DBUser user = new DBUser();
	user.setId(rs.getLong("id"));
	user.setName(rs.getString("name"));
	user.setUsername(rs.getString("userName"));
	user.setEmail(rs.getString("email"));
	user.setPhone(rs.getString("phone"));
	return user;
    }

    public static DBUser convertRSToUserWithRole(ResultSet rs) throws SQLException {
	DBUser user = convertRSToUser(rs);
	DBUserRole role = new DBUserRole();
	role.setId(rs.getLong("roleid"));
	role.setRoleName(rs.getString("roleName"));
	user.setRole(role);
	return user;
    }

}
