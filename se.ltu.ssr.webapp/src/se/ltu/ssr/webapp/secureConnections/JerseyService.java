package se.ltu.ssr.webapp.secureConnections;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("/employees")
public class JerseyService 
{
    @RolesAllowed("ADMIN")
    @GET
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllEmployees() 
    {
        Employees list = new Employees();
        list.setEmployeeList(new ArrayList<Employee>());
         
        list.getEmployeeList().add(new Employee(1, "Lokesh Gupta"));
        list.getEmployeeList().add(new Employee(2, "Alex Kolenchiskey"));
        list.getEmployeeList().add(new Employee(3, "David Kameron"));
         System.out.println(list.toString());
        return "ok";
    }
}   