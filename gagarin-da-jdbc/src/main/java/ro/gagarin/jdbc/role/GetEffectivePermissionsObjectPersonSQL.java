package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.Person;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;

public class GetEffectivePermissionsObjectPersonSQL extends SelectQuery {

    private Set<PermissionEnum> permissions;
    private final ControlEntity ce;
    private final Person person;

    public GetEffectivePermissionsObjectPersonSQL(BaseJdbcDAO dao, ControlEntity ce, Person person) {
	super(dao);
	this.ce = ce;
	this.person = person;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.permissions = new HashSet<PermissionEnum>();
	while (rs.next()) {
	    String permName = rs.getString("PermissionName");
	    // TODO:(2) Must protect from inexistent enum value
	    this.permissions.add(PermissionEnum.valueOf(permName));
	}
    }

    @Override
    protected String getSQL() {
	return "SELECT PermissionName FROM RolePersonAssignment "
		+ "INNER JOIN PermissionAssignment ON PermissionAssignment.role_id = RolePersonAssignment.role_id "
		+ "INNER JOIN UserPermissions ON PermissionAssignment.perm_id = UserPermissions.id "
		+ "WHERE RolePersonAssignment.person_id = ? AND RolePersonAssignment.object_id = ?";
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.person.getId());
	stmnt.setLong(2, this.ce.getId());
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireIdField(ce);
	FieldValidator.requireIdField(person);
    }

    public static Set<PermissionEnum> execute(BaseJdbcDAO dao, ControlEntity ce, Person person)
	    throws OperationException {
	GetEffectivePermissionsObjectPersonSQL sql = new GetEffectivePermissionsObjectPersonSQL(dao, ce, person);
	sql.execute();
	return sql.permissions;
    }
}
