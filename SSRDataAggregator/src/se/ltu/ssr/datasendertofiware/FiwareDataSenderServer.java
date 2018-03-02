package se.ltu.ssr.datasendertofiware;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import se.ltu.ssr.adapter.interfaces.DataPacket;


public class FiwareDataSenderServer extends Thread {
	
	Logger log=Logger.getLogger(FiwareDataSenderServer.class);
	private PriorityBlockingQueue<DataPacket> dataQueueforFiware;
	FiwareUpdateRequestSender fiwareUpdateRequestSender = new FiwareUpdateRequestSender();
	
	
	public FiwareDataSenderServer(PriorityBlockingQueue<DataPacket> dataQueueforFiware) {
		super();
		this.dataQueueforFiware = dataQueueforFiware;
	}

	public void run(){
	       //System.out.println("MyThread running");
	       
	     
	       	while(true){
	    		while(dataQueueforFiware.size()>0){
	    			try {
	    				log.info("Data Recieved from sensor. Queue Size: "+dataQueueforFiware.size());
						DataPacket dp =dataQueueforFiware.take();
						if(fiwareUpdateRequestSender.SendData(dp)){
							log.info("Data Send to fiware: "+dp.toString());
							
						}else{
							log.error("Data cannot be Send to fiware. Adding to queue");
							
							this.dataQueueforFiware.put(dp);
						}
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
	    		}
	    	
	       	}
	    }
	
	
	
}
