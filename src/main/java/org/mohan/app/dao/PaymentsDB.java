package org.mohan.app.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.mohan.app.model.Payments;
import org.mohan.app.model.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("paymentsDB")
public class PaymentsDB  implements IGenericsDB<Payments> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Payments> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Payments");
		List<Payments> paymentsList = (List<Payments>) query.list();
		return paymentsList;
	}

	@Override
	public Payments getByPK(Payments obj) {
		Session session = sessionFactory.openSession();
		Payments payments = (Payments)session.get(Payments.class, obj.getId());
		return payments;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(Payments obj) {
		Session session = sessionFactory.getCurrentSession();
		Payments payments = (Payments) session.get(Payments.class, obj.getId());
		session.delete(payments);
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int add(Payments obj) {
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
		return 0;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int update(Payments obj) {
		Session session = sessionFactory.getCurrentSession();
		session.update(obj);
		return 0;
	}
	
	public List<Payments> getByCustomerId(String nid) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Payments.class);
		criteria.add(Restrictions.eq("customer.nid", nid));
		List<Payments> paymentsList = (List<Payments>) criteria.list();
		return paymentsList;
	}
	
	public List<Payments> getBetweenCustomerIdAndDate(String nid,Date d) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Payments.class);
		criteria.add(Restrictions.eq("customer.nid", nid))
				.add(Restrictions.eq("date", d));
		List<Payments> paymentsList = (List<Payments>) criteria.list();
		return paymentsList;
	}
	
	public List<Payments> getBetweenCustomerIdAndDates(String nid,Date d1,Date d2) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Payments.class);
		criteria.add(Restrictions.eq("customer.nid", nid))
				.add(Restrictions.between("date", d1, d2));
		List<Payments> paymentsList = (List<Payments>) criteria.list();
		return paymentsList;
	}
	public List<Payments> getByDate(Date d1) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Payments.class);
		criteria.add(Restrictions.eq("date", d1));
		List<Payments> paymentsList = (List<Payments>) criteria.list();
		return paymentsList;
	}
	public List<Payments> getBetweenDates(Date d1,Date d2) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Payments.class);
		criteria.add(Restrictions.between("date", d1, d2));
		List<Payments> paymentsList = (List<Payments>) criteria.list();
		return paymentsList;
	}
}
