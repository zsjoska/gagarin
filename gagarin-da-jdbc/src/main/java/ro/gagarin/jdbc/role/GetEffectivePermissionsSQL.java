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
import ro.gagarin.Owner;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

public class GetEffectivePermissionsSQL extends SelectQuery {

    private final Owner[] owners;
    private Map<ControlEntity, Set<UserPermission>> permissions;

    public GetEffectivePermissionsSQL(BaseJdbcDAO dao, Owner[] owners) {
	super(dao);
	this.owners = owners;
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
	for (Owner owner : owners) {
	    FieldValidator.requireLongField("id", owner);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	int index = 1;
	for (Owner owner : owners) {
	    stmnt.setLong(index++, owner.getId());
	}
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT DISTINCT object_id, object_type, id, permissionName FROM RoleOwnerAssignment ");
	sb.append("INNER JOIN PermissionAssignment ON RoleOwnerAssignment.role_id = PermissionAssignment.role_id ");
	sb.append("INNER JOIN UserPermissions ON PermissionAssignment.perm_id = UserPermissions.id ");
	sb.append("WHERE ");
	for (int i = 0; i < owners.length; i++) {
	    sb.append(" RoleOwnerAssignment.owner_id = ? OR");
	}
	sb.delete(sb.length() - 2, sb.length());
	return sb.toString();
    }

    public Map<ControlEntity, Set<UserPermission>> getPermissions() {
	return this.permissions;
    }

    public static Map<ControlEntity, Set<UserPermission>> execute(BaseJdbcDAO dao, Owner[] owners)
	    throws OperationException {
	GetEffectivePermissionsSQL cmd = new GetEffectivePermissionsSQL(dao, owners);
	cmd.execute();
	return cmd.getPermissions();
    }
}
