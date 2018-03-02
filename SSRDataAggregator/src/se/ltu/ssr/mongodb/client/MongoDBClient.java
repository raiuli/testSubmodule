package se.ltu.ssr.mongodb.client;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import se.ltu.ssr.adapter.interfaces.DataPacket;

public class MongoDBClient {
	Logger log=Logger.getLogger(MongoDBClient.class);
	private String host="";
	private int port=0;
	private String dbName="";
	private String collectionName="";
	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> collection;
	
	public MongoDBClient(String host, int port, String dbName, String collectionName) {
		super();
		this.host = host;
		this.port = port;
		this.dbName = dbName;
		this.collectionName = collectionName;
	}

	public boolean createConnection(){
		try {
			
			List<ServerAddress> seeds = new ArrayList<ServerAddress>();
			seeds.add( new ServerAddress( host ));
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			credentials.add(
			    MongoCredential.createCredential(
			        "fiwareappuser",
			        "SSRtestDB",
			        "fiware@appuser".toCharArray()
			    )
			);
			mongoClient = new MongoClient( seeds, credentials );
			//mongoClient =new MongoClient(host,port);
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
	public boolean insertData(DataPacket dp){
		try{
			Gson gson = new Gson();
			String json = gson.toJson(dp);
			log.debug(json);
			Document doc = new Document().parse(json);
			collection.insertOne(doc);
			return true;	
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
}
