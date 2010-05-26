package ro.gagarin.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.jdbc.objects.DBUser;
import ro.gagarin.user.AuthenticationType;
import ro.gagarin.user.UserStatus;

public class JDBCRSConvert {

    public static DBUser convertRSToUser(ResultSet rs) throws SQLException {
	DBUser user = new DBUser();
	user.setId(rs.getLong("id"));
	user.setName(rs.getString("name"));
	user.setUsername(rs.getString("userName"));
	user.setEmail(rs.getString("email"));
	user.setPhone(rs.getString("phone"));
	user.setCreated(rs.getLong("created"));
	user.setAuthentication(convertToAuthenticationType(rs.getString("authentication")));
	user.setStatus(convertToStatus(rs.getInt("status")));
	user.setPhone(rs.getString("phone"));
	return user;
    }

    private static UserStatus convertToStatus(int ordinal) {
	return UserStatus.values()[ordinal];
    }

    private static AuthenticationType convertToAuthenticationType(String string) {
	return AuthenticationType.valueOf(string);
    }
}
