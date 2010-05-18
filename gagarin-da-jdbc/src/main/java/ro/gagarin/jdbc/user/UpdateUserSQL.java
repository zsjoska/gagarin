package ro.gagarin.jdbc.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.User;
import ro.gagarin.utils.FieldValidator;

public class UpdateUserSQL extends UpdateQuery {

    private final User user;

    public UpdateUserSQL(BaseJdbcDAO dao, User user) {
	super(dao, User.class);
	this.user = user;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// id is required for identification
	FieldValidator.requireLongField("id", user);

	// TODO: check that at least one field is not null
	// TODO: check some key fields if they are empty

    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	int index = 1;
	if (user.getUsername() != null) {
	    stmnt.setString(index++, user.getUsername());
	}
	if (user.getName() != null) {
	    stmnt.setString(index++, user.getName());
	}
	if (user.getEmail() != null) {
	    stmnt.setString(index++, user.getEmail());
	}
	if (user.getPassword() != null) {
	    stmnt.setString(index++, user.getPassword());
	}
	if (user.getPhone() != null) {
	    stmnt.setString(index++, user.getPhone());
	}
	if (user.getRole() != null) {
	    stmnt.setLong(index++, user.getRole().getId());
	}
	if (user.getAuthentication() != null) {
	    stmnt.setString(index++, user.getAuthentication().name());
	}
	if (user.getStatus() != null) {
	    stmnt.setInt(index++, user.getStatus().ordinal());
	}

	stmnt.setLong(index, user.getId());
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("UPDATE Users SET ");
	if (user.getUsername() != null) {
	    sb.append(" username = ?,");
	}
	if (user.getName() != null) {
	    sb.append(" name = ?,");
	}
	if (user.getEmail() != null) {
	    sb.append(" email = ?,");
	}
	if (user.getPassword() != null) {
	    sb.append(" password = ?,");
	}
	if (user.getPhone() != null) {
	    sb.append(" phone = ?,");
	}
	if (user.getRole() != null) {
	    sb.append(" roleid = ?,");
	}
	if (user.getAuthentication() != null) {
	    sb.append(" authentication = ?,");
	}
	if (user.getStatus() != null) {
	    sb.append(" status = ?,");
	}
	sb.deleteCharAt(sb.length() - 1);
	sb.append(" WHERE id = ?");

	return sb.toString();
    }
}
