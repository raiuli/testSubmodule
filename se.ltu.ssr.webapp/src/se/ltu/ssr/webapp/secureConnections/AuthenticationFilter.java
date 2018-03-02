/*package se.ltu.ssr.webapp.secureConnections;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;
@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter{
	@Context
    private ResourceInfo resourceInfo;
	@Context
	private transient HttpServletRequest servletRequest; 
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
                                                        .entity("You cannot access this resource").build();
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
                                                        .entity("Access blocked for all users !!").build();
  
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("Inside authentication checking");
		
		 Method method = resourceInfo.getResourceMethod();
	        //Access allowed for all
	        if( ! method.isAnnotationPresent(PermitAll.class))
	        {
	            //Access denied for all
	            if(method.isAnnotationPresent(DenyAll.class))
	            {
	                requestContext.abortWith(ACCESS_FORBIDDEN);
	                return;
	            }
	              
	            //Get request headers
	            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
	              
	            //Fetch authorization header
	            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
	              
	            //If no authorization information present; block access
	            if(authorization == null || authorization.isEmpty())
	            {
	            	System.out.println("Inside authentication checking");	
	            	requestContext.abortWith(ACCESS_DENIED);
	                return;
	            }
	              
	            //Get encoded username and password
	            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
	              
	            //Decode username and password
	            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;
	  
	            //Split username and password tokens
	            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
	            final String username = tokenizer.nextToken();
	            final String password = tokenizer.nextToken();
	              
	            //Verifying Username and password
	            System.out.println(username);
	            System.out.println(password);
	              
	            //Verify user access
	            if(method.isAnnotationPresent(RolesAllowed.class))
	            {
	                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
	                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
	                  
	                //Is user valid?
	                if( ! isUserAllowed(username, password, rolesSet))
	                {
	                    requestContext.abortWith(ACCESS_DENIED);
	                    return;
	                }else{
	                	this.servletRequest.setAttribute("Token", "token");
	                }
	            }
	        }
		
	}
	private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet)
    {
        boolean isAllowed = false;
          
        //Step 1. Fetch password from database and match with password in argument
        //If both match then get the defined role for user from database and continue; else return isAllowed [false]
        //Access the database and do this part yourself
        //String userRole = userMgr.getUserRole(username);
         System.out.println("username: "+username);
         System.out.println("password: "+password);
         
        if(username.equals("aquaductus") && password.equals("passw0rd@2016"))
        {
            String userRole = "ADMIN";
             
            //Step 2. Verify user role
            if(rolesSet.contains(userRole))
            {
                isAllowed = true;
            }
        }else{
        	if(verifyToFiware(username,password)){
        		String userRole = "ADMIN";
                
                //Step 2. Verify user role
                if(rolesSet.contains(userRole))
                {
                    isAllowed = true;
                }
        	}
        }
        return isAllowed;
    }
	public boolean verifyToFiware(String username,String password){
		System.out.println("Verifying with keyrock------------");
		String content= "{ \"auth\": {"
		    +"\"identity\": {"
		    +"\"methods\": [\"password\"],"
		     +" \"password\": {"
		     +"   \"user\": {"
		     +"    \"name\": \"iot_sensor_0dd19cb16ccd4d939da8210c3e422c3f\","
		     +"   \"domain\": { \"id\": \"default\" },"
		     +"   \"password\": \"ccba2e1d5b594843829774af674824e8\""
		     +"  }"
		     +" }"
		     +"}"
		     +"}"
		     +"}";
		try {
			JSONObject jObject  = new JSONObject(content);
			JSONObject user=jObject.getJSONObject("auth").getJSONObject("identity").getJSONObject("password").getJSONObject("user");
			System.out.println("User:"+user.toString());
			System.out.println("password:"+password.toString());
			user.put("name", username);
			user.put("password", password);
			System.out.println("jObject:"+jObject.toString());
			content=jObject.toString();
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}

		
		Client client = ClientBuilder.newClient( new ClientConfig().register( AuthenticationFilter.class ) );
		WebTarget webTarget = client.target("http://130.240.134.128:5000/v3/auth").path("tokens");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(content, MediaType.APPLICATION_JSON));
		String headers= response.getHeaderString("X-Subject-Token");
		System.out.println("response:"+response.getStatus()+" X-Subject-Token: "+headers);
		if(response.getStatus()==201){
			this.servletRequest.setAttribute("X-Subject-Token", headers);
			return true;
		}else{
			return false;
		}
		
		
	}
}
*/