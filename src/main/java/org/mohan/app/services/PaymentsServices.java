package org.mohan.app.services;

import java.util.ArrayList;
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
import org.mohan.app.dao.GenericDAO;
import org.mohan.app.dao.PaymentsDB;
import org.mohan.app.model.Payments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@Path("payment")
public class PaymentsServices {

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;

	@Autowired
	@Qualifier("paymentsDB")
	PaymentsDB paymentsDB;
	
	@Autowired
	@Qualifier("genericDAO")
	GenericDAO genericDAO;

	@GET
	@Path("metadata")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSongMeta() {
		Payments p = new Payments();
		try {
			@SuppressWarnings("unchecked")
			HashMap songHM = mapper.convertValue(p, HashMap.class);
			return Response.status(200).entity(mapper.writeValueAsString(songHM)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.status(500).build();
	}
	
	// get all
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getTransactions() {
		List<Object> arr = null;
		try {
			arr = genericDAO.getAll(new Payments());
		} catch (GException e1) {
			e1.printStackTrace();
		}
		String paymentString = null;
		try {
			paymentString = mapper.writeValueAsString(arr);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity("Please check the values").build();
		}
		return Response.status(Response.Status.OK).entity(paymentString).build();
	}

	// get by nid, date, id and "nid and date".
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getTransactions(@DefaultValue("nothing") @QueryParam("nid") String nid,
			@DefaultValue("0") @QueryParam("id") int id, @DefaultValue("1963-12-22") @QueryParam("date") String date) {
		List<Payments> list = new ArrayList<Payments>();
		String paymentString = null;
		if (nid.equals("nothing") && date.equals("1963-12-22") && id == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please enter any value to search.").build();
		} else if (nid.equals("nothing") ^ date.equals("1963-12-22")) {
			if (nid.equals("nothing")) {
				list = paymentsDB.getByDate(java.sql.Date.valueOf(date));
				try {
					paymentString = mapper.writeValueAsString(list);
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(Response.Status.BAD_REQUEST).entity("Please check the values").build();
				}
			} else if (date.equals("1963-12-22")) {
				list = paymentsDB.getByCustomerId(nid);
				try {
					paymentString = mapper.writeValueAsString(list);
				} catch (Exception e) {
					e.printStackTrace();
					return Response.status(Response.Status.BAD_REQUEST).entity("Please check the values").build();
				}
			}
		} else if (id == 0) {
			list = paymentsDB.getBetweenCustomerIdAndDate(nid, java.sql.Date.valueOf(date));
			try {
				paymentString = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("Please check the values").build();
			}
		} else {
			Payments obj = new Payments();
			obj.setId(id);
			Payments a = paymentsDB.getByPK(obj);
			list.add(a);
			try {
				paymentString = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("Please check the values").build();
			}
		}
		return Response.status(Response.Status.OK).entity(paymentString).build();
	}

	// get with two dates and nid.
	@GET
	@Path("/date/{date1}/{date2}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPaymentByDatesOrNid(@PathParam("date1") String date1, @PathParam("date2") String date2,
			@DefaultValue("nothing") @QueryParam("nid") String nid) {
		List<Payments> list = null;
		String paymentString = null;
		if (nid.equals("nothing")) {
			
			list = paymentsDB.getBetweenDates(java.sql.Date.valueOf(date1), java.sql.Date.valueOf(date2));
			try {
				paymentString = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(200).entity(paymentString).build();
		} else {
			list = paymentsDB.getBetweenCustomerIdAndDates(nid, java.sql.Date.valueOf(date1), 
					java.sql.Date.valueOf(date2));
			try {
				paymentString = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(200).entity(paymentString).build();
		}
	}

	// Add a Payment
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response createPayment(String payload) {
		Payments p = null;
		try {
			System.out.println(payload);
			p = mapper.readValue(payload, Payments.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			Response.status(400).entity("could not read string").build();
		}
		try {
			genericDAO.add(p);
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(200).entity("Added ").build();
	}

	// Update a Payment
	@POST
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response updatePayment(String payload, @PathParam("id") int id) {
		Payments p = null;
		try {
			p = mapper.readValue(payload, Payments.class);
			p.setId(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			Response.status(400).entity("could not read string").build();
		}
		try {
			genericDAO.update(p);
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(200).build();
	}

	// Delete a Payment
	@DELETE
	@Path("{id}")
	public Response deletePayment(@PathParam("id") int id) {

		Payments obj = new Payments();
		obj.setId(id);
		try {
			genericDAO.delete(obj);
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(200).build();
	}
}
