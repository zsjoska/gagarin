package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.Person;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

public class GetEffectivePermissionsSQL extends SelectQuery {

    private final ControlEntity entity;
    private final Person[] persons;
    private Set<UserPermission> permissions;

    public GetEffectivePermissionsSQL(BaseJdbcDAO dao, ControlEntity entity, Person[] persons) {
	super(dao, ControlEntity.class);
	this.entity = entity;
	this.persons = persons;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.permissions = new HashSet<UserPermission>();
	while (rs.next()) {
	    DBUserPermission permission = new DBUserPermission();
	    permission.setId(rs.getLong("id"));
	    permission.setPermissionName(rs.getString("permissionName"));
	    System.err.println(permission);
	    permissions.add(permission);
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", this.entity);
	for (Person person : persons) {
	    FieldValidator.requireLongField("id", person);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.entity.getId());
	int index = 2;
	for (Person person : persons) {
	    stmnt.setLong(index, person.getId());
	}
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT UserPermissions.id, UserPermissions.permissionName FROM RolePersonAssignment ");
	sb.append("INNER JOIN PermissionAssignment ON RolePersonAssignment.role_id = PermissionAssignment.role_id ");
	sb.append("INNER JOIN UserPermissions ON PermissionAssignment.perm_id = UserPermissions.id ");
	sb.append("WHERE RolePersonAssignment.object_id = ? AND (");
	for (Person person : persons) {
	    sb.append(" RolePersonAssignment.person_id = ? OR");
	}
	sb.delete(sb.length() - 2, sb.length());
	sb.append(")");
	return sb.toString();
	// return
	// "INSERT INTO RolePersonAssignment (role_id, person_id, objectType, object_id) VALUES ( ?, ?, ?, ?)";
    }

    public Set<UserPermission> getPermissions() {
	return this.permissions;
    }

}
