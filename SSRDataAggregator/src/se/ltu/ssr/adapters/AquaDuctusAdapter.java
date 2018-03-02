/*
 * 
 */
package se.ltu.ssr.adapters;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.json.JSONTokener;

import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;

// TODO: Auto-generated Javadoc
/**
 * The Class AquaDuctusAdapter.
 */
public class AquaDuctusAdapter   implements RetriverAdapterInterface,Runnable{
	
	/** The log. */
	Logger log=Logger.getLogger(AquaDuctusAdapter.class);
	
	/** The threshold per minute. */
	int thresholdPerMinute=0;
	
	/** The idtags. */
	private String IDTAGS="Waste Water level";
	
	/** The prev data. */
	private SSRData prevData = null;
	
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
	 * Instantiates a new aqua ductus adapter.
	 *
	 * @param dataQueue the data queue
	 */
	public AquaDuctusAdapter(PriorityBlockingQueue<DataPacket> dataQueue) {
		super();
		this.dataQueue = dataQueue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		SSRData currentData=null;
		//log.info(this.getClass().getName()+" Started");
		HttpHost target = new HttpHost("admin.aquaductus.se", 80, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials("ssr_api_user", "ssr382"));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        try {

            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate DIGEST scheme object, initialize it and add it to the local
            // auth cache
            DigestScheme digestAuth = new DigestScheme();
            // Suppose we already know the realm name
            digestAuth.overrideParamter("realm", "some realm");
            // Suppose we already know the expected nonce value
            digestAuth.overrideParamter("nonce", "whatever");
            authCache.put(target, digestAuth);

            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);

            HttpGet httpget = new HttpGet("https://admin.aquaductus.se/api/version/1.0/unit/");
            
            log.info("Executing request " + httpget.getRequestLine() + " to target " + target);
            ArrayList<DataPacket> t=null;
                CloseableHttpResponse response = httpclient.execute(target, httpget, localContext);
                try {
                	log.info("----------------------------------------"+response.getStatusLine());
                	
                	String s=EntityUtils.toString(response.getEntity());
                	log.debug(s);
                	log.info(s);
                	JSONObject jsonObject= new JSONObject(s);
                	t=convertJsonToData(jsonObject);
                	log.debug(t.toString());
                	
                } finally {
                    response.close();
                }
              /* for(int i=0;i<t.size();i++){
            	   SSRData dp=(SSRData)t.get(i);
            	    String unitID=dp.getSourceID().split("_")[1];
            	    httpget = new HttpGet("https://admin.aquaductus.se/api/version/1.0/unit/"+unitID+"/param/level");
                   
                   log.info("Executing request " + httpget.getRequestLine() + " to target " + target);
                   
                        response = httpclient.execute(target, httpget, localContext);
                       try {
                       	log.info("----------------------------------------"+response.getStatusLine());
                       	
                       	String s=EntityUtils.toString(response.getEntity());
                       	log.debug(s);
                       	log.info(s);
                       	JSONObject jsonObject= new JSONObject(s);
                       	dp=(SSRData) addRecordTOData(dp,jsonObject);
                       	//t=convertJsonToData(jsonObject);
                       	log.debug(t.toString());
                       	if(checkIfNewData(prevData,dp)){
        	        		dataQueue.put(dp);
        	        		prevData=dp;
        	        		
        	        	}
                       } finally {
                           response.close();
                       } 
               }*/
               
               
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

	
    /**
     * Convert json to data.
     *
     * @param jsonObj the json obj
     * @return the array list
     */
    public ArrayList<DataPacket> convertJsonToData(JSONObject jsonObj){
    	JSONArray jsonArray = null;
    	ArrayList<DataPacket> SSRDataList = new ArrayList<DataPacket>();
    	try {
			jsonArray=(JSONArray) jsonObj.get("units");
		
	    	for(int i=0; i<jsonArray.length();i++){
	    		SSRData data = new SSRData();
	        	data.setiDTags(IDTAGS);
	        	data.setTypesOfData(TypesOfData.TOURIST_DATA);
	        	
	        	JSONObject j= (JSONObject) jsonArray.get(i);
	        	data.setDescription("Name:"+j.getString("NAME")+" CUST_NAME:"+j.getString("CUST_NAME"));
	        	data.setSourceID("WasteWaterSensor_"+j.getInt("ID"));
	        	data.setSourceType("WasteWaterSensor");
	        	SSRGeoLocation geolocation= new SSRGeoLocation();
	        	geolocation.setLatitude(j.getDouble("LATITUDE"));
	        	geolocation.setLongitude(j.getDouble("LONGITUDE"));
	        	data.setGeolocation(geolocation);
	        	DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
	        	Date date = null;
				try {
					date = format.parse(j.getString("SAMPLE_TIME"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        //	data.setUpdateDateTime((new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)).);
	        	data.setUpdateDateTime(date);
	        	data.setDataQueryDateTime(new Date());
	        	ArrayList<Record> recordList = new ArrayList<Record>();
	        	Record record01= new Record("temp", Integer.toString(j.getInt("temp")), "", "");
	        	recordList.add(record01);
	        	Record record02= new Record("level", Integer.toString(j.getInt("level")), "", "");
	        	recordList.add(record02);
	        	Record record03= new Record("battery", Double.toString(j.getDouble("battery")), "", "");
	        	recordList.add(record03);
	        	data.setRecords(recordList);
	        	SSRDataList.add(data);
	        	if(checkIfNewData(prevData,data)){
	        		dataQueue.put(data);
	        		prevData=data;
	        		
	        	}
	        	
	    	}
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//data.set
    	log.debug(SSRDataList.toString());
    	return SSRDataList;
    	
    }
    
    /**
     * Adds the record TO data.
     *
     * @param data the data
     * @param jsonObject the json object
     * @return the data packet
     */
    public DataPacket addRecordTOData(SSRData data,JSONObject jsonObject){
    	try{
    		
           	JSONArray ja= (JSONArray) jsonObject.get("values");
           	JSONArray ja2= (JSONArray) ja.get(ja.length()-1);
        	log.info(ja2.get(0));
        	log.info(ja2.get(1));
           	log.info(ja.length());
           	ArrayList<Record> recordList = new ArrayList<Record>();
        	//Record record01= new Record("temp", Integer.toString(j.getInt("temp")), "", "");
        	//recordList.add(record01);
        	Record record02= new Record("level", Integer.toString(ja2.getInt(1)), "", "");
        	recordList.add(record02);
        	//Record record03= new Record("battery", Double.toString(j.getDouble("battery")), "", "");
        	//recordList.add(record03);
        	data.setRecords(recordList);
        	
        	
        	//data.set
        	log.debug(data.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return data;
    	
    }
	
	/* (non-Javadoc)
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#getThresholdPerMinute()
	 */
	@Override
	public int getThresholdPerMinute() {
		// TODO Auto-generated method stub
		return thresholdPerMinute;
	}

	/* (non-Javadoc)
	 * @see se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface#setThresholdPerMinute(int)
	 */
	@Override
	public void setThresholdPerMinute(int i) {
		thresholdPerMinute=i;
		
	}
	
	/**
	 * Check if new data.
	 *
	 * @param prevData the prev data
	 * @param newData the new data
	 * @return true, if successful
	 */
	private boolean checkIfNewData(SSRData prevData, SSRData newData){
		if(prevData!=null){
			if(prevData.getUpdateDateTime().getTime()==newData.getUpdateDateTime().getTime()){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}

}
