package ro.gagarin.jdbc.group;

import java.security.acl.Group;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.jdbc.objects.DBGroup;
import ro.gagarin.utils.FieldValidator;

public class CreateGroupSQL extends UpdateQuery {

    private final DBGroup dbGroup;

    public CreateGroupSQL(BaseJdbcDAO dao, DBGroup dbGroup) {
	super(dao, Group.class);
	this.dbGroup = dbGroup;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, dbGroup.getId());
	stmnt.setString(2, dbGroup.getName());
	stmnt.setString(3, dbGroup.getDescription());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO Groups( id, name, description) VALUES (?,?,?)";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", dbGroup);
	FieldValidator.requireStringField("name", dbGroup, true);

    }

}
