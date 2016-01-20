package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<html><head><title>CS263 Assignment 3 - Darshan Maiya</title></head><body>");
        resp.getWriter().println("<h2>CS263 Assignment 3 - Darshan Maiya</h2>");

        String paramKeyName = req.getParameter("keyname");
        String paramValue = req.getParameter("value");
        
        if(paramKeyName == null && paramValue == null) {
            // List all
            PrintWriter out = resp.getWriter();
            
            out.println("Showing all values stored: <br /><br />");
            
            out.println("<table border><thead><tr><th>Key</th><th>Value</th><th>Created At</th></tr></thead><tbody>");
            
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Query q = new Query("TaskData");
            PreparedQuery pq = datastore.prepare(q);  
            for (Entity result : pq.asIterable()) {
                String keyName = result.getKey().toString();
                keyName = keyName.substring(10, keyName.length()-2);
                
                String value = (String) result.getProperty("value");
                
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String timestamp = df.format(result.getProperty("date"));
                
                out.println("<tr><td>" + keyName + "</td><td>" + value + "</td><td>" + timestamp + "</td></tr>"); 
            }
            
            out.println("</tbody></table>");
            
        } else if(paramKeyName != null && paramValue == null) {
            // Display stored value
            Key taskToRetrieve = KeyFactory.createKey("TaskData", paramKeyName);
            
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Entity storedTask;
            
            try {
                storedTask = datastore.get(taskToRetrieve);
                
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String timestamp = df.format(storedTask.getProperty("date"));
                
                resp.getWriter().println("Value stored for key '" + paramKeyName + "' is: '" + storedTask.getProperty("value") + "' with timestamp: '" + timestamp + "'");
            
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                
                resp.getWriter().println("ERROR: No value stored for key:'" + paramKeyName + "'");
            }
        } else if(paramKeyName != null && paramValue != null) {
            // Store value
            Entity newTask = new Entity("TaskData", paramKeyName);
            newTask.setProperty("value", paramValue);
            newTask.setProperty("date", Calendar.getInstance().getTime());
             
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(newTask);
            
            resp.getWriter().println("Stored '" + paramKeyName + "' and '" + paramValue + "' in Datastore");
        } else {
            // Invalid
            resp.getWriter().println("Invalid parameters passed.");
        }
        
        /*Key bobKey = KeyFactory.createKey("Person", "Bob");
        Entity bob = new Entity(bobKey);
        bob.setProperty("gender", "male");
        bob.setProperty("age", "23");*/
        
        //datastore.put(bob);
        
        /*Key bobKey = KeyFactory.createKey("Person", "Bob");
        Key aliceKey = KeyFactory.createKey("Person", "Alice");
         
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity alice, bob;
        
        try {
            alice = datastore.get(aliceKey);
            bob = datastore.get(bobKey);
         
            String aliceAge = String.valueOf(alice.getProperty("age"));
            String bobAge = String.valueOf(bob.getProperty("age"));
            System.out.println("Alice's age: " + aliceAge);
            System.out.println("Bob's age: " + bobAge);
            
            resp.getWriter().println("Alice’s age: " + aliceAge + "<br />" + "Bob’s age: " + bobAge);
        
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            
            resp.getWriter().println("Alice and/or Bob not found.");
        }*/
        
        resp.getWriter().println("</body></html>");
    }
}
