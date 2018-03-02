package se.ltu.ssr.coap.adapter;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.json.JSONObject;

import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface;
import se.ltu.ssr.adapters.AquaDuctusAdapter;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;

public class AquaDuctusCoapAdapter implements RetriverAdapterInterface,Runnable{
	Logger log=Logger.getLogger(AquaDuctusCoapAdapter.class);
	int thresholdPerMinute=0;
	private String IDTAGS="Coap Waste Water level";
	
	PriorityBlockingQueue<DataPacket> dataQueue=null;
	
	public AquaDuctusCoapAdapter(PriorityBlockingQueue<DataPacket> dataQueue) {
		super();
		this.dataQueue = dataQueue;
	}

	@Override
	public void run() {
		URI uri = null; // URI parameter of the request
		
		
			
			
			try {
				uri = new URI("coap://46.230.231.120:5683/temphumid");
			} catch (URISyntaxException e) {
				log.error("Invalid URI: " + e.getMessage());
				
			}
			
			CoapClient client = new CoapClient(uri);

			CoapResponse response = client.get();
			
			if (response!=null) {
				String tmp=response.getResponseText();
				log.info("----------------------------------------Response Code:"+response.getCode()+" Options:"+response.getOptions()+" Text:"+tmp);
				log.info("\nADVANCED\n");
				log.info(Utils.prettyPrint(response));
				//Temp=21.0*  Humidity=34.0%
				String[] tmp1=tmp.split(" ");
				//SSRData prevData = null;
				SSRData data = new SSRData();
	        	data.setiDTags(IDTAGS);
	        	data.setTypesOfData(TypesOfData.HISTORICAL_DATA);
	        	
	        	data.setDescription("Name:AquaDuctus CUST_NAME:AquaDuctus");
	        	data.setSourceID("WasteWaterSensor_CoAP_1");
	        	data.setSourceType("WasteWaterSensor");
	        	SSRGeoLocation geolocation= new SSRGeoLocation();
	        	//64.7438210,20.9466060
	        	geolocation.setLatitude(64.7438210);
	        	geolocation.setLongitude(20.9466060);
	        	data.setGeolocation(geolocation);
	        	
	        	data.setUpdateDateTime(new Date());
	        	data.setDataQueryDateTime(new Date());
	        	ArrayList<Record> recordList = new ArrayList<Record>();
	        	String tmp2=tmp1[0].split("=")[1];
	        	Record record01= new Record("temp", tmp2.substring(0, tmp2.length()-1),"","");
	        	recordList.add(record01);
	        	tmp2=tmp1[2].split("=")[1];
	        	Record record02= new Record("temp", tmp2.substring(0, tmp2.length()-1),"","");
	        	recordList.add(record02);
	        	data.setRecords(recordList);
	        	//SSRDataList.add(data);
	        	//if(checkIfNewData(prevData,data)){
	        		dataQueue.put(data);
	        	//	prevData=data;
	        		
	        	//}
				
			} else {
				log.error("No response received.");
			}
			
		
		
	}

	@Override
	public DataPacket dataRetrieved() {
		// TODO Auto-generated method stub
		return null;
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
