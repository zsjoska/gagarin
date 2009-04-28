package ro.gagarin.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import ro.gagarin.BaseManager;

public class BaseHibernateManager {
	private static final transient Logger LOG = Logger.getLogger(BaseHibernateManager.class);
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("gagarin");

	private EntityManager em = null;
	private boolean ourEntityManager;

	public BaseHibernateManager() {
		this.em = emf.createEntityManager();
		this.em.getTransaction().begin();
		this.ourEntityManager = true;
		LOG.debug("Created EntityManagerInstance " + em.toString());
	}

	public BaseHibernateManager(BaseManager mgr) {
		if (mgr == null)
			throw new NullPointerException("attempt to initialize BaseHibernateManager with null");
		if (mgr instanceof BaseHibernateManager) {
			this.em = ((BaseHibernateManager) mgr).getEM();
			this.ourEntityManager = false;
		} else {
			this.em = emf.createEntityManager();
			this.em.getTransaction().begin();
			this.ourEntityManager = true;
			LOG.debug("Created EntityManagerInstance " + em.toString());
		}
	}

	EntityManager getEM() {
		return this.em;
	}

	public void release() {
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
