
package se.ltu.ssr.webapp.secureConnections;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/fiwareproxy")
public class FiwareProxy {
	private static final Logger LOGGER = Logger.getLogger(FiwareProxy.class.getName());
	String X_Subject_Token="";
	@Path("/ngsi10/updateContext")
	//@RolesAllowed("ADMIN")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	
    public Response getDataFrmFiwareProxy(String s,@Context HttpServletResponse servletResponse,@Context HttpServletRequest servletRequest) 
    {
		
	    String AuthorizationHeader =servletRequest.getHeader("Authorization");
	    String username="";
		String password="";
		JSONObject jObject = null;
		LOGGER.info("SensorData Before adding sensorDataRecevTime:"+s);
		try {
			s=addDataReceiveEpocTime(s);
			LOGGER.info("SensorData After adding sensorDataRecevTime:"+s);
		} catch (Exception e1) {
			
			e1.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"Error\":\"Something went wrong !!!\"}").build();
		}
	    if(AuthorizationHeader!=null){
	    	//Get encoded username and password
            final String encodedUserPassword = AuthorizationHeader.replaceFirst("Basic" + " ", "");
              
            //Decode username and password
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;
  
            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            username = tokenizer.nextToken();
            password = tokenizer.nextToken();
              
            //Verifying Username and password
            LOGGER.info(username);
            LOGGER.info(password);
            try {
				jObject = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.status(Response.Status.UNAUTHORIZED).entity("{\"Error\":\"Username and password is not present\"}").build();
			}
           
        LOGGER.info("Request Body :"+jObject.toString());
    		Response responseFrmClient= sendRequestToIoTBroker(jObject.toString());
    		LOGGER.info(String.valueOf(responseFrmClient.getStatus()));
    		String response_string=responseFrmClient.readEntity(String.class);
    		LOGGER.info(response_string);
    		if(responseFrmClient.getStatus()!=200){
    			return Response.status(responseFrmClient.getStatus()).entity("{\"Error\":\"Something went wrong !!!\"}").build();
    		}else{
    			/*try {
    				jObject = new JSONObject(response_string);
    				if(jObject.has("errorCode")){
    					return Response.status(Response.Status.OK).entity(jObject.getJSONObject("errorCode").getString("details")).build();
    				}
    				
    			} catch (JSONException e) {
    				
    				e.printStackTrace();
    				return Response.status(Response.Status.BAD_REQUEST).entity("{\"Error\":\"Json Parsing error\"}").build();
    			}*/
    			return Response.status(responseFrmClient.getStatus()).entity(response_string).build();
    		}
	    } else{
			LOGGER.info("uri"+servletRequest.getPathInfo());
			LOGGER.info("body"+s);
			
			
			try {
				jObject = new JSONObject(s);
				JSONObject user=jObject.getJSONObject("auth");
				username=user.getString("username");
				password=user.getString("password");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.status(Response.Status.UNAUTHORIZED).entity("{\"Error\":\"Username and password is not present\"}").build();
			}
			if(!verifyToFiware(username,password)){
				return Response.status(Response.Status.UNAUTHORIZED).entity("{\"Error\":\"Username and password wrong\"}").build();
			}
			jObject.remove("auth");
	    }
		
		LOGGER.info("Request Body after removing auth field:"+jObject.toString());
		Response responseFrmClient=sendRequestToFiware(jObject.toString());
		LOGGER.info(String.valueOf(responseFrmClient.getStatus()));
		String response_string=responseFrmClient.readEntity(String.class);
		LOGGER.info(response_string);
		if(responseFrmClient.getStatus()!=200){
			return Response.status(responseFrmClient.getStatus()).entity("{\"Error\":\"Something went wrong !!!\"}").build();
		}else{
			try {
				jObject = new JSONObject(response_string);
				if(jObject.has("errorCode")){
					return Response.status(Response.Status.BAD_REQUEST).entity(jObject.getJSONObject("errorCode").getString("details")).build();
				}
				
			} catch (JSONException e) {
				
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("{\"Error\":\"Json Parsing error\"}").build();
			}
			return Response.status(responseFrmClient.getStatus()).entity(response_string).build();
		}
		
        //return "ok";
    }
	public String addDataReceiveEpocTime(String content) throws Exception {
		
		//JSONObject jObject = new JSONObject(content);
		
		JSONObject jObject = new JSONObject();
		JSONObject jOuc = new JSONObject(content);
		try {
			jObject.put("name", "sensorDataRecevTime");
			jObject.put("type", "epochtime");
			jObject.put("value", String.valueOf(System.currentTimeMillis()));
			
			JSONObject jomd = new JSONObject();
			jomd.put("name", "sensorDataRecevTime");
			LOGGER.info("Adding sensorDataRecevTime:"+jObject.toString());
			
			JSONArray jArray=(JSONArray) ((JSONObject) ((JSONArray) jOuc.get("contextElements")).get(0)).get("attributes");
			int length=jArray.length();
			//LOGGER.info(String.valueOf(length));
			jArray.put(length, jObject);
			//LOGGER.info("SensorData After adding sensorDataRecevTime:"+jOuc.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jOuc.toString();
		
	}
	public Response sendRequestToFiware(String content){
		LOGGER.info("Forwarding request to PEP proxy------------");
		Client client = ClientBuilder.newClient( new ClientConfig().register( FiwareProxy.class ) );
		//WebTarget webTarget = client.target("http://130.240.134.131/ngsi10").path("updateContext");
		//WebTarget webTarget = client.target("http://130.240.134.128/ngsi10").path("updateContext");
		WebTarget webTarget = client.target("http://130.240.134.128/v1").path("updateContext");
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("X-Subject-Token", X_Subject_Token);
		invocationBuilder.header("X-Auth-Token", X_Subject_Token);
		Response response = invocationBuilder.post(Entity.entity(content, MediaType.APPLICATION_JSON));
		 
		
		return response;
		
	}
	public Response sendRequestToIoTBroker(String content){
		LOGGER.info("Forwarding request to IoTBroker------------");
		Client client = ClientBuilder.newClient( new ClientConfig().register( FiwareProxy.class ) );
		WebTarget webTarget = client.target("http://130.240.134.131/ngsi10").path("updateContext");
		LOGGER.info("Forwarding request to IoTBroker------------");
		//WebTarget webTarget = client.target("http://130.240.134.128/ngsi10").path("updateContext");
		//WebTarget webTarget = client.target("http://130.240.134.128/v1").path("updateContext");
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		//invocationBuilder.header("X-Subject-Token", X_Subject_Token);
		//invocationBuilder.header("X-Auth-Token", X_Subject_Token);
		Response response = invocationBuilder.post(Entity.entity(content, MediaType.APPLICATION_JSON));
		 
		
		return response;
		
	}
	public boolean verifyToFiware(String username,String password){
		LOGGER.info("Verifying with keyrock------------");
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
			LOGGER.info("User:"+user.toString());
			LOGGER.info("password:"+password.toString());
			user.put("name", username);
			user.put("password", password);
			LOGGER.info("jObject:"+jObject.toString());
			content=jObject.toString();
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}

		
		Client client = ClientBuilder.newClient( new ClientConfig().register( FiwareProxy.class ) );
		WebTarget webTarget = client.target("http://130.240.134.128:5000/v3/auth").path("tokens");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(content, MediaType.APPLICATION_JSON));
		String headers= response.getHeaderString("X-Subject-Token");
		LOGGER.info("response:"+response.getStatus()+" X-Subject-Token: "+headers);
		if(response.getStatus()==201){
			
			X_Subject_Token=headers;
			return true;
		}else{
			return false;
		}
		
		
	}
}
