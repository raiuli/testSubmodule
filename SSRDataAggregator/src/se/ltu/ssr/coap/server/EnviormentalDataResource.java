package se.ltu.ssr.coap.server;

import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.adapters.AquaDuctusAdapter;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.PriorityBlockingQueue;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.BAD_REQUEST;;
public class EnviormentalDataResource extends CoapResource{
	Logger log=Logger.getLogger(EnviormentalDataResource.class);
	PriorityBlockingQueue<DataPacket> dataQueue=null;
	private String IDTAGS="Enviormental Label";
	public EnviormentalDataResource(String name, PriorityBlockingQueue<DataPacket> dataQueue) {
		super(name);
	
		this.dataQueue=dataQueue;
	}
	public void handlePUT(CoapExchange exchange){
		
		  byte[] payload = exchange.getRequestPayload();

          try {
              String value = new String(payload, "UTF-8");
              log.info(value);
              this.dataQueue.put(convertStringToData(value));
              exchange.respond(CHANGED, "updated");
          } catch (Exception e) {
              e.printStackTrace();
              exchange.respond(BAD_REQUEST, "Invalid String");
          }
		//exchange.respond("EnviormentalDataResource");
	}
	public SSRData convertStringToData(String datafrmSensor){
		String[] lstRecords= datafrmSensor.split("_");
		SSRData data = new SSRData();
    	data.setiDTags(IDTAGS);
    	data.setTypesOfData(TypesOfData.TOURIST_DATA);
    	data.setDescription("AquaDuctus Enviormental data Adapter");
    	data.setSourceID("EnviormentalSensor_"+super.getName());
    	data.setSourceType("EnviormentalSensor");
    	SSRGeoLocation geolocation= new SSRGeoLocation();
    	geolocation.setLatitude(64.751228);
    	geolocation.setLongitude(20.954745);
    	data.setGeolocation(geolocation);
    	
    	data.setUpdateDateTime(new Date());
    	data.setDataQueryDateTime(new Date());
    	ArrayList<Record> recordList = new ArrayList<Record>();
    	for(int i=0;i<lstRecords.length;i++){
    		String data_field_value[]=lstRecords[i].split(":");
    		String unit;
    		if(i==8){
    			unit="pcs/0.1 cf";
    		}else{
    			unit="ppm";
    		}
    		Record record01= new Record(data_field_value[0], data_field_value[1], unit, "");
        	recordList.add(record01);
    	}
    	
    	data.setRecords(recordList);
    	
    	return data;
    	
    }

}
