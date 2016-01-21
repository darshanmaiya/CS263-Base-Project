package cs263w16;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;


public class TaskDataResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String keyname;

	private DatastoreService datastore = null;
    
    private MemcacheService syncCache = null;
    
	public TaskDataResource(UriInfo uriInfo, Request request, String kname) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.keyname = kname;
		
		datastore = DatastoreServiceFactory.getDatastoreService();
    	
    	syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}
	
	// for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public TaskData getTaskDataHTML() {
		//add your code here (get Entity from datastore using this.keyname)
		// throw new RuntimeException("Get: TaskData with " + keyname +	" not found");
		//if not found
		Key taskToRetrieve = KeyFactory.createKey("TaskData", keyname);
        
        Entity storedTask;
        
        try {
        	storedTask = (Entity) syncCache.get(keyname); // Read from cache
            if (storedTask == null) {
                storedTask = datastore.get(taskToRetrieve);
                syncCache.put(keyname, storedTask);
            } else {
            	System.out.println(keyname + " retrieved from cache.");
            }

            String value = (String) storedTask.getProperty("value");
            Date timestamp = (Date) storedTask.getProperty("date");
            
            TaskData entity = new TaskData(keyname, value, timestamp);
            
            return entity;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            
            throw new RuntimeException("Get: TaskData with " + keyname + " not found in datastore.");
        }
	}
	
	// for the application
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public TaskData getTaskData() {
		//same code as above method
		return getTaskDataHTML();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putTaskData(String val) {
		Response res = null;
		//add your code here
		//first check if the Entity exists in the datastore
		Key taskToRetrieve = KeyFactory.createKey("TaskData", keyname);
        
        Entity storedTask = null;
        boolean exists = true;
        
        try {
            storedTask = datastore.get(taskToRetrieve);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            
            exists = false;
        }
        
        Entity newTask = new Entity("TaskData", keyname);
        newTask.setProperty("value", val);
        Date date = null;
        if(exists) {
    		//if it is not, create it and 
    		//signal that we created the entity in the datastore
        	date = (Date) storedTask.getProperty("date");
    		res = Response.noContent().build();
        } else {
    		//else signal that we updated the entity
        	date = Calendar.getInstance().getTime();
    		res = Response.created(uriInfo.getAbsolutePath()).build();
        }
        
        newTask.setProperty("date", date);
         
        datastore.put(newTask);
 
		return res;
	}

	@DELETE
	public void deleteIt() {

		//delete an entity from the datastore
		//just print a message upon exception (don't throw)
		Key taskToRetrieve = KeyFactory.createKey("TaskData", keyname);
        
        try {
        	// This will throw an exception if entity doesn't exist
            datastore.get(taskToRetrieve);
            
            datastore.delete(taskToRetrieve);
            syncCache.delete(keyname); // Invalidate the cache
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            
            System.out.println("Delete: TaskData with " + keyname + " not found in datastore.");
        }
	}
}