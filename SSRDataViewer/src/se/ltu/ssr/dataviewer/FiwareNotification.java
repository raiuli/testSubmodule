package se.ltu.ssr.dataviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;

@WebServlet("/notify")
public class FiwareNotification  extends HttpServlet{
	private String IDTAGS="Enviormental Label";
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getParameterNames());
		
		 StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  System.out.println(jb);
		  
		  try {
			insertData(jb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
}
	private boolean insertData(String data_string) throws Exception{
		//"130.240.134.129",27017,"SSRtestDB","RawData"
		JSONObject jsonObject= new JSONObject(data_string.toString());
		JSONArray contextResponses=jsonObject.getJSONArray("contextResponses");
		JSONObject contextElement=((JSONObject) contextResponses.get(0)).getJSONObject("contextElement");
		String sourceId=contextElement.getString("id");
		
		SSRData data = new SSRData();
    	data.setiDTags(IDTAGS);
    	data.setTypesOfData(TypesOfData.TOURIST_DATA);
    	data.setDescription("AquaDuctus Enviormental data Sensor");
    	data.setSourceID(sourceId);
    	data.setSourceType("EnviormentalSensor");
    	data.setUpdateDateTime(new Date());
    	data.setDataQueryDateTime(new Date());
    	JSONArray attributes=contextElement.getJSONArray("attributes");
    	ArrayList<Record> recordList = new ArrayList<Record>();
    	for(int i=0;i<attributes.length();i++){
    		
    		String name=((JSONObject) attributes.get(i)).getString("name");
    		if(name.equals("sensorDataRecevTime")) {
    			
    		} else {
	    		String value=((JSONObject) attributes.get(i)).getString("value");
	    		JSONArray metadatas=((JSONObject) attributes.get(i)).getJSONArray("metadatas");
	    		String unit=((JSONObject) metadatas.get(0)).getString("value");
	    		Record record01= new Record(name, value, unit, "");
	    		recordList.add(record01);
    		}
    	}
    	SSRGeoLocation geolocation= new SSRGeoLocation();
    	geolocation.setLatitude(0.0);
    	geolocation.setLongitude(0.0);
    	data.setGeolocation(geolocation);
    	data.setRecords(recordList);
    	
    	
		
		MongoClient mongoClient =new MongoClient("localhost",27017);
		MongoDatabase db= mongoClient.getDatabase("SSRtestDB");
		MongoCollection<Document> collection =  db.getCollection("Fiware");
		try{
			Gson gson = new Gson();
			String json = gson.toJson(data);
			System.out.println(json);
			Document doc = new Document().parse(json);
			//doc.append("Date", new Date());
			collection.insertOne(doc);
			mongoClient.close();
			return true;	
		}catch(Exception e){
			e.printStackTrace();
			mongoClient.close();
			return false;
		}
		
	}	
		
}
