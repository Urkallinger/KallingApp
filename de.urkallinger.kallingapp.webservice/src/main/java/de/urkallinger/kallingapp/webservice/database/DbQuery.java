package de.urkallinger.kallingapp.webservice.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DbQuery {
	private Query query;
	private DatabaseHelper db = null;
	private EntityManager em = null;
	
	public DbQuery(String queryString) {
		db = DatabaseHelper.getInstance();
		em = db.getEntityManager();
		query = em.createQuery(queryString); 
	}
	
	public DbQuery addParam(String name, Object value) {
		query.setParameter(name, value);
		return this;
	}
	
	public Object getSingleResult() {
		try {
			Object result = query.getSingleResult();
			return result;
		} finally {
			if(em != null) em.close();
		}
	}
	
	public List<?> getResultList() {
		try {
			List<?> list = query.getResultList();
			return list;
		} finally {
			if(em != null) em.close();
		}
	}
}
