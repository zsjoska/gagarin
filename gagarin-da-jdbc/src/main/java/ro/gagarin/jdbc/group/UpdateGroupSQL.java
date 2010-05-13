package ro.gagarin.jdbc.group;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.Group;
import ro.gagarin.utils.FieldValidator;

public class UpdateGroupSQL extends UpdateQuery {

    private final Group group;

    public UpdateGroupSQL(BaseJdbcDAO dao, Group group) {
	super(dao, Group.class);
	this.group = group;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// id is required for identification
	FieldValidator.requireLongField("id", group);

	if (group.getName() == null && group.getDescription() == null) {
	    throw new FieldRequiredException("name or description", Group.class);
	}

	if (group.getName() != null) {
	    FieldValidator.requireStringField("name", group, true);
	}
	if (group.getDescription() != null) {
	    FieldValidator.requireStringField("description", group, false);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	int index = 1;
	if (group.getName() != null) {
	    stmnt.setString(index, group.getName());
	    index++;
	}
	if (group.getDescription() != null) {
	    stmnt.setString(index, group.getDescription());
	    index++;
	}
	stmnt.setLong(index, group.getId());
    }

    @Override
    protected String getSQL() {
	return "UPDATE Groups SET "
		+ (group.getName() == null ? "" : " name = ?" + (group.getDescription() == null ? " " : ", "))
		+ (group.getDescription() == null ? "" : "description = ? ") + " WHERE id = ?";
    }

}
