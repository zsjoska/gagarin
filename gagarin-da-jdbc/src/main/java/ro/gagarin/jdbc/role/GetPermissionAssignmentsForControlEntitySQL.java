package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.ControlEntity;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBOwner;
import ro.gagarin.jdbc.objects.DBPermOwnerCEAssignment;
import ro.gagarin.jdbc.objects.DBUserRole;
import ro.gagarin.user.PermOwnerCEAssignment;
import ro.gagarin.utils.FieldValidator;

public class GetPermissionAssignmentsForControlEntitySQL extends SelectQuery {

    private List<PermOwnerCEAssignment> assignments;
    private final ControlEntity ce;

    public GetPermissionAssignmentsForControlEntitySQL(BaseJdbcDAO dao, ControlEntity ce) {
	super(dao);
	this.ce = ce;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.assignments = new ArrayList<PermOwnerCEAssignment>();
	while (rs.next()) {

	    Long owner_id = rs.getLong("owner_id");
	    Long role_id = rs.getLong("id");
	    String role_name = rs.getString("roleName");
	    DBPermOwnerCEAssignment assignment = new DBPermOwnerCEAssignment();
	    DBUserRole role = new DBUserRole();
	    role.setRoleName(role_name);
	    role.setId(role_id);
	    DBOwner owner = new DBOwner(owner_id);
	    assignment.setRole(role);
	    assignment.setOwner(owner);
	    assignment.setControlEntity(this.ce);
	    this.assignments.add(assignment);
	}

    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireIdField(this.ce);
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.ce.getId());
    }

    @Override
    protected String getSQL() {
	return "SELECT UserRoles.id, UserRoles.roleName, owner_id "
		+ "FROM RoleOwnerAssignment INNER JOIN UserRoles on role_id = UserRoles.id WHERE object_id = ?";
    }

    public static List<PermOwnerCEAssignment> execute(BaseJdbcDAO dao, ControlEntity ce) throws OperationException {
	GetPermissionAssignmentsForControlEntitySQL sql = new GetPermissionAssignmentsForControlEntitySQL(dao, ce);
	sql.execute();
	return sql.assignments;
    }

}
