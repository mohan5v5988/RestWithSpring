package org.mohan.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.mohan.app.dao.CustomerDB;
import org.mohan.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("customer")
@Service
public class CustomerServices {

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;

	@Autowired
	@Qualifier("customerDB")
	CustomerDB customerDB;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getAll() {
		List<Customer> c = customerDB.getAll();
		String out = null;
		try {
			out = mapper.writeValueAsString(c);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return out;
	}

	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getCustomer(@DefaultValue("nothing") @QueryParam("nid") String nid,
			@DefaultValue("nothing") @QueryParam("name") String name) {
		String out = null;

		List<Customer> list = null;
		Customer c = null;
		Customer cus = new Customer();
		cus.setNid(nid);
		cus.setName(name);
		if (nid.equals("nothing") && name.equals("nothing")) {
			return null;
		} else if (name.equals("nothing")) {
			c = customerDB.getByPK(cus);
			list = new ArrayList<Customer>();
			list.add(c);
		} else {
			list = customerDB.getByCName(cus);
		}
		try {
			out = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return out;
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public void add(String payload) {
		Customer customer = null;
		try {
			customer = mapper.readValue(payload, Customer.class);
			customerDB.add(customer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@POST
	@Path("{nid}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public void update(String payload, @PathParam("nid") String id) {
		Customer customer = null;
		try {
			customer = mapper.readValue(payload, Customer.class);
			customer.setNid(id);
			customerDB.update(customer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Delete a customer
	@DELETE
	@Path("{nid}")
	public void delete(@PathParam("nid") String nid) {
		Customer customer = new Customer();
		customer.setNid(nid);
		customerDB.delete(customer);
	}
}
