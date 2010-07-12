package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ro.gagarin.ControlEntity;
import ro.gagarin.Owner;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.user.PermissionEnum;
import ro.gagarin.utils.FieldValidator;

public class GetEffectivePermissionsObjectOwnerSQL extends SelectQuery {

    private Set<PermissionEnum> permissions;
    private final ControlEntity ce;
    private final Owner owner;

    public GetEffectivePermissionsObjectOwnerSQL(BaseJdbcDAO dao, ControlEntity ce, Owner owner) {
	super(dao);
	this.ce = ce;
	this.owner = owner;
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
	return "SELECT PermissionName FROM RoleOwnerAssignment "
		+ "INNER JOIN PermissionAssignment ON PermissionAssignment.role_id = RoleOwnerAssignment.role_id "
		+ "INNER JOIN UserPermissions ON PermissionAssignment.perm_id = UserPermissions.id "
		+ "WHERE RoleOwnerAssignment.owner_id = ? AND RoleOwnerAssignment.object_id = ?";
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.owner.getId());
	stmnt.setLong(2, this.ce.getId());
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireIdField(ce);
	FieldValidator.requireIdField(owner);
    }

    public static Set<PermissionEnum> execute(BaseJdbcDAO dao, ControlEntity ce, Owner owner)
	    throws OperationException {
	GetEffectivePermissionsObjectOwnerSQL sql = new GetEffectivePermissionsObjectOwnerSQL(dao, ce, owner);
	sql.execute();
	return sql.permissions;
    }
}
