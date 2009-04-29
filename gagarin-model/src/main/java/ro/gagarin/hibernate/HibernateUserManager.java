package ro.gagarin.hibernate;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ro.gagarin.BaseManager;
import ro.gagarin.UserManager;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.UserAlreadyExistsException;
import ro.gagarin.exceptions.UserNotFoundException;
import ro.gagarin.user.DBUser;
import ro.gagarin.user.User;
import ro.gagarin.user.UserRole;

public class HibernateUserManager extends BaseHibernateManager implements UserManager {

	private static final transient Logger LOG = Logger.getLogger(HibernateUserManager.class);

	public HibernateUserManager() {
	}

	public HibernateUserManager(BaseManager mgr) {
		super(mgr);
	}

	@Override
	public User userLogin(String username, String password) throws UserNotFoundException {

		Query query = getEM().createQuery(
				"select u from User u where u.username=:username and u.password=:password");
		query.setParameter("username", username);
		query.setParameter("password", password);

		try {
			User user = (User) query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			LOG.info("User " + username + " was not authenticated");
			throw new UserNotFoundException(username);
		}
	}

	@Override
	public long createUser(DBUser user) throws FieldRequiredException, UserAlreadyExistsException {

		requireStringField(user.getUsername(), "username");

		getEM().persist(user);

		LOG.info("Created user:" + user.getUsername() + "; id:" + user.getId());
		return user.getId();
	}

	private void requireStringField(String value, String fieldname) throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname, DBUser.class);
	}

	@Override
	public User getUserByUsername(String username) {

		Query query = getEM().createQuery("select u from DBUser u where u.username=:username");
		query.setParameter("username", username);
		try {
			User user = (User) query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void deleteUserById(long id) {

		Query query = getEM().createQuery("select u from DBUser u where u.id=:id");
		query.setParameter("id", id);
		User user = (User) query.getSingleResult();

		if (user == null)
			return;
		LOG.info("Delete user:" + user.getUsername() + "; id:" + user.getId());

		getEM().remove(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersWithRole(UserRole role) {

		Query query = getEM().createQuery("select u from DBUser u where u.role=:role");
		query.setParameter("role", role);
		List result = query.getResultList();
		return result;

	}
}
