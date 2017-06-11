package de.urkallinger.kallingapp.webservice.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.urkallinger.kallingapp.datastructure.DataObject;

public class DatabaseHelper {
	private static final String PERSISTENCE_UNIT_NAME = "kallingapp";
	
	private static DatabaseHelper instance = null;
	private static EntityManagerFactory factory = null;
	
	private static void init() {
		if(instance == null) {
			instance = new DatabaseHelper();
		}
		
		// FIXME ist das sinnvoll?
		if(factory == null) {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		}
	}
	
	public static DatabaseHelper getInstance() {
		init();
		return instance;
	}
	
	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
	public void persist(DataObject<?> dob) {
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.persist(dob);
		em.getTransaction().commit();
	}
	
	public void merge(DataObject<?> dob) {
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		em.merge(dob);
		em.getTransaction().commit();
	}
}
