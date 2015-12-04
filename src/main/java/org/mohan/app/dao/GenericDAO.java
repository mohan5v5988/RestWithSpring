package org.mohan.app.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.mohan.app.model.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("genericDAO")
public class GenericDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public List<Object> findByExample(Object o){
		Example example = Example.create(o).excludeZeroes().enableLike();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(o.getClass());
		criteria.add(example);
		return criteria.list();
	}
	
}
