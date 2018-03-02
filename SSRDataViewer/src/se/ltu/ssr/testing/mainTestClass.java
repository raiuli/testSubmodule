package se.ltu.ssr.testing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.ltu.ssr.dataviewer.CygnusConnector;

public class mainTestClass {

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("name", "sensorDataRecevTime");
			jObject.put("type", "epochtime");
			jObject.put("value", String.valueOf(System.currentTimeMillis()));
			
			System.out.println(jObject.toString());
			JSONObject jOuc = new JSONObject(updateContext);
			JSONArray jArray=(JSONArray) ((JSONObject) ((JSONArray) jOuc.get("contextElements")).get(0)).get("attributes");
			int length=jArray.length();
			System.out.println(length);
			jArray.put(length, jObject);
			System.out.println(jOuc.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		CygnusConnector cygnusConnector= new CygnusConnector("130.240.134.126",27017,"sth_default","sth_/_Aiiflow_testGW01_Gateway");
//		cygnusConnector.createConnection();
//		try {
//			cygnusConnector.getAiiflowData();
//		} catch (Exception e) {
//		
//			e.printStackTrace();
//		}
	}
	static String updateContext="{\n" + 
			"  \"contextElements\": [\n" + 
			"    {\n" + 
			"      \"id\": \"EnvironmentalSensor_S2\",\n" + 
			"      \"type\": \"sensor\",\n" + 
			"      \"isPattern\": false,\n" + 
			"      \"attributes\": [\n" + 
			"        {\n" + 
			"          \"name\": \"NO2\",\n" + 
			"          \"type\": \"float\",\n" + 
			"          \"value\": \"NaN\",\n" + 
			"          \"metadatas\": [\n" + 
			"            {\n" + 
			"              \"name\": \"unit\",\n" + 
			"              \"value\": \"ppm\",\n" + 
			"              \"type\": \"string\"\n" + 
			"            }\n" + 
			"          ]\n" + 
			"        },\n" + 
			"        {\n" + 
			"          \"name\": \"PM2.5\",\n" + 
			"          \"type\": \"float\",\n" + 
			"          \"value\": \"10.7695312\",\n" + 
			"          \"metadatas\": [\n" + 
			"            {\n" + 
			"              \"name\": \"unit\",\n" + 
			"              \"value\": \"ug/m3\",\n" + 
			"              \"type\": \"string\"\n" + 
			"            }\n" + 
			"          ]\n" + 
			"        },\n" + 
			"        {\n" + 
			"          \"name\": \"PM10\",\n" + 
			"          \"type\": \"float\",\n" + 
			"          \"value\": \"11.4941406\",\n" + 
			"          \"metadatas\": [\n" + 
			"            {\n" + 
			"              \"name\": \"unit\",\n" + 
			"              \"value\": \"ug/m3\",\n" + 
			"              \"type\": \"string\"\n" + 
			"            }\n" + 
			"          ]\n" + 
			"        },\n" + 
			"        {\n" + 
			"          \"name\": \"location\",\n" + 
			"          \"type\": \"string\",\n" + 
			"          \"value\": \"64.750535, 20.960444\",\n" + 
			"          \"metadatas\": [\n" + 
			"            {\n" + 
			"              \"name\": \"unit\",\n" + 
			"              \"value\": \"deg\",\n" + 
			"              \"type\": \"string\"\n" + 
			"            }\n" + 
			"          ]\n" + 
			"        }\n" + 
			"      ]\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"updateAction\": \"APPEND\",\n" + 
			"  \"auth\": {\n" + 
			"    \"username\": \"iot_sensor_74c4d0efff124c7b98ecd00280d026f9\",\n" + 
			"    \"password\": \"28443848bf414d6e9fa8f10a2caee9c6\"\n" + 
			"  }\n" + 
			"}";
}
