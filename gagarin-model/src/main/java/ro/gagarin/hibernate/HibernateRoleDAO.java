package ro.gagarin.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ro.gagarin.RoleDAO;
import ro.gagarin.hibernate.objects.DBUserPermission;
import ro.gagarin.hibernate.objects.DBUserRole;
import ro.gagarin.session.Session;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

public class HibernateRoleDAO extends BaseHibernateDAO implements RoleDAO {

	private static final transient Logger LOG = Logger.getLogger(HibernateRoleDAO.class);

	public HibernateRoleDAO(Session session) {
		super(session);
	}

	@Override
	public DBUserRole getRoleByName(String roleName) {

		Query query = getEM().createQuery("select r from DBUserRole r where r.roleName=:roleName");
		query.setParameter("roleName", roleName);

		try {
			DBUserRole adminRole = (DBUserRole) query.getSingleResult();
			return adminRole;
		} catch (NoResultException e) {
			LOG.info("No " + roleName + " role found");
			return null;
		}

	}

	@Override
	public long createRole(UserRole role) {

		try {

			DBUserRole dbRole = findRole(getEM(), role);

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

	public static Set<UserPermission> findPermissionSet(EntityManager em, Set<UserPermission> set)
			throws EntityNotFoundException {

		Set<UserPermission> permissions = new HashSet<UserPermission>();
		for (UserPermission userPermission : set) {
			permissions.add(findPermission(em, userPermission));
		}
		return permissions;
	}

	public static DBUserRole findRole(EntityManager em, UserRole role)
			throws EntityNotFoundException {

		DBUserRole reference = em.find(DBUserRole.class, role.getId());

		if (reference != null) {
			LOG.debug("found role" + reference.getId());
		} else if (role instanceof DBUserRole) {
			reference = (DBUserRole) role;

		} else {
			reference = new DBUserRole(role);
		}

		Set<UserPermission> userPermissions = reference.getUserPermissions();
		reference.setUserPermissions(new HashSet<UserPermission>());
		reference.setUserPermissions(findPermissionSet(em, userPermissions));

		return reference;
	}

	public static DBUserPermission findPermission(EntityManager em, UserPermission perm)
			throws EntityNotFoundException {

		DBUserPermission reference = em.find(DBUserPermission.class, perm.getId());

		if (reference != null) {
			LOG.debug("found permission" + reference.getId());
			return reference;
		} else if (perm instanceof DBUserPermission) {
			reference = (DBUserPermission) perm;

		} else {
			reference = new DBUserPermission(perm);
		}
		Set<UserRole> userRoles = reference.getUserRoles();
		reference.setUserRoles(null);
		reference.setUserRoles(findRoleSet(em, userRoles));

		return reference;
	}

	public static Set<UserRole> findRoleSet(EntityManager em, Set<UserRole> set)
			throws EntityNotFoundException {

		Set<UserRole> roles = new HashSet<UserRole>();
		for (UserRole userRole : set) {
			roles.add(findRole(em, userRole));
		}
		return roles;
	}

	@Override
	public long createPermission(UserPermission perm) {
		// TODO: requireStringField(user.getUsername(), "username");

		DBUserPermission dbPermission = findPermission(getEM(), perm);

		try {
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

		Query query = getEM().createQuery("select p from DBUserPermission p");
		try {
			List<UserPermission> resultList = query.getResultList();
			return resultList;

		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteRole(UserRole role) {
		DBUserRole dbUserRole = findRole(getEM(), role);
		getEM().remove(dbUserRole);
		LOG.info("Deleted role:" + role.getRoleName() + "; id:" + role.getId());
	}

	@Override
	public UserPermission getPermissionByName(String permissionName) {
		Query query = getEM().createQuery(
				"select p from DBUserPermission p where p.permissionName=:permissionName");
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
		getEM().remove(findPermission(getEM(), perm));
		LOG.info("Deleted permission:" + perm.getPermissionName() + "; id:" + perm.getId());
	}

	@Override
	public List<UserRole> getAllRoles() {

		Query query = getEM().createQuery("select r from DBUserRole r ");
		return query.getResultList();
	}

	@Override
	public List<UserPermission> substractUsersRolePermissions(UserRole main, UserRole substract) {
		Query query = getEM()
				.createQuery(
						"select r from DBUserPermission r where r.id=:subRoleid and r not in (select p from DBUserPermission p where p.id=:mainRoleid)")
				.setParameter("mainRoleid", main.getId()).setParameter("subRoleid",
						substract.getId());
		return query.getResultList();
	}
}
