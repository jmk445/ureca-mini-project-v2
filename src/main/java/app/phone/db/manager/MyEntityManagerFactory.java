package app.phone.db.manager;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.jpa.HibernatePersistenceProvider;

import app.phone.db.config.MyPersistenceUnitInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class MyEntityManagerFactory {
	EntityManager em;
	public MyEntityManagerFactory() {
		Map<String, String> props = new HashMap<>();
		props.put("hibernate.show_sql", "true");
		props.put("hibernate.hbm2ddl.auto", "update");
		
		EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(
				new MyPersistenceUnitInfo(),props
				);
		
		this.em = emf.createEntityManager();
	}
	public EntityManager getEm() {
		return em;
	}
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
}
