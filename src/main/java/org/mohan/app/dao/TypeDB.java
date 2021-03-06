package org.mohan.app.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mohan.app.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("typeDB")
public class TypeDB {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Type> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Type where Active = ?").setBoolean(0, true);
		List<Type> list = (List<Type>) query.list();
		return list;
	}

	public Type getByPK(Type obj) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Type where type = ? and Active = ?")
							 .setString(0, obj.getType())
							 .setBoolean(1, true);
		Type type = (Type) query.uniqueResult();
		return type;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int delete(Type obj) {
		Session session = sessionFactory.getCurrentSession();
		Type type = (Type) session.get(Type.class, obj.getType());
		type.setActive(false);
		update(type);
		return 0;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int add(Type obj) {
		Session session = sessionFactory.getCurrentSession();
		Type type = (Type) session.get(Type.class, obj.getType());
		if(type == null) {
			session.save(obj);
		} else {
			obj.setActive(true);
			session.flush();
			update(obj);
		}
		return 0;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int update(Type obj) {
		Session session = sessionFactory.getCurrentSession();
		session.clear();
		session.update(obj);
		return 0;
	}
}
