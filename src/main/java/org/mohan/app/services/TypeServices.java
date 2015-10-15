package org.mohan.app.services;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mohan.app.dao.TypeDB;
import org.mohan.app.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("type")
@Service
public class TypeServices {

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;

	@Autowired
	@Qualifier("typeDB")
	TypeDB typeDB;

	@GET
	@Path("metadata")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSongMeta() {
		Type type = new Type();
		try {
			@SuppressWarnings("unchecked")
			HashMap typeHM = mapper.convertValue(type, HashMap.class);
			typeHM.remove("active");
			return Response.status(200).entity(mapper.writeValueAsString(typeHM)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return Response.status(500).build();
	}
	
	// Browse all Types
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response browseTypes(@QueryParam("offset") int offset, @QueryParam("count") int count) {

		List<Type> list = typeDB.getAll();
		String typeString = null;
		try {
			typeString = mapper.writeValueAsString(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(typeString).build();// typeString).build();
	}

	// get types by type
	@GET
	@Path("{type}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getType(@PathParam("type") String type) {
		Type obj = new Type();
		obj.setType(type);
		String typeString = null;
		try {
			typeString = mapper.writeValueAsString(typeDB.getByPK(obj));
			System.out.println(typeString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(typeString).build();
	}

	// Add a type
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response createType(String payload) {
		Type c = null;
		try {
			c = mapper.readValue(payload, Type.class);
			c.setActive(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			Response.status(400).entity("could not read string").build();
		}
		try {
			typeDB.add(c);
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(201).build();
	}

	// Update a type
	@POST
	@Path("{type}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response updateTypes(String payload, @PathParam("type") String type) {
		Type t = null;
		try {
			t = mapper.readValue(payload, Type.class);
			t.setType(type);
		} catch (Exception ex) {
			ex.printStackTrace();
			Response.status(400).entity("could not read string").build();
		}
		try {
			typeDB.update(t);
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(201).build();
	}

	// Delete a type
	@DELETE
	@Path("{type}")
	public Response deleteType(@PathParam("type") String type) {
		Type t = new Type();
		t.setType(type);
		try {
			typeDB.delete(t);
		} catch (Exception e) {
			e.printStackTrace();
			Response.status(500).build();
		}
		return Response.status(200).build();
	}
}
