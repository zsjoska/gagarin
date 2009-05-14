package ro.gagarin.jdbc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import ro.gagarin.BaseDAO;
import ro.gagarin.session.Session;

public class BaseJdbcDAO implements BaseDAO {
	private static final transient Logger LOG = Logger.getLogger(BaseJdbcDAO.class);

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("gagarin");

	private EntityManager em = null;
	private boolean ourEntityManager = false;

	public BaseJdbcDAO(Session session) {
		if (session == null)
			throw new NullPointerException("attempt to initialize BaseHibernateManager with null");

		synchronized (session) {
			session.setBusy(true);
			Object property = session.getProperty(BaseJdbcDAO.class);
			if (property instanceof BaseJdbcDAO) {
				EntityManager em = ((BaseJdbcDAO) property).getEM();
				this.em = em;
				this.ourEntityManager = false;
			} else {
				// null -- or wrong object
				if (property != null) {
					throw new RuntimeException("Wrong object type was found on session for "
							+ BaseJdbcDAO.class.getName() + "; found:"
							+ property.getClass().getName());
				}
				this.em = emf.createEntityManager();
				this.em.getTransaction().begin();
				this.ourEntityManager = true;
				session.setProperty(BaseJdbcDAO.class, this);
				LOG.debug("Created EntityManagerInstance " + em.toString());
			}
		}
	}

	EntityManager getEM() {
		return this.em;
	}

	public void release() {

		EntityManager tmpem = this.em;
		this.em = null;

		if (this.ourEntityManager) {

			RuntimeException exception = null;
			if (!tmpem.getTransaction().getRollbackOnly()) {
				LOG.debug("Committing EntityManagerInstance " + tmpem.toString());
				try {
					tmpem.getTransaction().commit();
					tmpem.close();
					LOG.debug("Released EntityManagerInstance " + tmpem.toString());

					return;
				} catch (RuntimeException e) {
					// this is the most relevant exception, so keep it then
					// throw it
					exception = e;
					LOG.error("Exception on commit:", e);
				}
			}
			LOG.debug("Rollback EntityManagerInstance " + tmpem.toString());
			try {
				tmpem.getTransaction().rollback();
			} catch (RuntimeException e) {
				if (exception == null)
					exception = e;
				LOG.error("Exception on rollback:", e);
			}
			try {
				tmpem.close();
			} catch (RuntimeException e) {
				if (exception == null)
					exception = e;
				LOG.error("Exception on close:", e);
			}
			if (exception != null)
				throw exception;
		}

	}

	public void markRollback() {
		this.em.getTransaction().setRollbackOnly();
	}
}
