package ro.gagarin.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import ro.gagarin.RoleManager;
import ro.gagarin.exceptions.AlreadyExistsException;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class HibernateRoleManager implements RoleManager {

	private static final transient Logger LOG = Logger.getLogger(HibernateRoleManager.class);
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
	private static final HibernateRoleManager INSTANCE = new HibernateRoleManager();

	private EntityManager em = null;

	public static HibernateRoleManager getInstance() {
		return INSTANCE;
	}

	public HibernateRoleManager() {
		this.em = emf.createEntityManager();
		this.em.getTransaction().begin();
	}

	@Override
	public UserRole getRoleByName(String roleName) {

		Query query = em.createQuery("select r from UserRole r where r.roleName=:roleName");
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
	public long createRole(UserRole role) throws AlreadyExistsException {
		// requireStringField(user.getUsername(), "username");

		// em.getTransaction().begin();
		role.generateId();
		em.persist(role);
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
	public long createPermission(UserPermission perm) throws AlreadyExistsException {
		// requireStringField(user.getUsername(), "username");

		// em.getTransaction().begin();
		perm.generateId();
		em.persist(perm);
		// try {
		// commit(em);
		// } catch (ConstraintViolationException e) {
		// // TODO: other types of error could cause this... so make it more
		// // generic
		// throw new AlreadyExistsException("USER_PERMISSIONS",
		// perm.getPermissionName(), e);
		// }
		LOG.info("Created permission:" + perm.getPermissionName() + "; id:" + perm.getId());
		return perm.getId();
	}

	@Override
	public List<UserPermission> getAllPermissions() {

		Query query = em.createQuery("select p from UserPermission p");
		try {
			List<UserPermission> permissions = new ArrayList<UserPermission>();
			List<UserPermission> resultList = query.getResultList();
			for (UserPermission userPermission : resultList) {
				permissions.add(userPermission);
			}
			return permissions;

		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void saveRole(UserRole role) throws AlreadyExistsException {

		// em.getTransaction().begin();
		// UserRole merge = em.merge(role);
		// // em.merge(role.getUserPermissions());
		// em.persist(merge);
		// // em.flush();
		// try {
		// commit(em);
		// } catch (ConstraintViolationException e) {
		// throw new AlreadyExistsException("ROLES", role.getRoleName(), e);
		// }
		// LOG.info("Created role:" + role.getRoleName() + "; id:" +
		// role.getId());
	}

	@Override
	public void release() {
		commit(em);
		this.em.close();
		this.em = null;
	}

	@Override
	public void deleteRole(UserRole role) {
		// em.getTransaction().begin();
		em.remove(role);
		// commit(em);
		LOG.info("Deleted role:" + role.getRoleName() + "; id:" + role.getId());
	}

	@Override
	public UserPermission getPermissionByName(String permissionName) {
		Query query = em
				.createQuery("select p from UserPermission p where p.permissionName=:permissionName");
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
		em.remove(perm);
		// commit(em);
		LOG.info("Deleted permission:" + perm.getPermissionName() + "; id:" + perm.getId());
	}

}
