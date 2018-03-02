package se.ltu.ssr.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/Sources")
public class ListOfSensor {
	MongoDbClientNew mongoDbClient = new MongoDbClientNew("130.240.134.129",27017,"SSRtestDB","RawData");
	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	  public String getSources() {
		//MongoDbClient mongoDbClient = new MongoDbClient("localhost",27017,"SSRtestDB","RawData");
		mongoDbClient.openConnection();
		String json=mongoDbClient.getSensorList();
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			
			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		mongoDbClient.closeConnection();
		return json2.toString();
	  }
	@GET @Path("/{sourceid}")
	@Produces(MediaType.APPLICATION_JSON)
	  public String getSensorLastData(@PathParam("sourceid") String sensorId) {
		//MongoDbClient mongoDbClient = new MongoDbClient("localhost",27017,"SSRtestDB","RawData");
		mongoDbClient.openConnection();
		String json=mongoDbClient.getSensorData(sensorId);
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
		
			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		mongoDbClient.closeConnection();
		return json2.toString();
		
		
	}
	@GET @Path("/{sourceid}/latest")
	@Produces(MediaType.APPLICATION_JSON)
	  public String getSensorLatestData(@PathParam("sourceid") String sensorId) {
		mongoDbClient.openConnection();
		String json=mongoDbClient.getLatestData(sensorId);
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {

			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		mongoDbClient.closeConnection();
		return json2.toString();
		
	}
	@GET @Path("/{sourceid}/latestsmall")
	@Produces(MediaType.APPLICATION_JSON)
	  public String getSensorLatestSmallData(@PathParam("sourceid") String sensorId) {
		mongoDbClient.openConnection();
		String json=mongoDbClient.getLatestData(sensorId);
		JSONObject json2 = null;
		//JSONObject j= new JSONObject();
		String valueofdata="";
		JSONArray result = new JSONArray();
		try {
			json2 = new JSONObject(json);
			JSONArray j2c=(JSONArray) json2.get("Sources");
			JSONObject Sources=(JSONObject) j2c.get(0);
			
			
			JSONObject data=(JSONObject) ((JSONArray)Sources.get("records")).get(0);
			JSONObject j= new JSONObject();
			j.append(data.getString("name"), data.getString("value"));
			result.put(j);
			data=(JSONObject) ((JSONArray)Sources.get("records")).get(1);
			JSONObject j2= new JSONObject();
			j2.append(data.getString("name"), data.getString("value"));
			result.put(j2);
		} catch (JSONException e) {

			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		mongoDbClient.closeConnection();
		return result.toString();
		
	}
	@GET @Path("/{sourceid}/data")
	@Produces(MediaType.APPLICATION_JSON)
	  public String getSensorData(@PathParam("sourceid") String sensorId,@QueryParam("skip") int skip,
				@QueryParam("limit") int limit) {
		
		System.out.println("Skip:"+skip);
		System.out.println("limit:"+limit);
		mongoDbClient.openConnection();
		
		
		String json=mongoDbClient.getSensorData(sensorId,skip,limit);
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {

			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		mongoDbClient.closeConnection();
		return json2.toString();
		
		
	}
}
