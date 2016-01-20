package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.io.*;
import java.util.Calendar;
import java.util.logging.Level;

@SuppressWarnings("serial")
public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("keyname");
        String value = request.getParameter("value");
        
        Entity newTask = new Entity("TaskData", key);
        newTask.setProperty("value", value);
        newTask.setProperty("date", Calendar.getInstance().getTime());

        // Store in datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(newTask);
        
        // Store in cache
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        syncCache.put(key, newTask);
    }
}