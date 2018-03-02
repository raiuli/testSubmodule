package se.ltu.ssr.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

@Path("/SensorData")
public class SensorData {
	/*@GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public String sayPlainTextHello() {
		MongoDbClient mongoDbClient = new MongoDbClient("localhost",27017,"SSRtestDB","RawData");
		mongoDbClient.createConnection();
		String json=mongoDbClient.getData();
		//String json ="{\"tutorials\": {\"id\": \"Crunchify\",\"topic\": \"REST Service\",\"description\": \"This is REST Service Example by Crunchify.\"}}";
	    return json;
	  }*/
	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	  public String sayXMLTextHello() {
		MongoDbClient mongoDbClient = new MongoDbClient("localhost",27017,"SSRtestDB","RawData");
		mongoDbClient.createConnection();
		String json=mongoDbClient.getData();
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*String xml = null;
		try {
			xml = XML.toString(json2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//String json ="{\"tutorials\": {\"id\": \"Crunchify\",\"topic\": \"REST Service\",\"description\": \"This is REST Service Example by Crunchify.\"}}";
	    return json2.toString();
	  }
	
	
	@GET @Path("/latestUpdate")
	@Produces(MediaType.APPLICATION_JSON)
	  public String getLatestUpdate() {
		MongoDbClient mongoDbClient = new MongoDbClient("130.240.134.129",27017,"SSRtestDB","RawData");
		mongoDbClient.createConnection();
		String json=mongoDbClient.getLatestData();
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*String xml = null;
		try {
			xml = XML.toString(json2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//String json ="{\"tutorials\": {\"id\": \"Crunchify\",\"topic\": \"REST Service\",\"description\": \"This is REST Service Example by Crunchify.\"}}";
	    return json2.toString();
	  }
	
}
