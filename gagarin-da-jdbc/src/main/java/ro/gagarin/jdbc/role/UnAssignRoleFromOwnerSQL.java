package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.user.UserRole;
import ro.gagarin.utils.FieldValidator;

public class UnAssignRoleFromOwnerSQL extends UpdateQuery {

    private final UserRole role;
    private final Owner owner;
    private final ControlEntity object;

    public UnAssignRoleFromOwnerSQL(BaseJdbcDAO dao, UserRole role, Owner owner, ControlEntity object) {
	super(dao);
	this.role = role;
	this.owner = owner;
	this.object = object;
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", this.role);
	FieldValidator.requireLongField("id", this.owner);
	FieldValidator.requireLongField("id", this.object);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.role.getId());
	stmnt.setLong(2, this.owner.getId());
	stmnt.setLong(3, this.object.getId());
    }

    @Override
    protected String getSQL() {
	return "DELETE FROM RoleOwnerAssignment WHERE role_id = ? AND owner_id = ? AND object_id = ?";
    }
}
