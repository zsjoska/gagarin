package ro.gagarin;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;

import ro.gagarin.user.User;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void testCreate() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
		EntityManager em = emf.createEntityManager();

		User user = new User();
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

		Query query = em.createQuery("select u from User u where u.username=:username");
		query.setParameter("username", "Alma");
		User user = (User) query.getSingleResult();

		assertNotNull("persistent user id should not be null", user.getId());
		System.out.println("ok");
	}

	@Test
	public void testDelete() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("contabil");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Query query = em.createQuery("select u from User u where u.username=:username");
		query.setParameter("username", "Alma");
		User user = (User) query.getSingleResult();

		em.remove(user);
		em.getTransaction().commit();
	}
}
