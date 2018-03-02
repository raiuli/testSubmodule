package se.ltu.ssr.dataviewer;

import java.util.List;
import java.util.logging.Logger;

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



public class CygnusConnector {
	private String host="";
	private int port=0;
	private String dbName="";
	private String collectionName="";
	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> collection;
	private static final Logger LOGGER = Logger.getLogger(CygnusConnector.class.getName());
	public CygnusConnector(String host, int port, String dbName, String collectionName) {
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


			return true;

		} catch (MongoException e) {
			e.printStackTrace();
		}
		return false;
	}
	public String getAiiflowData(int skip, int limit) throws Exception{
		JSONObject jsonObjectResult= new JSONObject();
		JSONArray jsonArray = new JSONArray();
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
		
		
	
		DBCursor cursor=dbColl.find();
		
		System.out.println(cursor.count());
		long count=cursor.count();
		//cursor.sort(new BasicDBObject("updateDateTime",-1));
		cursor.sort(new BasicDBObject("_id",-1));
		cursor.skip(skip).limit(limit);
		while(cursor.hasNext()){
			DBObject d=(DBObject) cursor.next();
			JSONObject jsonObject= new JSONObject(d.toString());
			//System.out.println(jsonObject.toString());
			jsonArray.put(jsonObject);
			
			
		}
		
		//System.out.println(jsonArray.toString());
		jsonObjectResult.put("TotalCount", count);
		jsonObjectResult.put("Code", "200");
		jsonObjectResult.put("Data", jsonArray);
		return jsonObjectResult.toString();
		
	}
	public String getAiiflowData() throws Exception{
		JSONObject jsonObjectResult= new JSONObject();
		JSONArray jsonArray = new JSONArray();
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
		
		
	
		DBCursor cursor=dbColl.find();
		cursor.limit(200);
		System.out.println(cursor.count());
		long count=cursor.count();
		//cursor.sort(new BasicDBObject("updateDateTime",-1));
		cursor.sort(new BasicDBObject("_id",-1));
	 
		while(cursor.hasNext()){
			DBObject d=(DBObject) cursor.next();
			JSONObject jsonObject= new JSONObject(d.toString());
			//System.out.println(jsonObject.toString());
			jsonArray.put(jsonObject);
			
			
		}
		
		//System.out.println(jsonArray.toString());
		jsonObjectResult.put("TotalCount", count);
		jsonObjectResult.put("Code", "200");
		jsonObjectResult.put("Data", jsonArray);
		return jsonObjectResult.toString();
		
	}
	public String getLoriotData() throws Exception{
/*		JSONObject jsonObjectResult= new JSONObject();
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		DBCollection dbColl=(DBCollection) db.getCollection("sth_/_loriot_Gateway");
		
		List listOfSensorIds=dbColl.distinct("recvTime");
		long count=0;
		//for(int i=0;i<listOfSensorIds.size();i++){
		//System.out.println(listOfSensorIds.size());
		//System.out.println(listOfSensorIds.get(listOfSensorIds.size()-1));
		JSONArray jsonArray = new JSONArray();
		for(int i=listOfSensorIds.size()-1;i>listOfSensorIds.size()-100 && i>=0 ;i--){	
			System.out.println("i: "+i+"listOfSensorIds.size(): "+listOfSensorIds.size());
			DBCursor cursor = dbColl.find(new BasicDBObject("recvTime", new BasicDBObject("$eq", listOfSensorIds.get(i))));
			JSONObject jObject= new JSONObject();
			count=count+cursor.count();
			
			while(cursor.hasNext()){
			
				DBObject d=(DBObject) cursor.next();
				jObject.put((String) d.get("attrName"), d.get("attrValue"));
				System.out.println(jObject.toString());
				
			}
			System.out.println(":"+i+":"+jObject.toString());
			jsonArray.put(jObject);
		}
		System.out.println(jsonArray.toString());
		jsonObjectResult.put("TotalCount", count);
		jsonObjectResult.put("Code", "200");
		jsonObjectResult.put("Data", jsonArray);
		mongoClient.close();
		return jsonObjectResult.toString();*/
		
		JSONObject jsonObjectResult= new JSONObject();
		JSONArray jsonArray = new JSONArray();
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		DBCollection dbColl=(DBCollection) db.getCollection(collectionName);
		
		
	
		DBCursor cursor=dbColl.find();
		cursor.limit(200);
		
		long count=cursor.count();
		//cursor.sort(new BasicDBObject("updateDateTime",-1));
		cursor.sort(new BasicDBObject("_id",-1));
	 
		while(cursor.hasNext()){
			DBObject d=(DBObject) cursor.next();
			JSONObject jsonObject= new JSONObject(d.toString());
			//System.out.println(jsonObject.toString());
			jsonArray.put(jsonObject);
			
			
		}
		
		//System.out.println(jsonArray.toString());
		LOGGER.info("TotalCount"+ count);
		jsonObjectResult.put("TotalCount", count);
		jsonObjectResult.put("Code", "200");
		jsonObjectResult.put("Data", jsonArray);
		return jsonObjectResult.toString();
		
	}
	public String getAlleatoData() throws Exception{
		JSONObject jsonObjectResult= new JSONObject();
		MongoClient mongoClient =new MongoClient(host,port);
		DB db=mongoClient.getDB(this.dbName);
		DBCollection dbColl=(DBCollection) db.getCollection("sth_/_Alleato-dev - App_Gateway");
		
		List listOfSensorIds=dbColl.distinct("recvTime");
		long count=0;
		//for(int i=0;i<listOfSensorIds.size();i++){
		//System.out.println(listOfSensorIds.size());
		//System.out.println(listOfSensorIds.get(listOfSensorIds.size()-1));
		JSONArray jsonArray = new JSONArray();
		for(int i=listOfSensorIds.size()-1;i>listOfSensorIds.size()-100 && i>=0 ;i--){	
			System.out.println("i: "+i+"listOfSensorIds.size(): "+listOfSensorIds.size());
			DBCursor cursor = dbColl.find(new BasicDBObject("recvTime", new BasicDBObject("$eq", listOfSensorIds.get(i))));
			JSONObject jObject= new JSONObject();
			count=count+cursor.count();
			
			while(cursor.hasNext()){
			
				DBObject d=(DBObject) cursor.next();
				jObject.put((String) d.get("attrName"), d.get("attrValue"));
				System.out.println(jObject.toString());
				
			}
			System.out.println(":"+i+":"+jObject.toString());
			jsonArray.put(jObject);
		}
		System.out.println(jsonArray.toString());
		jsonObjectResult.put("TotalCount", count);
		jsonObjectResult.put("Code", "200");
		jsonObjectResult.put("Data", jsonArray);
		mongoClient.close();
		return jsonObjectResult.toString();
		
	}
}
