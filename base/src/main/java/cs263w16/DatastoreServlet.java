package cs263w16;

import javax.servlet.http.*;

import java.io.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        
        PrintWriter respWriter = resp.getWriter();
        
        respWriter.println("<html><head><title>CS263 Assignment 3 - Darshan Maiya</title></head><body>");
        respWriter.println("<h2>CS263 Assignment 3 - Darshan Maiya</h2>");

        String paramKeyName = req.getParameter("keyname");
        String paramValue = req.getParameter("value");
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        
        if(paramKeyName == null && paramValue == null) {
            // List all exisiting entities
            
            String allValuesString = "Entries in Datastore: <br /><br />";
            allValuesString += "<table border><thead><tr><th>Key</th><th>Value</th><th>Timestamp</th></tr></thead><tbody>";
            
            String memCacheTable = "<hr />Entries in Memcache: <br /><br />";
            memCacheTable += "<table border><thead><tr><th>Key</th><th>Value</th><th>Timestamp</th></tr></thead><tbody>";
            
            String valuesString = "";
            String memCacheString = "";
            
            Query q = new Query("TaskData");
            PreparedQuery pq = datastore.prepare(q);
            
            for (Entity result : pq.asIterable()) {
                String keyName = result.getKey().toString();
                keyName = keyName.substring(10, keyName.length()-2);
                
                String value = (String) result.getProperty("value");
                
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String timestamp = df.format(result.getProperty("date"));
                
                valuesString += "<tr><td>" + keyName + "</td><td>" + value + "</td><td>" + timestamp + "</td></tr>";
                
                Entity cacheTask = (Entity) syncCache.get(keyName);
                if (cacheTask != null) {
                    value = (String) cacheTask.getProperty("value");
                    timestamp = df.format(cacheTask.getProperty("date"));
                    memCacheString += "<tr><td>" + keyName + "</td><td>" + value + "</td><td>" + timestamp + "</td></tr>";
                }
            }
            
            if(!valuesString.equals("")) {
                allValuesString += valuesString;
                allValuesString += "</tbody></table>";
            } else {
                allValuesString = "<br />No entities in Datastore.<br />";
            }
            
            if(!memCacheString.equals("")) {
                memCacheTable += memCacheString;
                memCacheTable += "</tbody></table>";
            } else {
                memCacheTable = "<br />No entities in Memcache.<br />";
            }
            
            allValuesString += memCacheTable;
            
            respWriter.println(allValuesString);
        } else if(paramKeyName != null && paramValue == null) {
            // Display stored value
            
            Key taskToRetrieve = KeyFactory.createKey("TaskData", paramKeyName);
            
            Entity storedTask;
            
            String storedIn = "";
            
            try {
                storedTask = datastore.get(taskToRetrieve);
                
                storedIn = "Datastore";
                
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String timestamp = df.format(storedTask.getProperty("date"));
                
                Entity cacheTask = (Entity) syncCache.get(paramKeyName); // Read from cache
                if (cacheTask != null) {
                    storedIn = "Both";
                } else {
                    syncCache.put(paramKeyName, storedTask);
                }
                
                respWriter.println("Value stored for key '" + paramKeyName + "' is: '" + storedTask.getProperty("value") + "' with timestamp: '" + timestamp + "'. Found in <strong>" + storedIn + "</strong>");
            
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                
                respWriter.println("Entity found <strong>Neither</strong> in Datastore nor Memcache.");
            }
        } else if(paramKeyName != null && paramValue != null) {
            // Store value
            
            Entity newTask = new Entity("TaskData", paramKeyName);
            newTask.setProperty("value", paramValue);
            newTask.setProperty("date", Calendar.getInstance().getTime());
             
            datastore.put(newTask);
            syncCache.put(paramKeyName, newTask);
            
            respWriter.println("Stored '" + paramKeyName + "' and '" + paramValue + "' in Datastore.<br />");
            respWriter.println("Stored '" + paramKeyName + "' and '" + paramValue + "' in Memcache.<br />");
        } else {
            // Invalid
            respWriter.println("Invalid parameters passed.");
        }
        
        respWriter.println("</body></html>");
    }
}
