package org.csovessoft.contabil.user;

import javax.persistence.EntityManager;

public class BaseHibernateService {
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public void create(BaseEntity entity) {
		getEm().persist(entity);
	}

	public void delete(BaseEntity entity) {

		getEm().remove(entity);
	}

}
