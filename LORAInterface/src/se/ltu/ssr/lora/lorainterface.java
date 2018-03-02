package se.ltu.ssr.lora;

import java.io.IOException;
import java.text.SimpleDateFormat;
//import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.apache.tomcat.util.codec.binary.Base64;
import org.glassfish.jersey.client.ClientConfig;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.ltu.ssr.lora.common.ElsysDecoder;
import se.ltu.ssr.lora.common.PayloadtoHexConverter;

import java.util.logging.*;




@Path("/SensorData")
public class lorainterface {

	private static final Logger LOGGER = Logger.getLogger(lorainterface.class.getName());
	
	String updateRequest_1="{"
			+"\"contextElements\": ["
			+ "{\"type\": \"LoraNS\",\"isPattern\": \"false\",\"id\": \"lns_01\","
			+"\"attributes\": ["
                +"{\"name\": \"Msg\", \"type\": \"integer\", \"value\": \"test\"},"
               
           +" ]}],"     
           +"\"updateAction\": \"APPEND\"} ";
	@POST 
	@Path("/rxmessage")
	@Produces({"application/vnd.kerlink.iot-v1+json"})
	//@Consumes({"application/vnd.kerlink.iot-v1+json"})
	public Response loraInterfacePost(String s,@Context final HttpServletResponse response,@Context final HttpServletRequest request) {
		
		LOGGER.info(request.getRequestURL().toString());
		LOGGER.info(request.getRemoteAddr());
		
		LOGGER.info("DataFromLNS:"+s);
		
		/*String new_s=s.replace("\"", "");
		new_s=new_s.replace("{", "");
		new_s=new_s.replace("}", "");
		System.out.println(new_s);*/
		try {
			JSONObject inComingjReqObject = new JSONObject(s);
			LOGGER.info("Incoming Data from LORIOT:"+inComingjReqObject);
			
			JSONObject outGoingReqjObject = new JSONObject(updateRequest_1);
			//System.out.println(outGoingReqjObject);
			JSONArray ces =(JSONArray)outGoingReqjObject.get("contextElements");
			JSONObject ce=(JSONObject)ces.get(0);
			JSONArray attributes=((JSONArray)ce.get("attributes"));
			JSONObject attribute =(JSONObject) attributes.get(0);
		
			String msg=((JSONObject) inComingjReqObject.get("userdata")).getString("payload");
		
			String msg1=toHexNiceDisplay(msg);
		
			attribute.put("value",msg1);
			attribute.put("type","hex");
			attribute.put("name","payload");
			
			/*Iterator<?> keys = inComingjReqObject.keys();
			while(keys.hasNext()) {
				JSONObject attribute_lora =  new JSONObject();
				attribute_lora.put("name", keys);
			}*/
			//devEui
			JSONObject attribute_devEUI =  new JSONObject() ;
			attribute_devEUI.put("name", "devEui");
			attribute_devEUI.put("value",inComingjReqObject.get("devEui"));
			attribute_devEUI.put("type","hex");
			attributes.put(1, attribute_devEUI);
			//appEui
			JSONObject attribute_appEUI =  new JSONObject() ;
			attribute_appEUI.put("name", "appEui");
			attribute_appEUI.put("value",inComingjReqObject.getString("appEui"));
			attribute_appEUI.put("type","dec");
			attributes.put(2, attribute_appEUI);
			//devAddr
			JSONObject attribute_devAddr =  new JSONObject() ;
			attribute_devAddr.put("name", "devAddr");
			attribute_devAddr.put("value",inComingjReqObject.get("devAddr"));
			attribute_devAddr.put("type","hex");
			attributes.put(3, attribute_devAddr);
			//clusterId
			JSONObject attribute_clusterId =  new JSONObject() ;
			attribute_clusterId.put("name", "clusterId");
			attribute_clusterId.put("value",inComingjReqObject.get("clusterId"));
			attribute_clusterId.put("type","dec");
			attributes.put(4, attribute_clusterId);
			//msgId
			JSONObject attribute_msgId =  new JSONObject() ;
			attribute_msgId.put("name", "msgId");
			attribute_msgId.put("value",inComingjReqObject.get("msgId"));
			attribute_msgId.put("type","hex");
			attributes.put(5, attribute_msgId);
			
			//fcntDown
			JSONObject attribute_fcntDown =  new JSONObject() ;
			attribute_fcntDown.put("name", "fcntDown");
			attribute_fcntDown.put("value",inComingjReqObject.get("fcntDown"));
			attribute_fcntDown.put("type","dec");
			attributes.put(6, attribute_fcntDown);
			
			//fcntUp
			JSONObject attribute_fcntUp =  new JSONObject() ;
			attribute_fcntUp.put("name", "fcntUp");
			attribute_fcntUp.put("value",inComingjReqObject.get("fcntUp"));
			attribute_fcntUp.put("type","dec");
			attributes.put(7, attribute_fcntUp);
		
			//fport
			JSONObject attribute_fport =  new JSONObject() ;
			attribute_fport.put("name", "fport");
			attribute_fport.put("value",((JSONObject) inComingjReqObject.get("userdata")).get("fport"));
			attribute_fport.put("type","dec");
			attributes.put(8, attribute_fport);
			//maxretry
			JSONObject attribute_maxretry =  new JSONObject() ;
			attribute_maxretry.put("name", "maxretry");
			attribute_maxretry.put("value",((JSONObject) inComingjReqObject.get("userdata")).get("maxretry"));
			attribute_maxretry.put("type","dec");
			attributes.put(9, attribute_maxretry);
			
			//ttl
			JSONObject attribute_ttl =  new JSONObject() ;
			attribute_ttl.put("name", "ttl");
			attribute_ttl.put("value",((JSONObject) inComingjReqObject.get("userdata")).get("ttl"));
			attribute_ttl.put("type","dec");
			attributes.put(10, attribute_ttl);
			
			
		
			Date date = new Date ();
			//date.setTime((long)unix_time);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSS");
			String date1 = sdf.format(date);
			
			
			JSONObject attributeTS =  new JSONObject() ;
			attributeTS.put("name", "Timestamp");
			attributeTS.put("value",date1);
			attributeTS.put("type","dd-MM-yyyy HH:mm:ss.SSSS");
			attributes.put(11, attributeTS);
			
			ElsysDecoder elsysDecoder =new ElsysDecoder();
			try {
				JSONArray result=elsysDecoder.decoder(elsysDecoder.hexStringToByteArray(toHex(msg)));
				for(int i=0;i<result.length();i++) {
					attributes.put(12+i, result.get(i));
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			int attributes_counter=0;
			JSONArray allAttributes =new JSONArray();
			//allAttributes.put(attributes_counter, attribute);
			allAttributes.put(attributes_counter++,createAttributeJson("Temperature","","float"));
			allAttributes.put(attributes_counter++,createAttributeJson("Humidity","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Light","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Battery level","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Acceleration X-Axis","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Acceleration Y-Axis","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Acceleration Z-Axis","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Acc motion","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Motion","","integer"));
			allAttributes.put(attributes_counter++,createAttributeJson("Co2","","integer"));
			int c=12;
			
			if(attributes.length()>12) {
				c=attributes.length();
					for(int i=0;i<allAttributes.length();i++) {
						boolean needToAdd=false;
						for(int j=12;j<attributes.length();j++) {
							String x=(String) ((JSONObject)allAttributes.get(i)).get("name");
							String y=(String) ((JSONObject)attributes.get(j)).get("name");
							if(x.equals(y)) {
								break;
							}else {
								needToAdd=true;
								
							}
							
						}
						if(needToAdd==true) {
							attributes.put(c++,((JSONObject)allAttributes.get(i)));
						}
					}
				}else {
					for(int i=0;i<allAttributes.length();i++) {
						attributes.put(c++,((JSONObject)allAttributes.get(i)));
					}
				}
				
				
		
			LOGGER.info("Incoming Data Converted to NGSI:"+outGoingReqjObject);
			LOGGER.info("Forwarding request to OrionBroker------------");
			
			
			Client client = ClientBuilder.newClient( new ClientConfig().register( lorainterface.class ) );
			WebTarget webTarget = client.target("http://130.240.134.126:1026/v1/").path("updateContext");
			
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
			//invocationBuilder.header("X-Subject-Token", X_Subject_Token);
			//invocationBuilder.header("X-Auth-Token", X_Subject_Token);
			Response response2 = invocationBuilder.post(Entity.entity(outGoingReqjObject.toString(), MediaType.APPLICATION_JSON));
			LOGGER.info(String.valueOf( response2.getStatus()));
			String response_string=response2.readEntity(String.class);
			JSONObject response_json = new JSONObject(response_string);
			
			LOGGER.info(response_json.toString());
			
			sendDevEuiEntityID(outGoingReqjObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			
			LOGGER.info("lorainterface POST End______________________ ");
			e.printStackTrace();
			try {
				//response.sendError(400, "error");
				response.setStatus(400);
				response.flushBuffer();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"Error\":\"Something went wrong !!!\"}").build();
		}
		LOGGER.info("lorainterface POST End________________________");
		response.setStatus(HttpServletResponse.SC_CREATED);
	    try {
	        response.flushBuffer();
	    }catch(Exception e){}
	    return Response.status(Response.Status.CREATED).entity("{OK}").build();
	}
	/*@GET 
	public String saayXMLTextHello(String s) {
		LOGGER.info("_______________________lorainterface GET "+s);
		return "ok";
	}*/
	private JSONObject createAttributeJson(String name, String value, String value_type){
		
		JSONObject attribute =  new JSONObject() ;
		
		try {
			attribute.put("name", name);
			attribute.put("value",value);
			attribute.put("type",value_type);
			
			
			
			return attribute;
			//LOGGER.info(attribute.toString());
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return attribute;
	}
	private void sendDevEuiEntityID(JSONObject data) throws Exception {
		LOGGER.info(data.toString());
		JSONArray jsonCEs=(JSONArray) data.get("contextElements");
		JSONObject jsonCE = (JSONObject) jsonCEs.get(0);
		JSONArray jsonAtts=(JSONArray) jsonCE.get("attributes");
		JSONObject jsonDevEUI=(JSONObject) jsonAtts.get(1);
		jsonCE.put("id", jsonDevEUI.get("value"));
		jsonCE.put("type", "LORA_Sensor");
		jsonAtts.remove(1);
		LOGGER.info("Budling NGSI for individual LORA sensor:"+data);
		LOGGER.info("Forwarding request to OrionBroker for individual LORA sensor------------");
		
		
		Client client = ClientBuilder.newClient( new ClientConfig().register( lorainterface.class ) );
		WebTarget webTarget = client.target("http://130.240.134.126:1026/v1/").path("updateContext");
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		//invocationBuilder.header("X-Subject-Token", X_Subject_Token);
		//invocationBuilder.header("X-Auth-Token", X_Subject_Token);
		Response response2 = invocationBuilder.post(Entity.entity(data.toString(), MediaType.APPLICATION_JSON));
		LOGGER.info(String.valueOf( response2.getStatus()));
		String response_string=response2.readEntity(String.class);
		JSONObject response_json = new JSONObject(response_string);
		
		LOGGER.info(response_json.toString());
		//(JSONObject)((JSONArray) jsonCE.get(0)).get("attributes");
		
	} 
	public   String toHex(String payload) {
		char[] DIGITS
	    = {'0', '1', '2', '3', '4', '5', '6', '7',
	        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		
		byte[] data =Base64.decodeBase64(payload);
		//byte[] data =Base64.getDecoder().decode(payload);
		
		final StringBuffer sb = new StringBuffer(data.length * 2);
		
		for (int i = 0; i < data.length; i++) {
			sb.append(DIGITS[(data[i] >>> 4) & 0x0F]);
			sb.append(DIGITS[data[i] & 0x0F]);
			
		}
		LOGGER.info("SB:"+sb.toString());
		return sb.toString();

	}
	public   String toHexNiceDisplay(String data) {
		
		data=toHex(data);
		String newString = "";
		for(int i =0;i<data.length();i=i+2) {
			newString=newString+" "+data.substring(i, i+2);
		}
		LOGGER.info("newString:"+newString.toString());
		return newString.trim();
	}
	 
}
