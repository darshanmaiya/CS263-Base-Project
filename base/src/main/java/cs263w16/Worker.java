package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.io.*;
import java.util.Calendar;

@SuppressWarnings("serial")
public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("keyname");
        String value = request.getParameter("value");
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Entity newTask = new Entity("TaskData", key);
        newTask.setProperty("value", value);
        newTask.setProperty("date", Calendar.getInstance().getTime());
         
        datastore.put(newTask);
    }
}