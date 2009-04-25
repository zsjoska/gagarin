package org.csovessoft.contabil;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.csovessoft.contabil.user.User2;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void testApp() {
		User2 user = new User2();
		user.setName("Alma");
		user.setPassword("kukac");
		createHoney(user);
		assertNotNull("persistent user id should not be null", user.getId());
		System.out.println("ok");
	}

	private void createHoney(User2 user) {
		user.setId(System.currentTimeMillis());
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();

		// Transaction tx = null;
		// Session session =
		// SessionFactoryUtil.getInstance().getCurrentSession();
		// try {
		// tx = session.beginTransaction();
		// session.save(user);
		// tx.commit();
		// } catch (RuntimeException e) {
		// if (tx != null && tx.isActive()) {
		// try {
		// // Second try catch as the rollback could fail as well
		// tx.rollback();
		// } catch (HibernateException e1) {
		// logger.debug("Error rolling back transaction");
		// }
		// // throw again the first exception
		// throw e;
		// }
		// }
	}

	// public static void main(String[] args) {
	// User user = new User();
	// user.setName("Alma");
	// user.setPassword("kukac");
	// createHoney(user);
	// assertNotNull("persistent user id should not be null", user.getId());
	// System.out.println("ok");
	// }
}
