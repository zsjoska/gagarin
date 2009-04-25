package org.csovessoft.contabil;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.csovessoft.contabil.user.User2;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void testCreate() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
		EntityManager em = emf.createEntityManager();

		User2 user = new User2();
		user.setName("Alma");
		user.setUsername("Alma");
		user.setPassword("kukac");
		user.setId(System.currentTimeMillis());

		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();

		assertNotNull("persistent user id should not be null", user.getId());
		System.out.println("ok");
	}

	@Test
	public void testGet() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
		EntityManager em = emf.createEntityManager();

		Query query = em.createQuery("select u from User2 u where u.username=:username");
		query.setParameter("username", "Alma");
		User2 user = (User2) query.getSingleResult();

		assertNotNull("persistent user id should not be null", user.getId());
		System.out.println("ok");
	}
}
