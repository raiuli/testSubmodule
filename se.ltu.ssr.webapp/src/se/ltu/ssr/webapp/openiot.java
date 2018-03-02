package se.ltu.ssr.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/openiot")
public class openiot {
	@GET @Path("/latestUpdate")
	@Produces(MediaType.APPLICATION_JSON)
	  public String getLatestUpdate() {
		System.out.println("I am here");
		MongoDbClient mongoDbClient = new MongoDbClient("130.240.134.129",27017,"SSRtestDB","RawData");
		//mongoDbClient.createConnection();
		String json=mongoDbClient.getOpenIotLatestData();
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		//mongoDbClient.closeConnection();
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
