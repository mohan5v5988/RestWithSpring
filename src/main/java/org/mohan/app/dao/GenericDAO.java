package org.mohan.app.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.moham.app.exception.GException;
import org.moham.app.exception.NotFoundException;
import org.moham.app.exception.PKException;
import org.mohan.app.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("genericDAO")
public class GenericDAO implements IGenericsDB<Object> {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Object> getAll(Object obj) throws GException {
		List<Object> list = null;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(obj.getClass());
			list = criteria.list();
			session.close();
		} catch (Exception ex) {
			throw new GException(
					"This error is from getAll(Object o) in GenericDAO. The Exception Message is "
							+ ex.getMessage());
		}
		if(list.isEmpty() || list == null)
			throw new NotFoundException("from findByExample(Object o) in GenericDAO.", obj);
		return list;
	}

	public List<Object> findByExample(Object obj) throws GException {
		List<Object> list = null;
		try {
			Session session = sessionFactory.openSession();
			Example example = Example.create(obj).excludeZeroes().enableLike();
			Criteria criteria = session.createCriteria(obj.getClass());
			criteria.add(example);
			System.out.println(criteria.toString());
			list = criteria.list();
			session.close();
		} catch (Exception ex) {
			throw new GException(
					"from findByExample(Object o) in GenericDAO. The Exception Message is "
							+ ex.getMessage());
		}
		if(list.isEmpty() || list == null)
			throw new NotFoundException("from findByExample(Object o) in GenericDAO.", obj);
		return list;
	}

	@Override
	public Object getByPK(Object obj) throws GException {
		Object list = null;
		try {
			System.out.println(obj);
			Session session = sessionFactory.openSession();
			Example example = Example.create(obj).excludeZeroes().enableLike();
			Criteria criteria = session.createCriteria(obj.getClass());
			criteria.add(example);
			System.out.println(criteria.toString());
			try{
				list = criteria.uniqueResult();
			} catch(Exception e){
				List<Object> l = criteria.list();
				System.out.println("+++++++++++++++++++++++++++ " + l.size());
				for (Object object : l) {
					System.out.println(object);
				}
			}
			session.close();
		} catch (Exception ex) {
			throw new GException(
					"from findByExample(Object o) in GenericDAO. The Exception Message is "
							+ ex.getMessage());
		}
		if(list == null)
			throw new NotFoundException("from findByExample(Object o) in GenericDAO.", obj);
		return list;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean add(Object obj) throws GException {
		Boolean successful = false;
		Boolean flag = false;
		try {
			Session session = sessionFactory.getCurrentSession();
			if(!("Type".equals(obj.getClass().getName()))){
				flag = true;
			} else {
				Type type = (Type) session.get(Type.class, ((Type) obj).getType());
				if(type == null) flag = true;
				else {
					type.setActive(true);
					session.flush();
					successful = update(type);
				}
			}
			if(flag) {
				session.save(obj);
				session.flush();
				successful = true;
			}
		}catch (org.hibernate.exception.ConstraintViolationException ex) {
			throw new PKException(ex.getMessage());
		}catch(Exception ex) {
			throw new GException(
					"from add(Object obj) in GenericDAO. The Exception Message is "
							+ ex.getMessage());
		}
		return successful;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean update(Object obj) throws GException {
		Boolean successful = false;
		try {
			Session session = sessionFactory.getCurrentSession();
			session.clear();
			session.update(obj);
			successful = true;
		}catch (Exception ex) {
			throw new GException(
					"from add(Object obj) in GenericDAO. The Exception Message is "
							+ ex.getMessage());
		}
		return successful;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean delete(Object obj) throws GException {
		Boolean successful = false;
		Boolean flag = false;
		try {
			Session session = sessionFactory.getCurrentSession();
			if(!("Type".equals(obj.getClass().getName()))){
				flag = true;
			} else {
				Type type = (Type) session.get(Type.class, ((Type) obj).getType());
				if(type == null) flag = true;
				else {
					type.setActive(false);
					session.flush();
					successful = update(type);
				}
			}
			if(flag) {
				session.delete(obj);
				successful = true;
			}
		}catch(Exception ex) {
			throw new GException(
					"from add(Object obj) in GenericDAO. The Exception Message is "
							+ ex.getMessage());
		}
		return successful;
	}
}