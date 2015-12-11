package org.mohan.app.model;

import java.util.Set;
import java.util.List;
import java.util.HashMap;



import org.mohan.app.dao.GenericDAO;
import org.moham.app.exception.GException;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
@Scope("singleton")
public class SeedData {

	@Autowired
	@Qualifier("genericDAO")
	GenericDAO genericDAO;

	HashMap<String, Customer> h_Customer = new HashMap<String, Customer>();
	HashMap<String, Type> h_Type = new HashMap<String, Type>();

	public SeedData() {
		try {
			loadCustomers();
			loadType();
		} catch (GException ex) {
			ex.printStackTrace();
		}
	}

	void loadCustomers() throws GException {
		List<Object> list = genericDAO.getAll(new Customer());
		for (Object object : list) {
			Customer c = (Customer) object;
			h_Customer.put(c.getNid(), c);
		}
	}

	void loadType() throws GException {
		List<Object> list = genericDAO.getAll(new Type());
		for (Object object : list) {
			Type t = (Type) object;
			h_Type.put(t.getType(), t);
		}
	}
	
	public Customer getCustomer(String key) {
		return h_Customer.get(key) != null ? h_Customer.get(key) : null;
	}
	
	public Type getType(String key){
		return h_Type.get(key) != null ? h_Type.get(key) : null;
	}
	
	public Set<String> getCustomerKeys() {
		return h_Customer.keySet();
	}
	
	public Set<String> getTypeKeys() {
		return h_Type.keySet();
	}
}