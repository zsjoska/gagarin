package ro.gagarin.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import ro.gagarin.session.Session;

public class BaseHibernateManager {
	private static final transient Logger LOG = Logger.getLogger(BaseHibernateManager.class);
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("gagarin");

	private EntityManager em = null;
	private boolean ourEntityManager = false;

	public BaseHibernateManager(Session session) {
		if (session == null)
			throw new NullPointerException("attempt to initialize BaseHibernateManager with null");

		synchronized (session) {
			Object property = session.getProperty(BaseHibernateManager.class);
			if (property instanceof BaseHibernateManager) {
				EntityManager em = ((BaseHibernateManager) property).getEM();
				this.em = em;
				this.ourEntityManager = false;
			} else {
				// null -- or wrong object
				if (property != null) {
					throw new RuntimeException("Wrong object type was found on session for "
							+ BaseHibernateManager.class.getName() + "; found:"
							+ property.getClass().getName());
				}
				this.em = emf.createEntityManager();
				this.em.getTransaction().begin();
				this.ourEntityManager = true;
				session.setProperty(BaseHibernateManager.class, this);
				LOG.debug("Created EntityManagerInstance " + em.toString());
			}
		}
	}

	EntityManager getEM() {
		return this.em;
	}

	public void release() {

		// this class could be

		if (this.ourEntityManager) {
			LOG.debug("Committing EntityManagerInstance " + em.toString());
			RuntimeException exception = null;
			try {
				em.getTransaction().commit();
				this.em.close();
				LOG.debug("Released EntityManagerInstance " + em.toString());
				this.em = null;
				return;
			} catch (RuntimeException e) {
				exception = e;
				LOG.error("Exception on commit:", e);
			}
			try {
				em.getTransaction().rollback();
			} catch (Exception e) {
				LOG.error("Exception on rollback:", e);
			}
			this.em.close();
			throw exception;
		}
		this.em = null;
	}
}
