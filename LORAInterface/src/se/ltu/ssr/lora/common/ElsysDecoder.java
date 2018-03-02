package se.ltu.ssr.lora.common;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ElsysDecoder {
	private static final Logger LOGGER = Logger.getLogger(ElsysDecoder.class.getName());
	final byte TYPE_TEMP    	=0x01; //temp 2 bytes -3276.8°C -->3276.7°C
	final byte TYPE_RH		=0x02; //Humidity 1 byte  0-100%
	final byte TYPE_ACC		=0x03; //acceleration 3 bytes X,Y,Z -128 --> 127 +/-63=1G
	final byte TYPE_LIGHT	=0x04; //Light 2 bytes 0-->65535 Lux
	final byte TYPE_MOTION	=0x05; //No of motion 1 byte  0-255
	final byte TYPE_CO2		=0x06; //Co2 2 bytes 0-65535 ppm 
	final byte TYPE_VDD		=0x07; //VDD 2byte 0-65535mV
	final byte TYPE_ANALOG1  =0x08; //VDD 2byte 0-65535mV
	final byte TYPE_GPS      =0x09; //3bytes lat 3bytes long binary
	final byte TYPE_PULSE1   =0x0A; //2bytes relative pulse count
	final byte TYPE_ACC_MOTION   = 0x0F; //1byte number of vibration/motion
	final byte TYPE_AIR_QUALITY =0x0B; //24 bytes Air Quality data (PM1, PM2.5, PM10, and NO2 ppm ,lat, lon)
	JSONArray attributes= new JSONArray();
	JSONObject obj = new JSONObject();
	 int index;
	public ElsysDecoder() {
		super();
	}
	
	public JSONArray decoder(int[] b)  {
		int attributes_counter=0;
		//int i=0;
		for(int i=0;i<b.length;i++) {
			LOGGER.info("i:"+i+"data:"+b[i]);
			switch(b[i]) { 
		    case TYPE_TEMP: 
		    		int c= (b[i+1]<<8);
		    		int d = (c|(b[i+2]));
		    		int t=bin16dec(d);
		    		float temp=(float)t/10;
		    		addToJsonList(attributes_counter,"Temperature",Float.toString(temp),"float","unit","C","string");
				i=i+2;
				attributes_counter++;
		        break; 
		    case TYPE_RH: 
		    		addToJsonList(attributes_counter,"Humidity",Integer.toString(b[i+1]),"integer","unit","%","string");
				i=i+1;
				attributes_counter++;
		        break;
		    case TYPE_LIGHT:
		    		 c= (b[i+1]<<8);
		    		 d = (c|(b[i+2]));
		    		 addToJsonList(attributes_counter,"Light",Integer.toString(d),"integer","unit","LUX","string");
				 i=i+2;
				 attributes_counter++;
				 break;
		    case TYPE_VDD:
		    		 c= (b[i+1]<<8);
		    		 d = (c|(b[i+2]));
		    		 addToJsonList(attributes_counter,"Battery level",Integer.toString(d),"integer","unit","mV","string");
				 i=i+2;
				 attributes_counter++;
				 break;
		    case TYPE_ACC:
		    		 int x=bin8dec(b[i+1]);
		    		 addToJsonList(attributes_counter,"Acceleration X-Axis",Integer.toString(x),"integer","unit","+/-63 1G","string");
		    		 attributes_counter++;
		    		 int y=bin8dec(b[i+2]);
		    		 addToJsonList(attributes_counter,"Acceleration Y-Axis",Integer.toString(y),"integer","unit","+/-63 1G","string");
		    		 attributes_counter++;
		    		 int z=bin8dec(b[i+3]);
		    		 addToJsonList(attributes_counter,"Acceleration Z-Axis",Integer.toString(z),"integer","unit","+/-63 1G","string");
		    		 attributes_counter++;
		    		 i=i+3;
		    		 break;
		    case TYPE_ACC_MOTION:
	    		 	addToJsonList(attributes_counter,"Acc motion",Integer.toString(b[i+1]),"integer","unit","Number of Vibration/motion","string");
	    		 	i=i+1;
	    		 	attributes_counter++;
	    		 	break;
		    case TYPE_MOTION:
		    		 addToJsonList(attributes_counter,"Motion",Integer.toString(b[i+1]),"integer","unit","No of motion","string");
		    		 i=i+1;
		    		 attributes_counter++;
		    		 break;
		    case TYPE_CO2:
		    		 c= (b[i+1]<<8);
		    		 d = (c|(b[i+2]));
	    		 	addToJsonList(attributes_counter,"Co2",Integer.toString(d),"integer","unit","ppm","string");
	    		 	i=i+1;
	    		 	attributes_counter++;
	    		 break;
		    case TYPE_AIR_QUALITY:
                int tempIndex = i + 1;
                float pm1 = Float.intBitsToFloat( b[tempIndex + 3] ^ b[tempIndex + 2] << 8 ^ b[tempIndex + 1] << 16 ^ b[tempIndex] << 24 );
                addToJsonList(attributes_counter++, "PM1", Float.toString(pm1), "float", "unit", "ug/m3", "string");
                tempIndex += 4;
                float pm25 = Float.intBitsToFloat( b[tempIndex + 3] ^ b[tempIndex + 2] << 8 ^ b[tempIndex + 1] << 16 ^ b[tempIndex] << 24 );
                addToJsonList(attributes_counter++, "PM25", Float.toString(pm25), "float", "unit", "ug/m3", "string");
                tempIndex += 4;
                float pm10 = Float.intBitsToFloat( b[tempIndex + 3] ^ b[tempIndex + 2] << 8 ^ b[tempIndex + 1] << 16 ^ b[tempIndex] << 24 );
                addToJsonList(attributes_counter++, "PM10", Float.toString(pm10), "float", "unit", "ug/m3", "string");
                tempIndex += 4;
                float ppm = Float.intBitsToFloat( b[tempIndex + 3] ^ b[tempIndex + 2] << 8 ^ b[tempIndex + 1] << 16 ^ b[tempIndex] << 24 );
                addToJsonList(attributes_counter++, "NO2", Float.toString(ppm), "float", "unit", "ppm", "string");
                tempIndex += 4;
                float lat = Float.intBitsToFloat( b[tempIndex + 3] ^ b[tempIndex + 2] << 8 ^ b[tempIndex + 1] << 16 ^ b[tempIndex] << 24 );
                addToJsonList(attributes_counter++, "LAT", Float.toString(lat), "float", "unit", "degrees", "string");
                tempIndex += 4;
                float lon = Float.intBitsToFloat( b[tempIndex + 3] ^ b[tempIndex + 2] << 8 ^ b[tempIndex + 1] << 16 ^ b[tempIndex] << 24 );
                addToJsonList(attributes_counter++, "LON", Float.toString(lon), "float", "unit", "degrees", "string");
                tempIndex += 4;
                i += 24;
                break;
		    default: //optional
		    //statements
		    		break;
			}
			
		}		
		return attributes;
		
	}
	private void addToJsonList(int attributes_counter,String name, String value, String value_type,String metadata_name,String metadata_value,String metadata_type ){
		JSONObject obj = new JSONObject();
		JSONObject attribute =  new JSONObject() ;
		JSONArray metadats=new JSONArray();
		JSONObject metadata=new JSONObject();
		try {
			attribute.put("name", name);
			attribute.put("value",value);
			attribute.put("type",value_type);
			
			
			metadata.put("name", metadata_name);
			metadata.put("type", metadata_type);
			metadata.put("value", metadata_value);
			metadats.put(0,metadata);
			attribute.put("metadatas", metadats);
			attributes.put(attributes_counter, attribute);
			LOGGER.info(attribute.toString());
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	public int bin16dec(int bin) {
		 int num= (bin&0xFFFF);
		 
		 //System.out.println(ConvertByteToBinaryString_Demo.byteToBinaryStringV3());
		 int x3= (0x8000&num);
		 //System.out.println(ConvertByteToBinaryString_Demo.byteToBinaryStringV3(num));
		 if(x3!=0) {
			 num= -(0x010000 - num);
		 }
			 
		
	    return num;
	}
	public int bin8dec(int bin) {
		 int num= (bin&0xFFFF);
		 
		 //System.out.println(ConvertByteToBinaryString_Demo.byteToBinaryStringV3());
		 int x3= (0x80&num);
		 //System.out.println(ConvertByteToBinaryString_Demo.byteToBinaryStringV3(num));
		 if(x3!=0) {
			 num= -(0x0100 - num);
		 }
			 
		
	    return num;
	}
	public  int[] hexStringToByteArray(String s) {
	    int[] b = new int[s.length() / 2];
	    for (int i = 0; i < b.length; i++) {
	      int index = i * 2;
	      int v = Integer.parseInt(s.substring(index, index + 2), 16);
	      
	      b[i] =  v;
	    }
	    return b;
	  }
}
