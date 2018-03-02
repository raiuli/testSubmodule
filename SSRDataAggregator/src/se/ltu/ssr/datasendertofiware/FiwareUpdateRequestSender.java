package se.ltu.ssr.datasendertofiware;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.common.data.SSRData;

public class FiwareUpdateRequestSender {
	Logger log=Logger.getLogger(FiwareUpdateRequestSender.class);
	String method="POST"; 
	String contentType="application/json";
	//String contentType="application/xml";
	private final static int CONNECTION_TIMEOUT = 3000;
	// The url of IoTBroker
	String updateUrl="http://130.240.134.131/ngsi10/updateContext";
	//String updateUrl="http://130.206.126.52:1026/v1/updateContext";
			
	public boolean SendData(DataPacket dp) {
		
		
		FiwareWrapperUpdate fiwareWrapperUpdate= new FiwareWrapperUpdate();
		String request=fiwareWrapperUpdate.createUpdaterequest(dp);
		try {
			log.info("Sending data to fiware ------------>");
			log.info("Url:"+updateUrl);
			log.info("Data:"+request);
			String response=sendrequest(updateUrl,request);
			log.info("Ressceived response from fiware ------------>");
			log.info("Url:"+response);
		} catch (IOException e) {
			
			log.info("Ressceived error response from fiware ------------>");
			e.printStackTrace();
			return false;
		}
		
		
		return true;
		
	}
	public String sendrequest(String urls, String request) throws IOException{
		InputStream is = null;
		OutputStream os = null;
		String resp = null;
		//URL url = new URL(urls);
		//URL connectionURL = new URL(url + resource);
		URL connectionURL = new URL(urls);
		System.out.println("Connecting to: " + connectionURL.toString());

		// initialize and configure the connection
		HttpURLConnection connection = (HttpURLConnection) connectionURL.openConnection();

		// enable both input and output for this connection
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// configure other things
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("Accept",contentType);

		// set connection timeout
		connection.setConnectTimeout(CONNECTION_TIMEOUT);

		// set the request method to be used by the connection
		connection.setRequestMethod(method);
		// get outputstream and send data
		os = connection.getOutputStream();
		os.write(request.getBytes(Charset.forName("UTF-8")));
		//System.out.println("------------->Request = "+request);
		// send Message
		os.flush();
		// close connection again
		os.close();
		// now it is time to receive the response
		// get input stream from the connection
		is = connection.getInputStream();
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, "UTF-8");
		resp = writer.toString();

		is.close();

		//System.out.println("<-------------Response = "+resp);
		return resp;
		
	}
}
