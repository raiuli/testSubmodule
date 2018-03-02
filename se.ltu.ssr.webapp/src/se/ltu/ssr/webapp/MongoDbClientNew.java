package se.ltu.ssr.webapp;

import java.util.List;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDbClientNew {
	private String host="";
	private int port=0;
	private String dbName="";
	private String collectionName="";
	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> collection;
	
	public MongoDbClientNew(String host, int port, String dbName, String collectionName) {
		super();
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		this.collectionName = collectionName;
		//mongoClient =new MongoClient(host,port);
	}
	public String getSensorList(){
		//MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			JSONArray jsonArray = new JSONArray();
			//FindIterable<Document> iterable = db.getCollection(collectionName).find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38"))).sort(new BasicDBObject("updateDateTime", 1));
			
			DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
			List listOfSensorIds=dbColl.distinct("SourceID");
			for(int i=0;i<listOfSensorIds.size();i++){
				System.out.println(listOfSensorIds.get(i));
				
					
					//JSONObject jsonObject= new JSONObject(listOfSensorIds.get(i).);
					//System.out.println(jsonObject.toString());
					jsonArray.put(i,listOfSensorIds.get(i));
				
			}
			jsonObjectResult.put("Code", "200");
			jsonObjectResult.put("Sources", jsonArray);
			
			json=jsonObjectResult.toString();
			System.out.println(json);
			
			return json;	
		}catch(Exception e){
			e.printStackTrace();
			try {
				jsonObjectResult.append("Code", "Error");
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
			json=jsonObjectResult.toString();
			return json;
		}
	}
	public String getSensorData(String sensorId){
		
		DB db=mongoClient.getDB(this.dbName);
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			JSONArray jsonArray = new JSONArray();
			//FindIterable<Document> iterable = db.getCollection(collectionName).find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38"))).sort(new BasicDBObject("updateDateTime", 1));
			
			DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
			BasicDBObject bo= new BasicDBObject();
			bo.put("SourceID",sensorId);
			DBCursor cursor=dbColl.find(bo);
			cursor.sort(new BasicDBObject("_id",-1));
			cursor.limit(200);
			while(cursor.hasNext()){
				DBObject d=(DBObject) cursor.next();
				JSONObject jsonObject= new JSONObject(d.toString());
				System.out.println(jsonObject.toString());
				jsonArray.put(jsonObject);
				
				
			}
		
			
			jsonObjectResult.put("Code", "200");
			jsonObjectResult.put("Sources", jsonArray);
			
			json=jsonObjectResult.toString();
			System.out.println(json);
			
			return json;	
		}catch(Exception e){
			e.printStackTrace();
			try {
				jsonObjectResult.append("Code", "Error");
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
			json=jsonObjectResult.toString();
			return json;
		}
		 
		
	}
	public String getLatestData(String sensorId){
		
		DB db=mongoClient.getDB(this.dbName);
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			JSONArray jsonArray = new JSONArray();
			//FindIterable<Document> iterable = db.getCollection(collectionName).find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38"))).sort(new BasicDBObject("updateDateTime", 1));
			
			DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
			BasicDBObject bo= new BasicDBObject();
			bo.put("SourceID",sensorId);
			DBCursor cursor=dbColl.find(bo);
			cursor.sort(new BasicDBObject("_id",-1));
			cursor.limit(1);
			while(cursor.hasNext()){
				DBObject d=(DBObject) cursor.next();
				JSONObject jsonObject= new JSONObject(d.toString());
				System.out.println(jsonObject.toString());
				jsonArray.put(jsonObject);
				
				
			}
		
			
			jsonObjectResult.put("Code", "200");
			jsonObjectResult.put("Sources", jsonArray);
			
			json=jsonObjectResult.toString();
			System.out.println(json);
			
			return json;	
		}catch(Exception e){
			e.printStackTrace();
			try {
				jsonObjectResult.append("Code", "Error");
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
			json=jsonObjectResult.toString();
			return json;
		}
		
		
	}
	public String getSensorData(String sensorId,int skip,int limit){
		
		DB db=mongoClient.getDB(this.dbName);
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			JSONArray jsonArray = new JSONArray();
			//FindIterable<Document> iterable = db.getCollection(collectionName).find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38"))).sort(new BasicDBObject("updateDateTime", 1));
			
			DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
			BasicDBObject bo= new BasicDBObject();
			//bo.put("SourceID",sensorId+"/");
			bo.put("SourceID",sensorId);
			DBCursor cursor=dbColl.find(bo);
			System.out.println(cursor.count());
			long count=cursor.count();
			//cursor.sort(new BasicDBObject("updateDateTime",-1));
			cursor.sort(new BasicDBObject("_id",-1));
			cursor.skip(skip).limit(limit);
			while(cursor.hasNext()){
				DBObject d=(DBObject) cursor.next();
				JSONObject jsonObject= new JSONObject(d.toString());
				System.out.println(jsonObject.toString());
				jsonArray.put(jsonObject);
				
				
			}
			/*for(int i=0;i<listOfSensorIds.size();i++){
				System.out.println(listOfSensorIds.get(i));
				
					
					//JSONObject jsonObject= new JSONObject(listOfSensorIds.get(i).);
					//System.out.println(jsonObject.toString());
					jsonArray.put(i,listOfSensorIds.get(i));
				
			}*/
			/*FindIterable<Document> iterable = db.getCollection(collectionName).distinct("SourceID",null);
			Iterator it=iterable.iterator();
			while(it.hasNext()){
				Document d=(Document) it.next();
				JSONObject jsonObject= new JSONObject(d.toJson().toString());
				System.out.println(jsonObject.toString());
				break;
			}*/
			
			jsonObjectResult.put("TotalCount", count);
			jsonObjectResult.put("Code", "200");
			jsonObjectResult.put("Data", jsonArray);
			
			json=jsonObjectResult.toString();
			//System.out.println(json);
			
			return json;	
		}catch(Exception e){
			e.printStackTrace();
			try {
				jsonObjectResult.append("Code", "Error");
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
			json=jsonObjectResult.toString();
			return json;
		}
	
		
	}
	public boolean openConnection(){
		try {
			mongoClient =new MongoClient(host,port);
			
			return true;

		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean closeConnection(){
		try {
			mongoClient.close(); 
			return true;

		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}
}
