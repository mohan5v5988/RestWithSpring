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
import org.mohan.app.dao.TransactionsDB;
import org.mohan.app.model.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Path("transaction")
public class TransactionsServices {
	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;

	@Autowired
	@Qualifier("transactionsDB")
	TransactionsDB transactionsDB;
	
	@Autowired
	@Qualifier("genericDAO")
	GenericDAO genericDAO;

	@GET
	@Path("metadata")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSongMeta() {
		Transactions t = new Transactions();
		try {
			@SuppressWarnings("unchecked")
			HashMap tranHM = mapper.convertValue(t, HashMap.class);
//			HashMap<String, String> aa = new HashMap<String, String>();
//			aa.put("date", null);
//			aa.put("Nid", null);
//			aa.put("Type", null);
//			aa.put("rate", null);
//			System.out.println(aa);
			return Response.status(200).entity(mapper.writeValueAsString(tranHM)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.status(500).build();
	}
	
	// get all
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getTransactions() {
		List<Object> list = null;
		try {
			list = genericDAO.getAll(new Transactions());
		} catch (GException e1) {
			e1.printStackTrace();
		}
		try {
			return Response.status(200).entity(mapper.writeValueAsString(list)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity("Please enter any value to search.").build();
		}
	}

	// get Transaction by tid , nid ,date, "date and nid".
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getTransactions(@DefaultValue("nothing") @QueryParam("nid") String nid,
			@DefaultValue("0") @QueryParam("tid") int tid,
			@DefaultValue("1963-12-22") @QueryParam("date") String date) {

		if (nid.equals("nothing") && date.equals("1963-12-22") && tid == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please enter any value to search.").build();
		} else if (nid.equals("nothing") ^ date.equals("1963-12-22")) {
			if (nid.equals("nothing")) {
				List<Transactions> list = transactionsDB.getByDate(java.sql.Date.valueOf(date));
				try {
					return Response.status(200).entity(mapper.writeValueAsString(list)).build();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (date.equals("1963-12-22")) {
				List<Transactions> list = transactionsDB.getByCustomerId(nid);
				try {
					return Response.status(200).entity(mapper.writeValueAsString(list)).build();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (tid == 0) {
			List<Transactions> list = transactionsDB.getBetweenCustomerIdAndDate(nid, java.sql.Date.valueOf(date));
			try {
				return Response.status(200).entity(mapper.writeValueAsString(list)).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			List<Transactions> list = new ArrayList<Transactions>();
			Transactions obj = new Transactions();
			obj.setTid(tid);
			Transactions a = transactionsDB.getByPK(obj);
			list.add(a);
			try {
				return Response.status(200).entity(mapper.writeValueAsString(list)).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Please check the request.").build();
	}

	// get with two dates and nid.
	@GET
	@Path("/date/{date1}/{date2}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPaymentByDatesOrNid(@PathParam("date1") String date1, @PathParam("date2") String date2,
			@DefaultValue("nothing") @QueryParam("nid") String nid) {

		List<Transactions> list = null;
		String tranasactionString = null;
		if (nid.equals("nothing")) {
			list = transactionsDB.getBetweenDates(java.sql.Date.valueOf(date1), java.sql.Date.valueOf(date2));
			try {
				tranasactionString = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
				tranasactionString = "Please enter a valide dates";
			}
			return Response.status(200).entity(tranasactionString).build();
		} else {
			list = transactionsDB.getBetweenCustomerIdAndDates(nid, java.sql.Date.valueOf(date1),
					java.sql.Date.valueOf(date2));
			try {
				tranasactionString = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
				tranasactionString = "Please enter a valide dates";
			}
			return Response.status(200).entity(tranasactionString).build();
		}
	}

	// Add a Tranasaction
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response createTransaction(String payload) {
		Transactions t = null;
		String i = "";
		System.out.println(payload);
		try {
			t = mapper.readValue(payload, Transactions.class);
			t.getCalculation().cal();
			System.out.println(t);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(400).entity("could not read string").build();
		}
		try {
			genericDAO.add(t);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		return Response.status(200).entity(i).build();
	}

	// Update a Transaction
	@POST
	@Path("/update/{tid}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response updateTransaction(String payload, @PathParam("tid") int tid) {

		Transactions t = null;
		try {
			t = mapper.readValue(payload, Transactions.class);
			t.setTid(tid);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(400).entity("could not read string").build();
		}
		try {
			genericDAO.update(t);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		return Response.status(200).build();
	}

	// Delete a transaction
	@DELETE
	@Path("{tid}")
	public Response deleteTransaction(@PathParam("tid") int tid) {
		Transactions obj = new Transactions();
		obj.setTid(tid);
		try {
			genericDAO.delete(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		return Response.status(200).build();
	}
}
