package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.jdbc.objects.DBUserPermission;
import ro.gagarin.user.UserPermission;
import ro.gagarin.utils.FieldValidator;

public class GetEffectivePermissionsOnEntitySQL extends SelectQuery {

    private final ControlEntity entity;
    private final Owner[] owners;
    private Set<UserPermission> permissions;

    public GetEffectivePermissionsOnEntitySQL(BaseJdbcDAO dao, ControlEntity entity, Owner[] owners) {
	super(dao);
	this.entity = entity;
	this.owners = owners;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.permissions = new HashSet<UserPermission>();
	while (rs.next()) {
	    DBUserPermission permission = new DBUserPermission();
	    permission.setId(rs.getLong("id"));
	    permission.setPermissionName(rs.getString("permissionName"));
	    permissions.add(permission);
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", this.entity);
	for (Owner owner : owners) {
	    FieldValidator.requireLongField("id", owner);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.entity.getId());
	int index = 2;
	for (Owner owner : owners) {
	    stmnt.setLong(index++, owner.getId());
	}
    }

    @Override
    protected String getSQL() {
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT DISTINCT UserPermissions.id, UserPermissions.permissionName FROM RoleOwnerAssignment ");
	sb.append("INNER JOIN PermissionAssignment ON RoleOwnerAssignment.role_id = PermissionAssignment.role_id ");
	sb.append("INNER JOIN UserPermissions ON PermissionAssignment.perm_id = UserPermissions.id ");
	sb.append("WHERE RoleOwnerAssignment.object_id = ? AND (");
	for (int i = 0; i < owners.length; i++) {
	    sb.append(" RoleOwnerAssignment.owner_id = ? OR");
	}
	sb.delete(sb.length() - 2, sb.length());
	sb.append(")");
	return sb.toString();
    }

    public Set<UserPermission> getPermissions() {
	return this.permissions;
    }
}
