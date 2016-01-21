package cs263w16;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

//Map this class to /ds route
@Path("/ds")
public class DatastoreResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	private DatastoreService datastore = null;
    
    private MemcacheService syncCache = null;

    public DatastoreResource () {
    	datastore = DatastoreServiceFactory.getDatastoreService();
    	
    	syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }
    
	// Return the list of entities to the user in the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<TaskData> getEntitiesBrowser() {
		List<TaskData> list = new ArrayList<TaskData>();
		Query q = new Query("TaskData");
        PreparedQuery pq = datastore.prepare(q);
        int count = 0;
        
        for (Entity result : pq.asIterable()) {
            String keyName = result.getKey().getName();
            
            String value = (String) result.getProperty("value");
            Date timestamp = (Date) result.getProperty("date");
            
            TaskData entity = new TaskData(keyName, value, timestamp);
            list.add(count++, entity);
            
            // Dump it to console if it exists in memcache
            if(syncCache.get(keyName) != null)
            	System.out.println("Key " + keyName + " exists in memcache.");
        }
        
		return list;
	}

	// Return the list of entities to applications
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<TaskData> getEntities() {
		//datastore dump -- only do this if there are a small # of entities
		return getEntitiesBrowser();
	}

	//Add a new entity to the datastore
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTaskData(@FormParam("keyname") String keyname,
			@FormParam("value") String value,
			@Context HttpServletResponse servletResponse) throws IOException {
		
		Entity newTask = new Entity("TaskData", keyname);
        newTask.setProperty("value", value);
        Date date = Calendar.getInstance().getTime();
        newTask.setProperty("date", date);
        
        datastore.put(newTask);
        syncCache.put(keyname, newTask);
        
		System.out.println("Posting new TaskData: " + keyname + " val: "+ value +" ts: " + date);
	}

	//The @PathParam annotation says that keyname can be inserted as parameter after this class's route /ds
	@Path("{keyname}")
	public TaskDataResource getEntity(@PathParam("keyname") String keyname) {
		System.out.println("GETting TaskData for " +keyname);
		return new TaskDataResource(uriInfo, request, keyname);
	}
}