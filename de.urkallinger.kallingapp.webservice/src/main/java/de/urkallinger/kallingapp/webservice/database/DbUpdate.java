package de.urkallinger.kallingapp.webservice.database;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DbUpdate {
	private Query query;
	private DatabaseHelper db = null;
	private EntityManager em = null;
	
	public DbUpdate() {
		db = DatabaseHelper.getInstance();
		em = db.getEntityManager();
	}
	
	public void beginTransaction() {
		em.getTransaction().begin();
	}
	
	public void rollbackTransation() {
		try {
			em.getTransaction().rollback();
		} finally {
			if(em != null) em.close();
		}
	}

	public void commitTransation() {
		try {
			em.getTransaction().commit();
		} finally {
			if(em != null) em.close();
		}
	}
	
	public DbUpdate query(String queryString) {
		query = em.createQuery(queryString);
		return this;
	}
	
	public DbUpdate addParam(String name, Object value) {
		query.setParameter(name, value);
		return this;
	}
	
	public int executeUpdate() {
		int rowCount = query.executeUpdate();
		return rowCount;
	}
}
