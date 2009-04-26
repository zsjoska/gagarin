package org.csovessoft.contabil.user;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.csovessoft.contabil.UserManager;
import org.csovessoft.contabil.exceptions.FieldRequiredException;
import org.csovessoft.contabil.exceptions.UserAlreadyExistsException;
import org.csovessoft.contabil.exceptions.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

public class HibernateUserManager implements UserManager {

	private static final transient Logger LOG = Logger.getLogger(HibernateUserManager.class);

	private static final HibernateUserManager INSTANCE = new HibernateUserManager();

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");

	private HibernateUserManager() {

		// User user = new User();
		// user.setUsername("admin");
		// user.setPassword("test");
		//
		// try {
		// createUser(user);
		// LOG.info("User admin created with password test");
		// } catch (ExceptionBase e) {
		// LOG.error("Exception creating admin user", e);
		// }
	}

	public static UserManager getInstance() {
		return INSTANCE;
	}

	@Override
	public User userLogin(String username, String password) throws UserNotFoundException {

		EntityManager em = emf.createEntityManager();

		Query query = em
				.createQuery("select u from User u where u.username=:username and u.password=:password");
		query.setParameter("username", username);
		query.setParameter("password", password);

		try {
			User user = (User) query.getSingleResult();
			return user;
		} catch (NoResultException e) {
			LOG.info("User " + username + " was not authenticated");
			throw new UserNotFoundException(username);
		}

		// User user = this.users_userName.get(username);
		// if (user != null && user.getPassword().equals(password)) {
		// return user;
		// }
		// throw new UserNotFoundException(username);
	}

	@Override
	public long createUser(User user) throws FieldRequiredException, UserAlreadyExistsException {

		requireStringField(user.getUsername(), "username");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		user.generateId();
		em.persist(user);
		try {
			commit(em);
		} catch (ConstraintViolationException e) {
			throw new UserAlreadyExistsException("username", user.getUsername());
		}
		// em.getTransaction().commit();

		// User user2 = this.users_userName.get(user.getUsername());
		// if (user2 != null)
		// throw new UserAlreadyExistsException("username", user.getUsername());
		//
		// user.setId(String.valueOf(System.nanoTime()));
		// users_id.put(user.getId(), user);
		// users_userName.put(user.getUsername(), user);
		// LOG.info("Created user:" + user.getUsername() + "; id:" +
		// user.getId());
		// return user.getId();
		return user.getId();
	}

	private void commit(EntityManager em) {
		try {
			em.getTransaction().commit();
		} catch (RollbackException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ConstraintViolationException) {
				throw (ConstraintViolationException) cause;
			}
		}

	}

	private void requireStringField(String value, String fieldname) throws FieldRequiredException {
		if (value == null || value.length() == 0)
			throw new FieldRequiredException(fieldname, User.class);
	}

	@Override
	public User getUserByUsername(String username) {
		EntityManager em = emf.createEntityManager();

		Query query = em.createQuery("select u from User u where u.username=:username");
		query.setParameter("username", username);
		User user = (User) query.getSingleResult();
		// return this.users_userName.get(username);
		return user;
	}

	@Override
	public void deleteUserById(String id) {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Query query = em.createQuery("select u from User u where u.id=:id");
		query.setParameter("id", id);
		User user = (User) query.getSingleResult();

		if (user == null)
			return;
		LOG.info("Delete user:" + user.getUsername() + "; id:" + user.getId());

		em.remove(user);
		em.getTransaction().commit();

		// User user = this.users_id.get(id);
		// this.users_id.remove(id);
		// this.users_userName.remove(user.getUsername());
	}

}
