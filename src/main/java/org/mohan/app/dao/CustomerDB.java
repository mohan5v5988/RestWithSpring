package org.mohan.app.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.moham.app.exception.PKException;
import org.mohan.app.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("customerDB")
public class CustomerDB{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Customer> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Customer");
		List<Customer> customerList = (List<Customer>) query.list();
		return customerList;
	}

	public Customer getByPK(Customer obj) {
		Session session = sessionFactory.openSession();
		Customer customer = (Customer)session.get(Customer.class, obj.getNid());
		return customer;
	}

	public List<Customer> getByCName(Customer obj) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Customer where name = ?")
				.setString(0, obj.getName());
		List<Customer> list = (List<Customer>) query.list();
		return list;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(Customer obj) {
		Session session = sessionFactory.getCurrentSession();
		Customer customer = (Customer) session.get(Customer.class, obj.getNid());
		session.delete(customer);
		return 0;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int add(Customer obj) throws PKException {
		try {
			Session session = sessionFactory.getCurrentSession();
			session.save(obj);
			session.flush();
		}catch (org.hibernate.exception.ConstraintViolationException ex) {
			throw new PKException(ex.getMessage());
		}
		return 0;
		}

	@Transactional(propagation=Propagation.REQUIRED)
	public int update(Customer obj) {
		Session session = sessionFactory.getCurrentSession();
		session.update(obj);
		return 0;
	}
	
//	@Override
//	public int delete(Customer obj) {
//		Session session = sessionFactory.openSession();
//		Customer type = (Customer)session.get(Customer.class, obj.getNid());
//		Transaction transaction = session.getTransaction();
//		transaction.begin();
//		session.delete(type);
//		transaction.commit();
//		session.close();
//		return 0;
//	}
	
	public static void main(String[] args) {
		Customer customer = new Customer("22", "Mohan", "Velaga", "moh@f", new Date(System.currentTimeMillis()), 2039012144);
		Customer customer1 = new Customer("22", "Mohan", "Velaga", "moh@f", new Date(System.currentTimeMillis()), 2039012144);
		System.out.println(customer.equals(customer1));
	}
}
