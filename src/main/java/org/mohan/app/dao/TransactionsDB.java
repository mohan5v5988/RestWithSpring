package org.mohan.app.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.mohan.app.model.Customer;
import org.mohan.app.model.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("transactionsDB")
public class TransactionsDB implements IGenericsDB<Transactions> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Transactions> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Transactions");
		List<Transactions> transactionsList = (List<Transactions>) query.list();
		return transactionsList;
	}

	@Override
	public Transactions getByPK(Transactions obj) {
		Session session = sessionFactory.openSession();
		Transactions transactions = (Transactions)session.get(Transactions.class, obj.getTid());
		return transactions;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(Transactions obj) {
		Session session = sessionFactory.getCurrentSession();
		Transactions transactions = (Transactions) session.get(Transactions.class, obj.getTid());
		session.delete(transactions);
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int add(Transactions obj) {
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int update(Transactions obj) {
		Session session = sessionFactory.getCurrentSession();
		session.update(obj);
		return 0;
	}
	
	public List<Transactions> getByCustomerId(String id) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("customer.nid", id));
		List<Transactions> transactionsList = (List<Transactions>) criteria.list();
		System.out.println(transactionsList.isEmpty());
		return transactionsList;
	}
	
	public List<Transactions> getBetweenCustomerIdAndDate(String id,Date d) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("customer.nid", id))
				.add(Restrictions.eq("date", d));
		List<Transactions> transactionsList = (List<Transactions>) criteria.list();
		return transactionsList;
	}
	
	public List<Transactions> getBetweenCustomerIdAndDates(String id,Date d1,Date d2) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("customer.nid", id))
				.add(Restrictions.between("date", d1, d2));
		List<Transactions> transactionsList = (List<Transactions>) criteria.list();
		System.out.println(transactionsList.isEmpty());
		return transactionsList;
	}
	
	public List<Transactions> getByDate(Date d1) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Transactions.class);
		criteria.add(Restrictions.eq("date", d1));
		List<Transactions> transactionsList = (List<Transactions>) criteria.list();
		return transactionsList;
	}
	
	public List<Transactions> getBetweenDates(Date d1,Date d2) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Transactions.class);
		criteria.add(Restrictions.between("date", d1, d2));
		List<Transactions> transactionsList = (List<Transactions>) criteria.list();
		return transactionsList;
	}
}
