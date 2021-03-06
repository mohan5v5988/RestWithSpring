package org.mohan.app.services;

import java.util.HashMap;
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
import javax.ws.rs.core.Response;

import org.moham.app.exception.GException;
import org.mohan.app.dao.CustomerDB;
import org.mohan.app.dao.GenericDAO;
import org.mohan.app.model.Customer;
import org.mohan.app.model.Payments;
import org.mohan.app.model.Transactions;
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

	@Autowired
	@Qualifier("genericDAO")
	GenericDAO genericDAO;
	// get metadata
	@GET
	@Path("metadata")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSongMeta() {
		Customer cus = new Customer();
		try {
			@SuppressWarnings("unchecked")
			HashMap cusHM = mapper.convertValue(cus, HashMap.class);
			// songHM.remove("id");
			return Response.status(200).entity(mapper.writeValueAsString(cusHM)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.status(500).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response browseCustomers(@QueryParam("offset") int offset, @QueryParam("count") int count) {
		List<Object> list;
		String customerString = null;
		try {
			System.out.println(mapper.writeValueAsString(new Payments()));
			System.out.println(mapper.writeValueAsString(new Transactions()));
			list = genericDAO.getAll(new Customer());
			customerString = mapper.writeValueAsString(list);
		} catch(GException ex){
			customerString = "Sorry for the inconvence Please try later.";
		} catch (Exception ex) {
			customerString = "Sorry for the inconvence Please try later.";
		}
		return Response.status(200).entity(customerString).build();
	}

	// get customers by Id or name
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCustomers(@DefaultValue("nothing") @QueryParam("nid") String nid,
			@DefaultValue("nothing") @QueryParam("name") String name) {
		Customer cus = new Customer();
		String customerString = null;
		if (nid.equals("nothing") && name.equals("nothing")) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please enter any value to search.").build();
		} else if (name.equals("nothing")) {
			cus.setNid(nid);
			try {
				customerString = mapper.writeValueAsString(customerDB.getByPK(cus));
//				customerString = mapper.writeValueAsString(genericDAO.getByPK(cus));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(200).entity(customerString).build();
		} else {
			cus.setName(name);
			try {
				customerString = mapper.writeValueAsString(genericDAO.findByExample(cus));
//				customerString = mapper.writeValueAsString(customerDB.getByCName(cus));
			} catch(GException ex){
				customerString = "Sorry for the inconvence Please try later.";
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(200).entity(customerString).build();
		}
	}

	// Add a customer
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response createCustomer(String payload) {
		Customer c = null;
		String i = "";
		try {
			c = mapper.readValue(payload, Customer.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			Response.status(400).entity("could not read string").build();
		}
		try {
			genericDAO.add(c);
		} catch(GException ex){
			
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(201).entity(i).build();
	}

	// Update a song
	@POST
	@Path("{nid}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response updateCustomer(String payload, @PathParam("nid") String nid) {
		Customer t = null;
		try {
			t = mapper.readValue(payload, Customer.class);
			t.setNid(nid);
		} catch (Exception ex) {
			ex.printStackTrace();
			Response.status(400).entity("could not read string").build();
		}
		try {
			genericDAO.update(t);
		} catch(GException ex){
			
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(201).build();
	}

	// Delete a customer
	@DELETE
	@Path("{nid}")
	public Response deleteCustomer(@PathParam("nid") String nid) {
		Customer obj = new Customer();
		obj.setNid(nid);
		try {
			genericDAO.delete(obj);
		} catch(GException ex){
			
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(200).build();
	}
}
