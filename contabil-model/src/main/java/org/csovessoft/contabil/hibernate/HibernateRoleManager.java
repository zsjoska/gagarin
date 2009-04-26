package org.csovessoft.contabil.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.RoleManager;
import org.csovessoft.contabil.exceptions.AlreadyExistsException;
import org.csovessoft.contabil.user.UserPermission;
import org.csovessoft.contabil.user.UserRole;
import org.hibernate.exception.ConstraintViolationException;

public class HibernateRoleManager implements RoleManager {

	private static final transient Logger LOG = Logger.getLogger(HibernateRoleManager.class);

	private static final HibernateRoleManager INSTANCE = new HibernateRoleManager();

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
	private EntityManager em = emf.createEntityManager();

	public static HibernateRoleManager getInstance() {
		return INSTANCE;
	}

	@Override
	public UserRole getAdminRole(String adminRoleName) {

		Query query = em.createQuery("select r from UserRole r where r.roleName=:roleName");
		query.setParameter("roleName", adminRoleName);

		try {
			UserRole adminRole = (UserRole) query.getSingleResult();
			return adminRole;
		} catch (NoResultException e) {
			LOG.info("No " + adminRoleName + " role found");
			return null;
		}
	}

	@Override
	public long createRole(UserRole role) throws AlreadyExistsException {
		// requireStringField(user.getUsername(), "username");

		em.getTransaction().begin();
		role.generateId();
		em.persist(role);
		try {
			commit(em);
		} catch (ConstraintViolationException e) {
			throw new AlreadyExistsException("ROLES", role.getRoleName(), e);
		}
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

		em.getTransaction().begin();
		perm.generateId();
		em.persist(perm);
		try {
			commit(em);
		} catch (ConstraintViolationException e) {
			throw new AlreadyExistsException("USER_PERMISSIONS", perm.getPermissionName());
		}
		LOG.info("Created permission:" + perm.getPermissionName() + "; id:" + perm.getId());
		return perm.getId();
	}

	@Override
	public List<UserPermission> getAllPermissions() {

		Query query = em.createQuery("select p from UserPermission p");
		try {
			List<UserPermission> resultList = query.getResultList();
			return resultList;

		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void saveRole(UserRole role) throws AlreadyExistsException {

		em.getTransaction().begin();
		// UserRole merge = em.merge(role);
		// em.merge(role.getUserPermissions());
		// em.persist(merge);
		em.flush();
		try {
			commit(em);
		} catch (ConstraintViolationException e) {
			throw new AlreadyExistsException("ROLES", role.getRoleName(), e);
		}
		LOG.info("Created role:" + role.getRoleName() + "; id:" + role.getId());
	}

}
