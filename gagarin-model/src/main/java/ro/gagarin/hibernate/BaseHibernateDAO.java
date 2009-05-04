package ro.gagarin.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import ro.gagarin.session.Session;

public class BaseHibernateDAO {
	private static final transient Logger LOG = Logger.getLogger(BaseHibernateDAO.class);
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("gagarin");

	private EntityManager em = null;
	private boolean ourEntityManager = false;

	public BaseHibernateDAO(Session session) {
		if (session == null)
			throw new NullPointerException("attempt to initialize BaseHibernateManager with null");

		synchronized (session) {
			Object property = session.getProperty(BaseHibernateDAO.class);
			if (property instanceof BaseHibernateDAO) {
				EntityManager em = ((BaseHibernateDAO) property).getEM();
				this.em = em;
				this.ourEntityManager = false;
			} else {
				// null -- or wrong object
				if (property != null) {
					throw new RuntimeException("Wrong object type was found on session for "
							+ BaseHibernateDAO.class.getName() + "; found:"
							+ property.getClass().getName());
				}
				this.em = emf.createEntityManager();
				this.em.getTransaction().begin();
				this.ourEntityManager = true;
				session.setProperty(BaseHibernateDAO.class, this);
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
