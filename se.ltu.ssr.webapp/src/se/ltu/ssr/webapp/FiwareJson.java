package se.ltu.ssr.webapp;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.neclab.iotplatform.iotbroker.commons.JsonFactory;
import eu.neclab.iotplatform.iotbroker.commons.XmlFactory;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElementResponse;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;
import eu.neclab.iotplatform.ngsi.api.datamodel.QueryContextRequest;
import eu.neclab.iotplatform.ngsi.api.datamodel.QueryContextResponse;
@Path("/FiwareJSON")
public class FiwareJson {
	MongoDbClientNew mongoDbClient = new MongoDbClientNew("130.240.134.129",27017,"SSRtestDB","RawData");
	//queryContext
	@POST 
	 @Path("/queryContext")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	  public String getSources(String s) {
		JsonFactory jsonFactory = new JsonFactory();
		QueryContextRequest b_request = (QueryContextRequest) jsonFactory.convertStringToJsonObject(s,QueryContextRequest.class);
		XmlFactory a = new XmlFactory();
		//QueryContextRequest b_request = (QueryContextRequest)a.convertStringToXml(s, QueryContextRequest.class);
		
		System.out.println("S:"+s);
		System.out.println("b_request:"+b_request);
		QueryContextResponse qcr = new QueryContextResponse();
		
		QueryContextResponse b_response = (QueryContextResponse)a.convertStringToXml(response, QueryContextResponse.class);
		List<ContextElementResponse> lceRes=b_response.getListContextElementResponse();
		ContextElement ce=lceRes.get(0).getContextElement();
		ce.setEntityId(b_request.getEntityIdList().get(0));
		EntityId entityId =b_request.getEntityIdList().get(0);
		String attribute=b_request.getAttributeList().get(0);
		mongoDbClient.openConnection();
		String json=mongoDbClient.getLatestData(entityId.getId());
		JSONObject json2 = null;
		String valueofdata="";
		try {
			json2 = new JSONObject(json);
			JSONArray j2=(JSONArray) json2.get("Sources");
			JSONObject Sources=(JSONObject) j2.get(0);
			
			JSONArray records =Sources.getJSONArray("records");
			
			for(int i=0;i<records.length();i++){
				JSONObject data=(JSONObject) records.get(i);
				if(attribute.equals(data.get("name"))){
					valueofdata=(String) data.get("value");
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			mongoDbClient.closeConnection();
		}
		mongoDbClient.closeConnection();
		ContextAttribute contextAttribute = new ContextAttribute();
		contextAttribute.setName(attribute);
		contextAttribute.setContextValue(valueofdata);
		List<ContextAttribute> lca= new ArrayList<ContextAttribute>();
		lca.add(contextAttribute);
		b_response.getListContextElementResponse().get(0).getContextElement().setContextAttributeList(lca);
		System.out.println(s);
		//return b_response.toString();
		return b_response.toJsonString();
	  }
	String response=""
+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
+"<queryContextResponse>"
	+"<contextResponseList>"
		+"<contextElementResponse>"
			+"<contextElement>"
		     +"<entityId type=\"TemperatureSensor\" isPattern=\"false\">"
	                  +"<id>TempSensor_B</id>"
			 +"</entityId>"
				+"<contextAttributeList>"
					+"<contextAttribute>"
						+"<name>tempValue</name>"
						+"<contextValue>13</contextValue>"					
					+"</contextAttribute>"
				+"</contextAttributeList>"
			+"</contextElement>"
			+"<statusCode>"
				+"<code>200</code>"
				+"<reasonPhrase>Ok</reasonPhrase>"
			+"</statusCode>"
		+"</contextElementResponse>"
	+"</contextResponseList>"
+"</queryContextResponse> ";
}
