package se.ltu.ssr.mainapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import se.ltu.ssr.adapter.interfaces.RetriverAdapterInterface;
import se.ltu.ssr.coap.server.SSRCoapServer;
import se.ltu.ssr.coapproxy.CoAPProxy;
import se.ltu.ssr.common.data.SSRData;
import se.ltu.ssr.datasendertofiware.FiwareDataSenderServer;
import se.ltu.ssr.mongodb.client.MongoDBClient;
import se.ltu.ssr.adapter.interfaces.DataPacket;



public class MainApp {
	static Logger log;
	private  Properties mainConfiguration =new Properties();
	private PriorityBlockingQueue<DataPacket> dataQueue= new PriorityBlockingQueue<DataPacket>();
	private PriorityBlockingQueue<DataPacket> dataQueueforFiware= new PriorityBlockingQueue<DataPacket>();
	public PriorityBlockingQueue<DataPacket> getDataQueueforFiware() {
		return dataQueueforFiware;
	}
	public void setDataQueueforFiware(PriorityBlockingQueue<DataPacket> dataQueueforFiware) {
		this.dataQueueforFiware = dataQueueforFiware;
	}
	public static void main(String[] args) {
		int length = args.length;
		
		/*
		 * args[0] should specify the config file
		 */
	    if (length <= 0) {
	    	//log.fatal("Config file location is not mentioned");
	    	System.err.println("Config file location is not mentioned");
	    	System.exit(0); 
	    }
	    
	    MainApp mainApp= new MainApp();
    	mainApp.setMainConfiguration(LoadConfigurationFile(args[0]));
    	PropertyConfigurator.configure(mainApp.getMainConfiguration().getProperty("Log4jConfigFile"));
    	log=Logger.getLogger(MainApp.class);
    	/*try {
			CoAPProxy coapProoxy = new CoAPProxy();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	log.info("Starting SSR Data aggregator.");
    	log.info("Configuration files loaded.");
    	SSRCoapServer ssrCoapServer = new SSRCoapServer(mainApp.getDataQueue());
    	ssrCoapServer.StartServer();
    	log.info("CoapServer Started.");
    	FiwareDataSenderServer fiwareDataSenderServer = new FiwareDataSenderServer(mainApp.getDataQueueforFiware());
    //	fiwareDataSenderServer.start();
    	log.info("Fiware Data Sender Server Started.");
    	MongoDBClient mongoDbClient = new MongoDBClient(mainApp.getMainConfiguration().getProperty("MongoDBHost"), Integer.parseInt(mainApp.getMainConfiguration().getProperty("MongoDBPort")), mainApp.getMainConfiguration().getProperty("MongoDBDatabaseName"), mainApp.getMainConfiguration().getProperty("MongoDBcollectionName"));
    	if(mongoDbClient.createConnection()){
    		log.info("Connected with MongoDB.");
    	}else {
    		log.error("Cannot connect with MongoDB.");
    	}
    	HashMap<String,RetriverAdapterInterface> listOfAdapters =new HashMap<String,RetriverAdapterInterface>();
    	
    	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    	for(int i=1;i<=Integer.parseInt(mainApp.getMainConfiguration().getProperty("NoOfAdapter"));i++){
    		String adapterID="00"+i;
    		adapterID=adapterID.substring(adapterID.length()-2);
    		
    		RetriverAdapterInterface retriverAdapter=mainApp.loadClass(mainApp.getMainConfiguration().getProperty("Adapter"+adapterID+"ClassName"));
    		retriverAdapter.setThresholdPerMinute(Integer.parseInt(mainApp.getMainConfiguration().getProperty("Adapter"+adapterID+"PeriodInMinute")));
    		service.scheduleAtFixedRate((Runnable) retriverAdapter, 1, (long)retriverAdapter.getThresholdPerMinute(), TimeUnit.MINUTES);
    	
    		
    		listOfAdapters.put("Adapter"+adapterID, retriverAdapter);
    		
    	}
    	while(true){
    		while(mainApp.getDataQueue().size()>0){
    			try {
					//log.info(mainApp.getDataQueue().take().toString());
					DataPacket dp =mainApp.getDataQueue().take();
					mainApp.getDataQueueforFiware().put(dp);
					if(mongoDbClient.insertData(dp)){
						//log.info("Data Inserted: "+dp.toString());
					}else{
						log.error("Data cannot be inserted. Adding to queue");
						mainApp.getDataQueue().put(dp);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
	}
	public RetriverAdapterInterface loadClass(String className){
   	 Class<?> theClass;
   	 Constructor<?> theConstructor;
   	 RetriverAdapterInterface retriverAdapter=null;
		try {
			theClass = Class.forName(className);
			
			theConstructor=theClass.getConstructor(PriorityBlockingQueue.class);
			
			//retriverAdapter = (RetriverAdapterInterface)theClass.newInstance();
			retriverAdapter = (RetriverAdapterInterface)theConstructor.newInstance(this.getDataQueue());
			//displayProxy = (DisplayProxy)theConstructor.newInstance(proxParameters);
			 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
			
		}catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return retriverAdapter;
		
		
        
   }
	
	
	public PriorityBlockingQueue<DataPacket> getDataQueue() {
		return dataQueue;
	}
	public void setDataQueue(PriorityBlockingQueue<DataPacket> dataQueue) {
		this.dataQueue = dataQueue;
	}
	public  void setMainConfiguration(Properties mainConfiguration) {
		this.mainConfiguration = mainConfiguration;
	}
	public  Properties getMainConfiguration() {
		return mainConfiguration;
	}
	public static Properties LoadConfigurationFile(String path){
			Properties prop =new Properties();
			prop=LoadPropFile(path);
			return prop;
	}
	public static Properties LoadPropFile(String path){
		Properties prop =new Properties();
		  
		   try {
			
				   prop.load(new FileInputStream(path));
				   Enumeration e = prop.propertyNames();

				    while (e.hasMoreElements()) {
				      String key = (String) e.nextElement();
				      System.out.println(key + " -- " + prop.getProperty(key));
				    }
				  
				   
		   } catch (FileNotFoundException e) {
			   	log.fatal(e);
	       } catch (Exception e) {
				e.printStackTrace();
				log.fatal(e);
		  }
			    
			  return prop;
	}
}
