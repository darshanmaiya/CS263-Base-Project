package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import java.io.*;
import javax.ws.rs.client.Entity;

@SuppressWarnings("serial")
public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("keyname");
        String value = request.getParameter("value");
        
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.indexOf(request.getRequestURI()));
        WebTarget service = client.target(url);
        
        Form form =new Form();
        form.param("keyname", key);
        form.param("value", value);
        service.path("rest").path("ds").request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED),Response.class);
    }
}