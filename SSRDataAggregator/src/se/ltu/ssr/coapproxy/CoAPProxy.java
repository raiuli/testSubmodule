package se.ltu.ssr.coapproxy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import org.eclipse.californium.proxy.DirectProxyCoapResolver;
import org.eclipse.californium.proxy.ProxyHttpServer;
import org.eclipse.californium.proxy.resources.ForwardingResource;
import org.eclipse.californium.proxy.resources.ProxyCoapClientResource;
import org.eclipse.californium.proxy.resources.ProxyHttpClientResource;

import se.ltu.ssr.coap.adapter.AquaDuctusCoapAdapter;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.common.data.SSRGeoLocation;
import se.ltu.ssr.common.data.TypesOfData;
public class CoAPProxy {
	private static final int PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

	private CoapServer targetServerA;
	static Logger log=Logger.getLogger(CoAPProxy.class);
	int thresholdPerMinute=0;
	private String IDTAGS="Coap Waste Water level";
	SSRData data = null;
	public CoAPProxy() throws IOException {
		ForwardingResource coap2coap = new ProxyCoapClientResource("coap2coap");
		ForwardingResource coap2http = new ProxyHttpClientResource("coap2http");
		
		// Create CoAP Server on PORT with proxy resources form CoAP to CoAP and HTTP
		targetServerA = new CoapServer(PORT);
		targetServerA.add(coap2coap);
		targetServerA.add(coap2http);
		targetServerA.add(new TargetResource("temphumid"));
		targetServerA.start();
		
		ProxyHttpServer httpServer = new ProxyHttpServer(9000);
		httpServer.setProxyCoapResolver(new DirectProxyCoapResolver(coap2coap));
		
		System.out.println("CoAP resource \"target\" available over HTTP at: http://localhost:8080/proxy/coap://localhost:PORT/target");
	}
	
	/**
	 * A simple resource that responds to GET requests with a small response
	 * containing the resource's name.
	 */
	private  class TargetResource extends CoapResource {
		
		private int counter = 0;
		
		public TargetResource(String name) {
			super(name);
		}
		
		@Override
		public void handleGET(CoapExchange exchange) {
			System.out.println(exchange.toString());
			/*URI uri = null; // URI parameter of the request
		
		
			
			
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
				//SSRData 
				data = new SSRData();
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
	        		//dataQueue.put(data);
	        	//	prevData=data;
	        		
	        	//}
				
			} else {
				log.error("No response received.");
			}*/
			exchange.respond("Response "+(++counter)+" from resource " + data.toString());
		}
	}
	
	public static void main(String[] args) throws Exception {
		new CoAPProxy();
	}
}
