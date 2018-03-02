/*
 * 
 */
package se.ltu.ssr.adapters;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;

// TODO: Auto-generated Javadoc
/**
 * The Class EnviormentalDataUmea.
 */
public class EnviormentalDataUmea implements RetriverAdapterInterface,Runnable {
	
	/** The log. */
	Logger log=Logger.getLogger(EnviormentalDataUmea.class);
	
	/** The threshold per minute. */
	int thresholdPerMinute=0;
	
	/** The idtags. */
	private String IDTAGS="Enviormental data Umea";
	
	/** The data queue. */
	PriorityBlockingQueue<DataPacket> dataQueue=null;
	
	
	/**
	 * Instantiates a new enviormental data umea.
	 *
	 * @param dataQueue the data queue
	 */
	public EnviormentalDataUmea(PriorityBlockingQueue<DataPacket> dataQueue) {
		super();
		this.dataQueue = dataQueue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		SSRData ssrData = new SSRData();
        CloseableHttpClient httpclient = HttpClients.createDefault();
                
        try {
        	String q="http://ckan.openumea.se/api/action/datastore_search_sql?sql=";
        	String q1="SELECT * from \"27fb8bcc-23cb-4e85-b5b4-fde68a8ef93a\" order by _id desc limit 1";
        	String q2=q+URLEncoder.encode(q1, "UTF-8");
        	log.info(q2);
        	//new URI();
			//URI u = URI.create(q+q1);
            HttpGet httpget = new HttpGet(q2);
            //String q="SELECT * from '27fb8bcc-23cb-4e85-b5b4-fde68a8ef93a%22'order by Mättidpunkt desc limit 5";
            log.info("Executing request " + httpget.getRequestLine() );
           
                CloseableHttpResponse response = httpclient.execute(httpget);
                try {
                	log.info("----------------------------------------"+response.getStatusLine());
                	
                	String s=EntityUtils.toString(response.getEntity());
                	log.debug(s);
                	
                	JSONObject jsonObject= new JSONObject(s);
                	this.dataQueue.put(convertJsonToData(jsonObject));
                	log.info(jsonObject.toString());
                    
                } finally {
                    response.close();
                }

               
            
        } catch(Exception e){
        	e.printStackTrace();
        }
        finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
	}

	/* (non-Javadoc)
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#dataRetrieved()
	 */
	@Override
	public DataPacket dataRetrieved() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#getThresholdPerMinute()
	 */
	@Override
	public int getThresholdPerMinute() {
		// TODO Auto-generated method stub
		return  thresholdPerMinute;
	}

	/* (non-Javadoc)
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#setThresholdPerMinute(int)
	 */
	@Override
	public void setThresholdPerMinute(int i) {
		thresholdPerMinute=i;
		
	}
	 
 	/**
 	 * Convert json to data.
 	 *
 	 * @param jsonObj the json obj
 	 * @return the SSR data
 	 */
 	public SSRData convertJsonToData(JSONObject jsonObj){
	    	JSONArray jsonArray = null;
	    	SSRData data = new SSRData();
	    	try {
			 JSONObject g=(JSONObject) jsonObj.get("result");
			 jsonArray=(JSONArray) g.get("records");
		     
		        	data.setiDTags(IDTAGS);
		        	data.setTypesOfData(TypesOfData.TOURIST_DATA);
		        	
		        	data.setDescription("Name:Municipality of Umea");
		        	data.setSourceID("EnviormentalData_01");
		        	data.setSourceType("WasteWaterSensor");
		        	SSRGeoLocation geolocation= new SSRGeoLocation();
		        	geolocation.setLatitude(7080774);
		        	geolocation.setLongitude(150418);
		        	data.setGeolocation(geolocation);
		        	DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
		        	Date date = null;
					
		        	data.setUpdateDateTime(new Date());
		        	data.setDataQueryDateTime(new Date());
		        	ArrayList<Record> recordList = new ArrayList<Record>();
		        	Record record01= new Record("PM10", Integer.toString(((JSONObject) jsonArray.get(0)).getInt("PM10")), "ugm3", "");
		        	recordList.add(record01);
		        	Record record02= new Record("NO2", Integer.toString(((JSONObject) jsonArray.get(0)).getInt("NO2")), "ugm3", "");
		        	recordList.add(record02);
		        	//Record record03= new Record("battery", Double.toString(j.getDouble("battery")), "", "");
		        	//recordList.add(record03);
		        	data.setRecords(recordList);
		        	
		        	
		    
	    	} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	//data.set
	    	log.debug(data.toString());
	    	return data;
	    	
	    }

}
