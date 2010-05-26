package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.Person;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

public class GetEffectivePermissionsSQL extends SelectQuery {

    private final Person[] persons;
    private Map<ControlEntity, Set<UserPermission>> permissions;

    public GetEffectivePermissionsSQL(BaseJdbcDAO dao, Person[] persons) {
	super(dao);
	this.persons = persons;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.permissions = new HashMap<ControlEntity, Set<UserPermission>>();
	while (rs.next()) {

	    long objectId = rs.getLong("object_id");
	    ControlEntityCategory cat = ControlEntityCategory.valueOf(rs.getString("object_type"));
	    BaseControlEntity controlEntity = new BaseControlEntity(cat);
	    controlEntity.setId(objectId);

	    Set<UserPermission> set = permissions.get(controlEntity);
	    if (set == null) {
		set = new HashSet<UserPermission>();
		permissions.put(controlEntity, set);
	    }
	    DBUserPermission permission = new DBUserPermission();
	    permission.setId(rs.getLong("id"));
	    permission.setPermissionName(rs.getString("permissionName"));
	    set.add(permission);
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	for (Person person : persons) {
	    FieldValidator.requireLongField("id", person);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	int index = 1;
	for (Person person : persons) {
	    stmnt.setLong(index++, person.getId());
	}
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT DISTINCT object_id, object_type, id, permissionName FROM RolePersonAssignment ");
	sb.append("INNER JOIN PermissionAssignment ON RolePersonAssignment.role_id = PermissionAssignment.role_id ");
	sb.append("INNER JOIN UserPermissions ON PermissionAssignment.perm_id = UserPermissions.id ");
	sb.append("WHERE ");
	for (int i = 0; i < persons.length; i++) {
	    sb.append(" RolePersonAssignment.person_id = ? OR");
	}
	sb.delete(sb.length() - 2, sb.length());
	return sb.toString();
    }

    public Map<ControlEntity, Set<UserPermission>> getPermissions() {
	return this.permissions;
    }

    public static Map<ControlEntity, Set<UserPermission>> execute(BaseJdbcDAO dao, Person[] persons)
	    throws OperationException {
	GetEffectivePermissionsSQL cmd = new GetEffectivePermissionsSQL(dao, persons);
	cmd.execute();
	return cmd.getPermissions();
    }
}
