package org.mohan.app;

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
import javax.ws.rs.core.Response;

import org.mohan.app.dao.TransactionsDB;
import org.mohan.model.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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

	// get all
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getTransactions() {
		List<Transactions> list = transactionsDB.getAll();
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
		try {
			t = mapper.readValue(payload, Transactions.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(400).entity("could not read string").build();
		}
		try {
			transactionsDB.add(t);
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
			transactionsDB.update(t);
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
			transactionsDB.delete(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		return Response.status(200).build();
	}

	// public String getAll() {
	// List<Transactions> transactions = transactionsDB.getAll();
	// String out = null;
	// try {
	// out = mapper.writeValueAsString(transactions);
	// } catch (JsonProcessingException e) {
	// e.printStackTrace();
	// }
	// return out;
	// }
	//
	// public String getByPk() {
	// Transactions t = new Transactions();
	// t.setTid(1);
	// Transactions transactions = transactionsDB.getByPK(t);
	// String out = null;
	// try {
	// out = mapper.writeValueAsString(transactions);
	// } catch (JsonProcessingException e) {
	// e.printStackTrace();
	// }
	// return out;
	// }
}
