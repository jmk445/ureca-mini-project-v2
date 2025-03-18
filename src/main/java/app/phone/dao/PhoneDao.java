package app.phone.dao;


import java.sql.SQLException;
import java.util.List;

import app.phone.db.manager.MyEntityManagerFactory;
import app.phone.dto.Phone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
public class PhoneDao {
	
	private EntityManager manager;
	public PhoneDao() {
		MyEntityManagerFactory factory = new MyEntityManagerFactory();
		this.manager = factory.getEm(); 
		
	}	

	
	
	public Phone detailPhone(int phoneId) {
		String jpql = "select p from Phone p where phoneId = ?1";
		TypedQuery<Phone> query = manager.createQuery(jpql,Phone.class);
		query.setParameter(1,phoneId);
		Phone p = query.getSingleResult();
		return p;
	}
	
	public List<Phone> listPhone(){
		String jpql = "select p from Phone p";
		TypedQuery<Phone> query = manager.createQuery(jpql,Phone.class);
		List<Phone> list = query.getResultList();
		return list;
	}
	
	public int updatePhone(Phone phone) {		
		manager.getTransaction().begin();
		
		manager.merge(phone);	
							

		manager.getTransaction().commit(); //(이 시점에 테이블에 반영한다.)

					
		//언제 close
		return 0;
	}
	
	public int insertPhone(Phone phone) {
		manager.getTransaction().begin();
		
		manager.merge(phone);	
						

		manager.getTransaction().commit(); //(이 시점에 테이블에 반영한다.)

					
		//언제 close
		return 0;
	}
	
	
}
