package se.ltu.ssr.webapp;


import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters.*;


public class MongoDbClient {
	//Logger log=Logger.getLogger(MongoDBClient.class);
	private String host="";
	private int port=0;
	private String dbName="";
	private String collectionName="";
	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> collection;
	
	public MongoDbClient(String host, int port, String dbName, String collectionName) {
		super();
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		this.collectionName = collectionName;
	}

	public boolean createConnection(){
		try {
			mongoClient =new MongoClient(host,port);
			db= mongoClient.getDatabase(dbName);
			collection =  db.getCollection(collectionName);

			/*// convert JSON to DBObject directly
			DBObject dbObject = (DBObject) JSON
					.parse("{'name':'mkyong', 'age':30}");

			collection.insert(dbObject);

			DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
			}

			System.out.println("Done");*/
			return true;

		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean closeConnection(){
		try {
			mongoClient.close(); 
			/*// convert JSON to DBObject directly
			DBObject dbObject = (DBObject) JSON
					.parse("{'name':'mkyong', 'age':30}");

			collection.insert(dbObject);

			DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				System.out.println(cursorDoc.next());
			}

			System.out.println("Done");*/
			return true;

		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}
	public String getLatestData(){
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			JSONArray jsonArray = new JSONArray();
			//FindIterable<Document> iterable = db.getCollection(collectionName).find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38"))).sort(new BasicDBObject("updateDateTime", 1));
			
			DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
			List listOfSensorIds=dbColl.distinct("SourceID");
			for(int i=0;i<listOfSensorIds.size();i++){
				
				System.out.println("dbColl.count()"+dbColl.getCount());
				System.out.println(listOfSensorIds.get(i));
				dbColl=(DBCollection) db.getCollection(collectionName);
				//DBCursor car = dbColl.find(new BasicDBObject("SourceID", new BasicDBObject("$eq", listOfSensorIds.get(i)))).sort(new BasicDBObject("updateDateTime", -1)).limit(1);
				DBCursor car = dbColl.find(new BasicDBObject("SourceID", new BasicDBObject("$eq", listOfSensorIds.get(i))));
				System.out.println("car.count()"+car.count());
				car.sort(new BasicDBObject("_id",-1));
				car.limit(1);
				while(car.hasNext()){
					//System.out.println(car.next());
					DBObject d=(DBObject) car.next();
					JSONObject jsonObject= new JSONObject(d.toString());
					System.out.println(jsonObject.toString());
					jsonArray.put(jsonObject);
				}
			}
			/*FindIterable<Document> iterable = db.getCollection(collectionName).distinct("SourceID",null);
			Iterator it=iterable.iterator();
			while(it.hasNext()){
				Document d=(Document) it.next();
				JSONObject jsonObject= new JSONObject(d.toJson().toString());
				System.out.println(jsonObject.toString());
				break;
			}*/
			
			
			jsonObjectResult.append("Code", "200");
			jsonObjectResult.append("Data", jsonArray);
			
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
	public String getOpenIotLatestData(){
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			JSONArray jsonArray = new JSONArray();
			//FindIterable<Document> iterable = db.getCollection(collectionName).find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38"))).sort(new BasicDBObject("updateDateTime", 1));
			
			//DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
			//List listOfSensorIds=dbColl.distinct("SourceID");
			//for(int i=0;i<listOfSensorIds.size();i++){
				
				//System.out.println("dbColl.count()"+dbColl.getCount());
				//System.out.println(listOfSensorIds.get(i));
				DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
				//DBCursor car = dbColl.find(new BasicDBObject("SourceID", new BasicDBObject("$eq", listOfSensorIds.get(i)))).sort(new BasicDBObject("updateDateTime", -1)).limit(1);
				
				BasicDBObject fields = new BasicDBObject();
			    fields.put("SourceID", 1);
				DBCursor car = dbColl.find(new BasicDBObject("SourceID", new BasicDBObject("$eq", "WasteWaterSensor_38")),fields);
				System.out.println("car.count()"+car.count());
				car.sort(new BasicDBObject("_id",-1));
				car.limit(1);
				JSONObject jsonObject = null;
				while(car.hasNext()){
					//System.out.println(car.next());
					DBObject d=(DBObject) car.next();
					 jsonObject= new JSONObject(d.toString());
					System.out.println(jsonObject.toString());
					jsonArray.put(jsonObject);
				}
			//}
			/*FindIterable<Document> iterable = db.getCollection(collectionName).distinct("SourceID",null);
			Iterator it=iterable.iterator();
			while(it.hasNext()){
				Document d=(Document) it.next();
				JSONObject jsonObject= new JSONObject(d.toJson().toString());
				System.out.println(jsonObject.toString());
				break;
			}*/
			
			
			jsonObjectResult.append("Code", "200");
			jsonObjectResult.append("Data", jsonObject);
			
			json=jsonObjectResult.toString();
			System.out.println(json);
			mongoClient.close();
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
	public String getSensorList(){
		MongoClient mongoClient =new MongoClient(host,port);
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
			/*FindIterable<Document> iterable = db.getCollection(collectionName).distinct("SourceID",null);
			Iterator it=iterable.iterator();
			while(it.hasNext()){
				Document d=(Document) it.next();
				JSONObject jsonObject= new JSONObject(d.toJson().toString());
				System.out.println(jsonObject.toString());
				break;
			}*/
			
			
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
	public String getData(){
		//String json = "{\"tutorials\": {\"id\": \"Crunchify\",\"topic\": \"REST Service\",\"description\": \"This is REST Service Example by Crunchify.\"}}";;
		String json ="";
		JSONObject jsonObjectResult= new JSONObject();
		try{
			final JSONArray jsonArray = new JSONArray();
			
			FindIterable<Document> iterable = db.getCollection(collectionName).find().sort(new BasicDBObject("updateDateTime", 1));
			
			
			iterable.forEach(new Block<Document>() {
			    public void apply(final Document document) {
			        System.out.println(document);
			        try {
						JSONObject jsonObject= new JSONObject(document.toJson().toString());
						jsonArray.put(jsonObject);
						
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
			    }
			});
			
			jsonObjectResult.append("Code", "200");
			jsonObjectResult.append("Data", jsonArray);
			
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
		MongoClient mongoClient =new MongoClient(host,port);
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
			cursor.sort(new BasicDBObject("updateDateTime",-1));
			cursor.limit(200);
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


}
