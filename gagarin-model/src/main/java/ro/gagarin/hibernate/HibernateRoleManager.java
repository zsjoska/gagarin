package ro.gagarin.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import ro.gagarin.BaseManager;
import ro.gagarin.RoleManager;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class HibernateRoleManager extends BaseHibernateManager implements RoleManager {

	private static final transient Logger LOG = Logger.getLogger(HibernateRoleManager.class);

	public HibernateRoleManager() {
	}

	public HibernateRoleManager(BaseManager mgr) {
		super(mgr);
	}

	@Override
	public UserRole getRoleByName(String roleName) {

		Query query = getEM().createQuery("select r from UserRole r where r.roleName=:roleName");
		query.setParameter("roleName", roleName);

		try {
			UserRole adminRole = (UserRole) query.getSingleResult();
			return adminRole;
		} catch (NoResultException e) {
			LOG.info("No " + roleName + " role found");
			return null;
		}

	}

	@Override
	public long createRole(UserRole role) {
		// requireStringField(user.getUsername(), "username");

		// em.getTransaction().begin();
		getEM().persist(role);
		// try {
		// commit(em);
		// } catch (ConstraintViolationException e) {
		// throw new AlreadyExistsException("ROLES", role.getRoleName(), e);
		// }
		LOG.info("Created role:" + role.getRoleName() + "; id:" + role.getId());
		return role.getId();

	}

	// TODO: getRid of duplicate
	private void commit(EntityManager em) {
		try {
			em.getTransaction().commit();
		} catch (RollbackException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ConstraintViolationException) {
				throw (ConstraintViolationException) cause;
			}
			throw e;
		}

	}

	@Override
	public long createPermission(UserPermission perm) {
		// TODO: requireStringField(user.getUsername(), "username");

		getEM().persist(perm);
		getEM().flush();
		LOG.info("Created permission:" + perm.getPermissionName() + "; id:" + perm.getId());
		return perm.getId();
	}

	@Override
	public List<UserPermission> getAllPermissions() {

		Query query = getEM().createQuery("select p from UserPermission p");
		try {
			List<UserPermission> resultList = query.getResultList();
			return resultList;

		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteRole(UserRole role) {
		// em.getTransaction().begin();
		getEM().remove(role);
		// commit(em);
		LOG.info("Deleted role:" + role.getRoleName() + "; id:" + role.getId());
	}

	@Override
	public UserPermission getPermissionByName(String permissionName) {
		Query query = getEM().createQuery(
				"select p from UserPermission p where p.permissionName=:permissionName");
		query.setParameter("permissionName", permissionName);

		try {
			UserPermission permission = (UserPermission) query.getSingleResult();
			return permission;
		} catch (NoResultException e) {
			LOG.info("No " + permissionName + " role found");
			return null;
		}
	}

	@Override
	public void deletePermission(UserPermission perm) {
		// em.getTransaction().begin();
		getEM().remove(perm);
		// commit(em);
		LOG.info("Deleted permission:" + perm.getPermissionName() + "; id:" + perm.getId());
	}

	@Override
	public List<UserRole> getAllRoles() {

		Query query = getEM().createQuery("select r from UserRole r ");
		return query.getResultList();
	}

	@Override
	public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {
		Query query = getEM()
				.createQuery(
						"select r from UserPermission r where r.id=:subRoleid and r not in (select p from UserPermission p where p.id=:mainRoleid)")
				.setParameter("mainRoleid", main.getId()).setParameter("subRoleid",
						substract.getId());
		return query.getResultList();
	}
}
