package ro.gagarin.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ro.gagarin.RoleDAO;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.hibernate.objects.HDBUserPermission;
import ro.gagarin.hibernate.objects.HDBUserRole;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class HibernateRoleDAO extends BaseHibernateDAO implements RoleDAO {

	private static final transient Logger LOG = Logger.getLogger(HibernateRoleDAO.class);

	public HibernateRoleDAO(Session session) {
		super(session);
	}

	@Override
	public HDBUserRole getRoleByName(String roleName) {

		try {
			Query query = getEM().createQuery(
					"select r from DBUserRole r where r.roleName=:roleName");
			query.setParameter("roleName", roleName);

			HDBUserRole adminRole = (HDBUserRole) query.getSingleResult();
			return adminRole;
		} catch (NoResultException e) {
			LOG.info("No " + roleName + " role found");
			return null;
		} catch (RuntimeException e) {
			LOG.error("getRoleByName", e);
			throw e;
		}

	}

	@Override
	public long createRole(UserRole role) {

		try {

			HDBUserRole dbRole = findOrCreateRole(getEM(), role);

			// requireStringField(user.getUsername(), "username");

			getEM().persist(dbRole);
			getEM().flush();

			LOG.info("Created role:" + role.getRoleName() + "; id:" + role.getId());
			return role.getId();
		} catch (RuntimeException e) {
			LOG.error("createRole", e);
			throw e;
		}

	}

	public static HDBUserRole findOrCreateRole(EntityManager em, UserRole role) {

		HDBUserRole reference = em.find(HDBUserRole.class, role.getId());

		if (reference != null) {
			LOG.debug("found role" + reference.getId());
		} else if (role instanceof HDBUserRole) {
			reference = (HDBUserRole) role;

		} else {
			reference = new HDBUserRole(role);
		}

		return reference;
	}

	public static HDBUserPermission findPermission(EntityManager em, UserPermission perm)
			throws EntityNotFoundException {

		HDBUserPermission reference = em.find(HDBUserPermission.class, perm.getId());

		if (reference != null) {
			LOG.debug("found permission" + reference.getId());
			return reference;
		} else if (perm instanceof HDBUserPermission) {
			reference = (HDBUserPermission) perm;

		} else {
			reference = new HDBUserPermission(perm);
		}

		return reference;
	}

	@Override
	public long createPermission(UserPermission perm) {

		// TODO: requireStringField(user.getUsername(), "username");

		try {
			HDBUserPermission dbPermission = findPermission(getEM(), perm);

			getEM().persist(dbPermission);
			getEM().flush();
			LOG.info("Created permission:" + perm.getPermissionName() + "; id:" + perm.getId());
			return perm.getId();
		} catch (RuntimeException e) {
			LOG.error("createPermission", e);
			throw e;
		}
	}

	@Override
	public List<UserPermission> getAllPermissions() {

		try {
			Query query = getEM().createQuery("select p from DBUserPermission p");
			List<UserPermission> resultList = query.getResultList();
			return resultList;

		} catch (NoResultException e) {
			return null;
		} catch (RuntimeException e) {
			LOG.error("getAllPermissions", e);
			throw e;
		}

	}

	@Override
	public void deleteRole(UserRole role) {
		try {
			HDBUserRole dbUserRole = getEM().find(HDBUserRole.class, role.getId());
			if (dbUserRole == null) {
				LOG.debug("Role was not found:" + role.getRoleName() + "; id:" + role.getId());
				return;
			}

			getEM().remove(dbUserRole);
			LOG.info("Deleted role:" + role.getRoleName() + "; id:" + role.getId());
		} catch (RuntimeException e) {
			LOG.error("deleteRole", e);
			throw e;
		}
	}

	@Override
	public UserPermission getPermissionByName(String permissionName) {
		try {
			Query query = getEM().createQuery(
					"select p from DBUserPermission p where p.permissionName=:permissionName");
			query.setParameter("permissionName", permissionName);

			UserPermission permission = (UserPermission) query.getSingleResult();
			return permission;
		} catch (NoResultException e) {
			LOG.info("No " + permissionName + " role found");
			return null;
		} catch (RuntimeException e) {
			LOG.error("getPermissionByName", e);
			throw e;
		}
	}

	@Override
	public void deletePermission(UserPermission perm) {
		try {
			HDBUserPermission dbUserPermission = getEM().find(HDBUserPermission.class, perm.getId());
			if (dbUserPermission == null) {
				LOG.debug("Permission was not found:" + perm.getPermissionName() + "; id:"
						+ perm.getId());
				return;
			}
			getEM().remove(dbUserPermission);
			LOG.info("Deleted permission:" + perm.getPermissionName() + "; id:" + perm.getId());
		} catch (RuntimeException e) {
			LOG.error("deletePermission", e);
			throw e;
		}

	}

	@Override
	public List<UserRole> getAllRoles() {
		try {
			Query query = getEM().createQuery("select r from DBUserRole r ");
			return query.getResultList();
		} catch (RuntimeException e) {
			LOG.error("getAllRoles", e);
			throw e;
		}
	}

	@Override
	public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {
		try {
			// DBUserRole dbMain = getEM().find(DBUserRole.class, main.getId());
			// DBUserRole dbSubstract = getEM().find(DBUserRole.class,
			// substract.getId());
			// Query query = getEM()
			// .createQuery(
			// "select r from DBUserPermission r where r.id=:subRoleid and r not in (select p from DBUserPermission p where p.id=:mainRoleid)")
			// .setParameter("mainRoleid",
			// main.getId()).setParameter("subRoleid",
			// substract.getId());
			// Query query = getEM()
			// .createQuery(
			// ""
			// +
			// "select r.userPermissions as p from DBUserRole r where r.id=:subRoleid and (p not in ("
			// +
			// "select q.userPermissions as s from DBUserRole q where q.id=:mainRoleid))");
			// query.setParameter("subRoleid", substract.getId());
			// query.setParameter("mainRoleid", main.getId());
			Query query = getEM()
					.createQuery(
							"select q.userPermission from DBRoleAssignment q where q.userRole.id=:mainRoleid");
			query.setParameter("mainRoleid", main.getId());
			return query.getResultList();
		} catch (RuntimeException e) {
			LOG.error("substractUsersRolePermissions", e);
			throw e;
		}
	}

	@Override
	public void assignPermissionToRole(UserRole role, UserPermission perm)
			throws ItemNotFoundException {
		try {
			HDBUserRole dbRole = getEM().find(HDBUserRole.class, role.getId());
			if (dbRole == null)
				throw new ItemNotFoundException(UserRole.class, role.getId().toString());

			HDBUserPermission dbPerm = getEM().find(HDBUserPermission.class, perm.getId());
			if (dbPerm == null)
				throw new ItemNotFoundException(UserPermission.class, perm.getId().toString());

			dbRole.getUserPermissions().add(dbPerm);
			dbPerm.getUserRoles().add(dbRole);
		} catch (RuntimeException e) {
			LOG.error("assignPermissionToRole", e);
			throw e;
		}
	}
}
