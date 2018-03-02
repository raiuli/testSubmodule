/*
 * 
 */
package se.ltu.ssr.adapters;
/***
 * http://docs.ckan.apiary.io/#reference/retrieval-functions
 * http://opendata.opennorth.se/api/3/action/recently_changed_packages_activity_list
 * http://opendata.opennorth.se/api/3/action/organization_activity_list_html?id=municipality-of-skelleftea
 * http://opendata.opennorth.se/api/3/action/package_activity_list?id=radon-surveys-skelleftea
 * @author raiuli
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;

public class SkellefteaOpenDataAdapter implements RetriverAdapterInterface,Runnable {

	/** The log. */
	Logger log=Logger.getLogger(EnviormentalDataUmea.class);
	
	/** The threshold per minute. */
	int thresholdPerMinute=0;
	
	/** The idtags. */
	private String IDTAGS="SkellefteaOpenData";
	
	/** The data queue. */
	PriorityBlockingQueue<DataPacket> dataQueue=null;
	
	String[] listOfURLs = {
			"http://opendata.opennorth.se/api/3/action/package_show?id=radon-surveys-skelleftea", 
			"BBB", 
			"CCC", 
			"DDD", 
			"EEE"};
	Date lastDate=null;
	public SkellefteaOpenDataAdapter(PriorityBlockingQueue<DataPacket> dataQueue) {
		super();
		this.dataQueue = dataQueue;
	}

	@Override
	public void run() {
		
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
       try {
       
       	//new URI();
			//URI u = URI.create(q+q1);
           HttpGet httpget = new HttpGet(listOfURLs[0]);
           //String q="SELECT * from '27fb8bcc-23cb-4e85-b5b4-fde68a8ef93a%22'order by Mättidpunkt desc limit 5";
           log.info("Executing request " + httpget.getRequestLine() );
           
           CloseableHttpResponse response = httpclient.execute(httpget);
               try {
               	log.info("----------------------------------------"+response.getStatusLine());
               	
               	String s=EntityUtils.toString(response.getEntity());
               	log.debug(s);
               	
               	JSONObject jsonObject= new JSONObject(s);
               	JSONObject jsonObjectResult=(JSONObject) jsonObject.get("result"); 
               	JSONArray jsonObjectResources=(JSONArray)jsonObjectResult.optJSONArray("resources");
               	for(int i=0;i<jsonObjectResources.length();i++) {
               		jsonObject=(JSONObject)jsonObjectResources.get(i);
               		URL url= new URL(jsonObject.getString("url"));
               		String fileName=jsonObject.getString("name");
               		 jsonObject.getString("revision_timestamp");
               		String datea=jsonObject.getString("revision_timestamp");
               		datea=datea.split("\\.")[0];
               		SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  
               		Date revisionDate=formatter6.parse(datea); 
               		log.info("Last Date:"+lastDate);
               		log.info("Revision Date:"+revisionDate);
               		if	(lastDate==null) {
               			lastDate=revisionDate;
               			ArrayList<SSRData> sSRDataList =downloadCSV(url,fileName);
	               		for(int j=0;j<sSRDataList.size();j++) {
	               			//log.info(sSRDataList.get(j));
	               			this.dataQueue.put(sSRDataList.get(j));
	               		}
               		} else if((lastDate.compareTo(revisionDate)<0)){
               			
               			lastDate=revisionDate;
	               		ArrayList<SSRData> sSRDataList =downloadCSV(url,fileName);
	               		for(int j=0;j<sSRDataList.size();j++) {
	               			//log.info(sSRDataList.get(j));
	               			this.dataQueue.put(sSRDataList.get(j));
	               		}
               		}

               	}
               //	this.dataQueue.put(convertJsonToData(jsonObject));
               	log.info(jsonObject.toString());
                   
               } catch(Exception e){
                  	e.printStackTrace();
               }finally {
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

	@Override
	public DataPacket dataRetrieved() {
		// TODO Auto-generated method stub
		return null;
	}
	private  ArrayList<SSRData>  convertCSV(String filepath) {
		BufferedReader br = null;
		String line = "";
        String cvsSplitBy = ";";
        ArrayList<SSRData> sSRDataList = new ArrayList<SSRData>();
		 try {

	            br = new BufferedReader(new FileReader(filepath));
	            int row_counter=0;
	            String[] field_name = null;
	            while ((line = br.readLine()) != null) {
	            		SSRData ssrData = new SSRData();
	            		ssrData.setiDTags(IDTAGS);
	            		ssrData.setTypesOfData(TypesOfData.TOURIST_DATA);
			        	
	            		ssrData.setDescription("Name:Municipality of Skelleftea radon-surveys");
	            		ssrData.setSourceID("radon-surveys");
	            		ssrData.setSourceType("radon-surveys");
			        	SSRGeoLocation geolocation= new SSRGeoLocation();
			        	geolocation.setLatitude(7080774);
			        	geolocation.setLongitude(150418);
			        	ssrData.setGeolocation(geolocation);
			        	DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
			        	Date date = null;
						
			        	ssrData.setUpdateDateTime(new Date());
			        	ssrData.setDataQueryDateTime(new Date());
			        	ArrayList<Record> records = new ArrayList<Record>();
	                // use comma as separator
	                String[] data = line.split(cvsSplitBy);
	                if(row_counter==0) {
	                		field_name=new String[data.length]; 
	                		for(int i=0;i<data.length;i++) {
	                			field_name[i]=data[i];
	                		}
	                		row_counter++;
	                }else {
	                		if(data.length==field_name.length) {
	                			for(int i=0;i<data.length;i++) {
			                		Record	record = new Record();
			                		record.setName(field_name[i]);
			                		if (data[i]==null ){
			                			record.setValue("");
			                		}else {
			                			record.setValue(data[i]);
			                		}
			                		record.setUnit("");
			                		record.setDescription("");
			                		records.add(record);
			                		//log.info(record.toString());
			                	}
	                			ssrData.setRecords(records);
	        	                sSRDataList.add(ssrData);
	        	                //log.info("End reading one record-------------------------------------------------");
	                		}else {
	                			//log.info("Cannot read one record due to miss match in header and file length-------------------------------------------------");
	                		}
		                	
	                }
	                //System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
	                
	            }

	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		 log.info("sSRDataList size: "+sSRDataList.size());
		 log.info("End coverting file");
		 return sSRDataList;
	}
	private ArrayList<SSRData> downloadCSV(URL url,String fileName) {
		ArrayList<SSRData> sSRDataList=null;
		  try {
			  log.info("Downloading file:"+url.toString());
			    CloseableHttpClient client = HttpClientBuilder.create().build();
	            HttpGet request = new HttpGet(url.toString());
	 
	            HttpResponse response = client.execute(request);
	            HttpEntity entity = response.getEntity();
	 
	            int responseCode = response.getStatusLine().getStatusCode();
	 
	            log.info("Request Url: " + request.getURI());
	            log.info("Response Code: " + responseCode);
	 
	            InputStream is = entity.getContent();
	 
	            String filePath = fileName.replace("/", "_");
	             filePath = filePath.replace(":", "-");
	            FileOutputStream fos = new FileOutputStream(new File(filePath));
	 
	            int inByte;
	            while ((inByte = is.read()) != -1) {
	                fos.write(inByte);
	            }
	 
	            is.close();
	            fos.close();
	 
	            client.close();
	            sSRDataList=convertCSV(filePath);
	            log.info("File Download Completed!!!");
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (UnsupportedOperationException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		  return sSRDataList;
	}

	@Override
	public int getThresholdPerMinute() {
		// TODO Auto-generated method stub
		return this.thresholdPerMinute;
	}

	@Override
	public void setThresholdPerMinute(int i) {
		this.thresholdPerMinute=i;
		
	}

}
