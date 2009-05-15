package ro.gagarin.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.hibernate.objects.DBUserRole;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class JdbcRoleDAO extends BaseJdbcDAO implements RoleDAO {

	private static final transient Logger LOG = Logger.getLogger(JdbcRoleDAO.class);

	public JdbcRoleDAO(Session session) {
		super(session);
	}

	@Override
	public DBUserRole getRoleByName(String roleName) {

		DBUserRole role = new DBUserRole();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, roleName FROM UserRoles WHERE roleName = ?");
			query.setString(1, roleName);
			rs = query.executeQuery();
			if (rs.next()) {
				role.setId(rs.getLong("id"));
				role.setRoleName(rs.getString("roleName"));
				rs.close();
				return role;
			} else {
				LOG.info("UserRole " + roleName + " was not found");
			}
		} catch (SQLException e) {
			LOG.error("getRoleByName: Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					LOG.error("getRoleByName: Error on close", e);
				}
		}
		return null;
		// try {
		// Query query = getEM().createQuery(
		// "select r from DBUserRole r where r.roleName=:roleName");
		// query.setParameter("roleName", roleName);
		//
		// DBUserRole adminRole = (DBUserRole) query.getSingleResult();
		// return adminRole;
		// } catch (NoResultException e) {
		// LOG.info("No " + roleName + " role found");
		// return null;
		// } catch (RuntimeException e) {
		// LOG.error("getRoleByName", e);
		// throw e;
		// }

	}

	@Override
	public long createRole(UserRole role) {
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO UserRoles( id, roleName) VALUES (?,?)");
			query.setLong(1, role.getId());
			query.setString(2, role.getRoleName());
			int rows = query.executeUpdate();

			if (rows == 1) {
				LOG.info("UserPermission " + role.getRoleName() + " was created");
				return role.getId();
			} else {
				LOG.info("UserPermission " + role.getRoleName() + " was not created");
			}
		} catch (SQLException e) {
			LOG.error("createPermission: Error Executing query", e);
			super.markRollback();
		}

		// try {
		//
		// DBUserRole dbRole = findOrCreateRole(getEM(), role);
		//
		// // requireStringField(user.getUsername(), "username");
		//
		// getEM().persist(dbRole);
		// getEM().flush();
		//
		// LOG.info("Created role:" + role.getRoleName() + "; id:" +
		// role.getId());
		// return role.getId();
		// } catch (RuntimeException e) {
		// LOG.error("createRole", e);
		// throw e;
		// }

		return 0;
	}

	public static DBUserRole findOrCreateRole(EntityManager em, UserRole role) {

		// DBUserRole reference = em.find(DBUserRole.class, role.getId());
		//
		// if (reference != null) {
		// LOG.debug("found role" + reference.getId());
		// } else if (role instanceof DBUserRole) {
		// reference = (DBUserRole) role;
		//
		// } else {
		// reference = new DBUserRole(role);
		// }
		//
		// return reference;
		return null;
	}

	public static DBUserPermission findPermission(EntityManager em, UserPermission perm)
			throws EntityNotFoundException {

		// DBUserPermission reference = em.find(DBUserPermission.class,
		// perm.getId());
		//
		// if (reference != null) {
		// LOG.debug("found permission" + reference.getId());
		// return reference;
		// } else if (perm instanceof DBUserPermission) {
		// reference = (DBUserPermission) perm;
		//
		// } else {
		// reference = new DBUserPermission(perm);
		// }
		//
		// return reference;
		return null;
	}

	@Override
	public long createPermission(UserPermission perm) {

		try {
			PreparedStatement query = getConnection().prepareStatement(
					"INSERT INTO UserPermissions( id, permissionName) VALUES (?,?)");
			query.setLong(1, perm.getId());
			query.setString(2, perm.getPermissionName());
			int rows = query.executeUpdate();

			if (rows == 1) {
				LOG.info("UserPermission " + perm.getPermissionName() + " was created");
				return perm.getId();
			} else {
				LOG.info("UserPermission " + perm.getPermissionName() + " was not created");
			}
		} catch (SQLException e) {
			LOG.error("createPermission: Error Executing query", e);
			super.markRollback();
		}

		// // TODO: requireStringField(user.getUsername(), "username");
		//
		// try {
		// DBUserPermission dbPermission = findPermission(getEM(), perm);
		//
		// getEM().persist(dbPermission);
		// getEM().flush();
		// LOG.info("Created permission:" + perm.getPermissionName() + "; id:" +
		// perm.getId());
		// return perm.getId();
		// } catch (RuntimeException e) {
		// LOG.error("createPermission", e);
		// throw e;
		// }
		return 0;
	}

	@Override
	public List<UserPermission> getAllPermissions() {

		List<UserPermission> permissions = new ArrayList<UserPermission>();

		ResultSet rs = null;
		try {
			PreparedStatement query = getConnection().prepareStatement(
					"SELECT id, permissionName FROM UserPermissions");
			rs = query.executeQuery();
			while (rs.next()) {
				DBUserPermission permission = new DBUserPermission();
				permission.setId(rs.getLong("id"));
				permission.setPermissionName(rs.getString("permissionName"));
				permissions.add(permission);
			}
			rs.close();
		} catch (SQLException e) {
			LOG.error("getRoleByName: Error Executing query", e);
			super.markRollback();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					LOG.error("getRoleByName: Error on close", e);
				}
		}
		return permissions;

		// try {
		// Query query =
		// getEM().createQuery("select p from DBUserPermission p");
		// List<UserPermission> resultList = query.getResultList();
		// return resultList;
		//
		// } catch (NoResultException e) {
		// return null;
		// } catch (RuntimeException e) {
		// LOG.error("getAllPermissions", e);
		// throw e;
		// }

	}

	@Override
	public void deleteRole(UserRole role) {
		// try {
		// DBUserRole dbUserRole = getEM().find(DBUserRole.class, role.getId());
		// if (dbUserRole == null) {
		// LOG.debug("Role was not found:" + role.getRoleName() + "; id:" +
		// role.getId());
		// return;
		// }
		//
		// getEM().remove(dbUserRole);
		// LOG.info("Deleted role:" + role.getRoleName() + "; id:" +
		// role.getId());
		// } catch (RuntimeException e) {
		// LOG.error("deleteRole", e);
		// throw e;
		// }
	}

	@Override
	public UserPermission getPermissionByName(String permissionName) {
		// try {
		// Query query = getEM().createQuery(
		// "select p from DBUserPermission p where p.permissionName=:permissionName");
		// query.setParameter("permissionName", permissionName);
		//
		// UserPermission permission = (UserPermission) query.getSingleResult();
		// return permission;
		// } catch (NoResultException e) {
		// LOG.info("No " + permissionName + " role found");
		// return null;
		// } catch (RuntimeException e) {
		// LOG.error("getPermissionByName", e);
		// throw e;
		// }
		return null;
	}

	@Override
	public void deletePermission(UserPermission perm) {
		// try {
		// DBUserPermission dbUserPermission =
		// getEM().find(DBUserPermission.class, perm.getId());
		// if (dbUserPermission == null) {
		// LOG.debug("Permission was not found:" + perm.getPermissionName() +
		// "; id:"
		// + perm.getId());
		// return;
		// }
		// getEM().remove(dbUserPermission);
		// LOG.info("Deleted permission:" + perm.getPermissionName() + "; id:" +
		// perm.getId());
		// } catch (RuntimeException e) {
		// LOG.error("deletePermission", e);
		// throw e;
		// }

	}

	@Override
	public List<UserRole> getAllRoles() {
		// try {
		// Query query = getEM().createQuery("select r from DBUserRole r ");
		// return query.getResultList();
		// } catch (RuntimeException e) {
		// LOG.error("getAllRoles", e);
		// throw e;
		// }
		return null;
	}

	@Override
	public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {
		// try {
		// // DBUserRole dbMain = getEM().find(DBUserRole.class, main.getId());
		// // DBUserRole dbSubstract = getEM().find(DBUserRole.class,
		// // substract.getId());
		// // Query query = getEM()
		// // .createQuery(
		// //
		// "select r from DBUserPermission r where r.id=:subRoleid and r not in (select p from DBUserPermission p where p.id=:mainRoleid)")
		// // .setParameter("mainRoleid",
		// // main.getId()).setParameter("subRoleid",
		// // substract.getId());
		// // Query query = getEM()
		// // .createQuery(
		// // ""
		// // +
		// //
		// "select r.userPermissions as p from DBUserRole r where r.id=:subRoleid and (p not in ("
		// // +
		// //
		// "select q.userPermissions as s from DBUserRole q where q.id=:mainRoleid))");
		// // query.setParameter("subRoleid", substract.getId());
		// // query.setParameter("mainRoleid", main.getId());
		// Query query = getEM()
		// .createQuery(
		// "select q.userPermission from DBRoleAssignment q where q.userRole.id=:mainRoleid");
		// query.setParameter("mainRoleid", main.getId());
		// return query.getResultList();
		// } catch (RuntimeException e) {
		// LOG.error("substractUsersRolePermissions", e);
		// throw e;
		// }
		return null;
	}

	@Override
	public void assignPermissionToRole(UserRole role, UserPermission perm)
			throws ItemNotFoundException {
		// try {
		// DBUserRole dbRole = getEM().find(DBUserRole.class, role.getId());
		// if (dbRole == null)
		// throw new ItemNotFoundException(UserRole.class,
		// role.getId().toString());
		//
		// DBUserPermission dbPerm = getEM().find(DBUserPermission.class,
		// perm.getId());
		// if (dbPerm == null)
		// throw new ItemNotFoundException(UserPermission.class,
		// perm.getId().toString());
		//
		// dbRole.getUserPermissions().add(dbPerm);
		// dbPerm.getUserRoles().add(dbRole);
		// } catch (RuntimeException e) {
		// LOG.error("assignPermissionToRole", e);
		// throw e;
		// }
	}
}
