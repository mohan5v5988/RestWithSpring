package org.mohan.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mohan.app.dao.CustomerDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
@Service
public class MyResource {

	
	@Autowired
	@Qualifier("customerDB")
	CustomerDB customerDB;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	if(customerDB == null){
    		System.out.println("null");
    	} else {
    		System.out.println("Working");
    	}
        return "Got it!";
    }
}
