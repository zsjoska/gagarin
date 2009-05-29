package ro.gagarin.hibernate;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ro.gagarin.UserDAO;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.ItemNotFoundException;
import ro.gagarin.hibernate.objects.HDBUser;
import ro.gagarin.hibernate.objects.HDBUserRole;
import ro.gagarin.session.Session;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class HibernateUserDAO extends BaseHibernateDAO implements UserDAO {

	private static final transient Logger LOG = Logger.getLogger(HibernateUserDAO.class);

	public HibernateUserDAO(Session session) {
		super(session);
	}

	@Override
	public User userLogin(String username, String password) throws ItemNotFoundException {

		try {
			Query query = getEM().createQuery(
					"select u from DBUser u where u.username=:username and u.password=:password");
			query.setParameter("username", username);
			query.setParameter("password", password);

			User user = (User) query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			LOG.info("User " + username + " was not authenticated");
			throw new ItemNotFoundException(User.class, username);
		} catch (RuntimeException e) {
			super.markRollback();
			LOG.error("userLogin", e);
			throw e;
		}
	}

	@Override
	public long createUser(User user) throws FieldRequiredException, ItemNotFoundException {

		HibernateUtils.requireStringField("getUsername", user);
		HibernateUtils.requireStringField("getId", user);

		try {
			HDBUserRole dbRole = HibernateRoleDAO.findOrCreateRole(getEM(), user.getRole());
			if (dbRole == null) {
				throw new ItemNotFoundException(UserRole.class, "" + user.getRole().getId());
			}
			HDBUser dbUser = new HDBUser(user);
			dbUser.setRole(dbRole);

			getEM().persist(dbUser);
			getEM().flush();

			LOG.info("Created user:" + user.getUsername() + "; id:" + user.getId());
			return user.getId();
		} catch (RuntimeException e) {
			markRollback();
			LOG.error("createUser", e);
			throw e;
		}
	}

	@Override
	public User getUserByUsername(String username) {

		try {
			Query query = getEM().createQuery("select u from DBUser u where u.username=:username");
			query.setParameter("username", username);
			User user = (User) query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		} catch (RuntimeException e) {
			super.markRollback();
			LOG.error("getUserByUsername", e);
			throw e;
		}
	}

	@Override
	public void deleteUserById(long id) {
		try {
			HDBUser dbUser = getEM().find(HDBUser.class, id);
			if (dbUser == null)
				return;
			LOG.info("Delete user:" + dbUser.getUsername() + "; id:" + dbUser.getId());

			getEM().remove(dbUser);
		} catch (RuntimeException e) {
			super.markRollback();
			LOG.error("deleteUserById", e);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersWithRole(UserRole role) {
		try {
			Query query = getEM().createQuery("select u from DBUser u where u.role.id=:roleid");
			query.setParameter("roleid", role.getId());
			List result = query.getResultList();
			return result;
		} catch (RuntimeException e) {
			super.markRollback();
			LOG.error("getUsersWithRole", e);
			throw e;
		}

	}

	@Override
	public void deleteUser(User user) {
		deleteUserById(user.getId());
	}

	@Override
	public List<User> getAllUsers() {
		try {
			Query query = getEM().createQuery("select u from DBUser u");
			List result = query.getResultList();
			return result;
		} catch (RuntimeException e) {
			super.markRollback();
			LOG.error("getAllUsers", e);
			throw e;
		}
	}
}