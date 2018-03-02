/*
 * 
 */
package se.ltu.ssr.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
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
 * The Class SMHIAdapter.
 */
/*
 * 
 * 150450: Skellefteå 
 * 151380: Skellefteå Flygplats
 * 
 * 
 * 
 * */
public class SMHIAdapter  implements RetriverAdapterInterface,Runnable {
	
	/** The log. */
	Logger log=Logger.getLogger(SMHIAdapter.class);
	
	/** The threshold per minute. */
	int thresholdPerMinute=0;
	
	/** The idtags. */
	private String IDTAGS="Weather data Skelleftea";
	
	/** The data queue. */
	PriorityBlockingQueue<DataPacket> dataQueue=null;
	 
	/* (non-Javadoc)
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#dataRetrieved()
	 */
	@Override
	public DataPacket dataRetrieved() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Instantiates a new SMHI adapter.
	 *
	 * @param dataQueue the data queue
	 */
	public SMHIAdapter(PriorityBlockingQueue<DataPacket> dataQueue) {
		super();
		this.dataQueue = dataQueue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//log.info(this.getClass().getName()+" Started");
		SSRData ssrData = new SSRData();
        CloseableHttpClient httpclient = HttpClients.createDefault();
                
        try {

            HttpGet httpget = new HttpGet("http://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station/151380/period/latest-hour/data.json");
            
            log.info("Executing request " + httpget.getRequestLine() );
           
                CloseableHttpResponse response = httpclient.execute(httpget);
                try {
                	log.info("----------------------------------------"+response.getStatusLine());
                	
                	String s=EntityUtils.toString(response.getEntity());
                	log.debug(s);
                	
                	JSONObject jsonObject= new JSONObject(s);
                	 ssrData=convertJsonToData(jsonObject,ssrData);
                	log.info(ssrData.toString());
                    
                } finally {
                    response.close();
                }

                 httpget = new HttpGet("http://opendata-download-metobs.smhi.se/api/version/1.0/parameter/4/station/151380/period/latest-hour/data.json");
                
                log.info("Executing request " + httpget.getRequestLine() );
               
                     response = httpclient.execute(httpget);
                    try {
                    	log.info("----------------------------------------"+response.getStatusLine());
                    	
                    	String s=EntityUtils.toString(response.getEntity());
                    	log.debug(s);
                    	
                    	JSONObject jsonObject= new JSONObject(s);
                    	 ssrData=convertJsonToData(jsonObject,ssrData);
                    	log.info(ssrData.toString());
                    	dataQueue.put(ssrData);
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
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#getThresholdPerMinute()
	 */
	@Override
	public int getThresholdPerMinute() {
		
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
 	 * @param data the data
 	 * @return the SSR data
 	 */
 	public SSRData convertJsonToData(JSONObject jsonObj,SSRData data){
	    	
	    		try{
	    			if(data.getiDTags()== null){
	    				data.setiDTags(IDTAGS);
			        	data.setTypesOfData(TypesOfData.HISTORICAL_DATA);
			        	JSONObject j=(JSONObject) jsonObj.get("station");
			        	String s=j.getString("name");
			        	data.setDescription("Weathe data of temprature and wind speed of "+s);
			        	data.setSourceID("WeatherData_"+j.getString("key"));
			        	data.setSourceType("WeatherData");
			        	j=(JSONObject) ((JSONArray) jsonObj.get("position")).get(1);
			        	SSRGeoLocation geolocation= new SSRGeoLocation();
			        	geolocation.setLatitude( j.getDouble("latitude"));
			        	geolocation.setLongitude(j.getDouble("longitude"));
			        	data.setGeolocation(geolocation);
	    			}
		    		
	    			//JSONObject j=(JSONObject) jsonObj.getLong("updated");
		        	data.setUpdateDateTime(new Date(jsonObj.getLong("updated")));
		        	data.setDataQueryDateTime(new Date());
		        	ArrayList<Record> recordList = null;
		        	if(data.getRecords().size() ==0){
		        		recordList= new ArrayList<Record>();
		        	}else{
		        		recordList=data.getRecords();
		        	}
		        	
		        	Record record01= new Record( ((JSONObject) jsonObj.get("parameter")).getString("name"), ((JSONObject) ((JSONArray) jsonObj.get("value")).get(0)).getString("value"), ((JSONObject) jsonObj.get("parameter")).getString("unit"), ((JSONObject) jsonObj.get("parameter")).getString("summary"));
		        	recordList.add(record01);
		        	
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
